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

public class CountDianhCalculator implements DianhCalculator{

	private String stationFlag;
	
	public CountDianhCalculator(EdataConfigForStation edataConfig) {
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
		if (tData==null) {
			throw new RuntimeException(stationFlag+" "+dianh+"号位 今日数据不全。 "+today);
		}
		return NumberUtils.createBigDecimal(tData.get("dianhz").toString()).doubleValue();
	}

}
