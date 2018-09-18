package com.jspxcms.plug.service.edata;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.domain.PowerStationInfo;
import com.jspxcms.plug.service.edata.calc.ElectricCalculator;
import com.jspxcms.plug.service.edata.calc.impl.CountDianhCalculator;
import com.jspxcms.plug.service.edata.calc.impl.DataCalculator;
import com.jspxcms.plug.service.edata.calc.impl.DayDianhCalculator;
import com.jspxcms.plug.service.edata.calc.impl.ElectricCalculatorImpl;
import com.jspxcms.plug.service.edata.entity.EdataConfigForStation;
import com.jspxcms.plug.service.edata.inter.EdataTask;
import com.jspxcms.plug.sql.SQL_EDATA;
import com.jspxcms.plug.sql.SQL_POWERSTATION;

/**
 * 更新实时的日发电量
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class EdataUpdateRealtimeTask implements EdataTask{

	private Logger log = Logger.getLogger(EdataTask.class);
	DataCalculator dataCalculator = new DataCalculator();
	@Override
	public void execute() {
		Date beginTime = new Date();
		List<PowerStationInfo> psInfos = listPowerstationInfos();
		int successNum = 0;
		int failNum = 0;
		//更新每个电厂的实时、日、月、年历史的电量数据
		Map<Integer, EdataConfigForStation> psConfig = CacheHelper.getPsConfig();
		for(PowerStationInfo ps:psInfos) {
			try {
				//和计算发电量相关的参数
				EdataConfigForStation edataConfig = psConfig.get(ps.getId());
				if (edataConfig == null) {
					failNum++;
					continue;
				}
				//计算电厂实时发电量
				//new DayDianhCalculator(edataConfig);
				ElectricCalculator eCalc = new ElectricCalculatorImpl(edataConfig,new DayDianhCalculator(edataConfig));
				//计算电厂累积发电量
				ElectricCalculator countCalc = new ElectricCalculatorImpl(edataConfig,new CountDianhCalculator(edataConfig));
				//更新数据库
				updateDB(ps, eCalc,countCalc);
				successNum++;
			} catch (Exception e) {
				failNum++;
				log.error("",e);
			}
		}
		//13个电厂更新完后，更新总的电量与其他信息。
		dataCalculator.updateTotalData();
		//记录日志
		StringBuffer logMsg = new StringBuffer();
		logMsg.append("执行"+DateFormatUtils.format(beginTime, "yyyyMMddHHmmss")+"号update实时电量任务，");
		logMsg.append("电厂个数:"+psInfos.size()+",");
		logMsg.append("用时(ms):"+(new Date().getTime()-beginTime.getTime())+"。");
		logMsg.append("成功["+successNum+"],");
		logMsg.append("失败["+failNum+"]。");
		TaskManager.dbLog(logMsg.toString(), "2");
	}

	private List<PowerStationInfo> listPowerstationInfos() {
		SqlServiceA sqlService = CacheHelper.getSqlService();
		return sqlService.selectToListWithBeans(SQL_POWERSTATION.dataForStationInfo, PowerStationInfo.class);
	}

	private void updateDB(PowerStationInfo ps, ElectricCalculator eCalc, ElectricCalculator countCalc) throws Exception {
		SqlServiceA sqlService = CacheHelper.getSqlService();
		try {
			Double realElectric = eCalc.calcGenerateElectric();
			DayDianhCalculator dianhCalculator = ((ElectricCalculatorImpl) eCalc).getDianhCalculator();
			String beginTime = DateFormatUtils.format(dianhCalculator.getBeginTime(), "yyyy/MM/dd HH:mm:ss");
			String endTime = DateFormatUtils.format(dianhCalculator.getEndTime(), "yyyy/MM/dd HH:mm:ss");
			String math = ((ElectricCalculatorImpl) eCalc).getMath();
			String comment = endTime + "-" + beginTime + "计算公式:" + math;
			//更新日(实时)发电量
			sqlService.executeSql(SQL_EDATA.UPDATE_DAY_ELEC, realElectric, comment, ps.getId());
			//更新累计发电量
			sqlService.executeSql(SQL_EDATA.UPDATE_COUNT_ELEC, countCalc.calcGenerateElectric(),ps.getId());
		} catch (Exception e) {
			//计算中失败也要更新实时、历史数据。
			sqlService.executeSql(SQL_EDATA.UPDATE_DAY_ELEC, null, null, ps.getId());
			dataCalculator.update(ps,new Date());
			throw new Exception("计算实时电量失败:"+ps.getName(),e);
		}
		//更新其他数据
		dataCalculator.update(ps,new Date());
	}

	public static void main(String[] args) throws Exception {
		EdataUpdateRealtimeTask task = new EdataUpdateRealtimeTask();
		
		EdataConfigForStation config = new EdataConfigForStation();
		config.setDiancm("阿特斯高科光伏电厂");
		config.setDianh("0,2");
		config.setFactor("1000");
		config.setMath("{factor}*({dianh(0)}+{dianh(2)}*2)");
		ElectricCalculatorImpl dec = new ElectricCalculatorImpl(config,new DayDianhCalculator(config));
		
		PowerStationInfo psInfo = new PowerStationInfo();
		psInfo.setId(1);
		psInfo.setName("测试电站");
		
		task.updateDB(psInfo, dec, null);
	}
}
