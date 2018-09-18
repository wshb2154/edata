package com.jspxcms.plug.service.edata.calc.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.service.edata.CacheHelper;
import com.jspxcms.plug.service.edata.calc.DianhCalculator;
import com.jspxcms.plug.service.edata.entity.EdataConfigForStation;
import com.jspxcms.plug.sql.SQL_EDATA;

/**
 * 计算当天的实时点号值
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class DayDianhCalculator implements DianhCalculator {


	private String stationFlag;
	private Date beginTime;  //昨天
	private Date endTime;  //今天
	
	
	private DayDianhCalculator() {
	}

	public DayDianhCalculator(EdataConfigForStation edataConfig) {
		this.stationFlag = edataConfig.getDiancm();
	}

	@Override
	public Double calcDianhValue(String dianh) {
		return calcDianhValue(dianh, new Date());
	}

	@Override
	public Double calcDianhValue(String dianh, Date date) {
		Date today = DateUtils.truncate(date, Calendar.DATE);
		SqlServiceA sqlService = CacheHelper.getSqlService();
		Map<String, Object> tData = sqlService.selectToMap(SQL_EDATA.DIANHZ_BY_DATE, dianh,stationFlag,today);
		Map<String, Object> yData = sqlService.selectToMap(SQL_EDATA.DIANHZ_BY_DATE, dianh,stationFlag,DateUtils.addDays(today, -1));
		if (tData==null||yData==null) {
			throw new RuntimeException(stationFlag+" "+dianh+"号位 今日和昨日数据不全。 "+today);
		}
		this.beginTime = (Date) yData.get("maxsj");
		this.endTime = (Date) tData.get("maxsj");
		return NumberUtils.createBigDecimal(tData.get("dianhz").toString()).subtract(NumberUtils.createBigDecimal(yData.get("dianhz").toString())).doubleValue();
	}
	
	public static void main(String[] args) {
		DayDianhCalculator dc = new DayDianhCalculator();
		dc.stationFlag = "阿特斯高科光伏电厂";
		Double value = dc.calcDianhValue("2");
		System.out.println(value);
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	
}
