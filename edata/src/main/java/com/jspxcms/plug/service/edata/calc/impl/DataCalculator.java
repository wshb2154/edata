package com.jspxcms.plug.service.edata.calc.impl;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.domain.PowerStationInfo;
import com.jspxcms.plug.service.edata.CacheHelper;
import com.jspxcms.plug.sql.SQL_EDATA;

public class DataCalculator {
	
	private SqlServiceA sqlService = CacheHelper.getSqlService();
	
	public void update(PowerStationInfo ps, Date date) {
		//更新日(历史)发电量
		updateDayHis(ps, date);
		//更新月(实时)发电量
		//更新月(历史)发电量
		updateMonth(ps,date);
		//更新年(实时)发电量
		//更新年(历史)发电量
		updateYear(ps,date);
		//页面显示需要的其他数据{发电收益，标煤节约量。。。}
	}

	private void updateYear(PowerStationInfo ps, Date date) {
		String year = DateFormatUtils.format(date, "yyyy");
		sqlService.executeSql(SQL_EDATA.UPDATE_YEAR_ELEC, ps.getId(),year,ps.getId());
		sqlService.executeSql(SQL_EDATA.UPDATE_YEAR_HIS_DEL, ps.getId(),year);
		sqlService.executeSql(SQL_EDATA.UPDATE_YEAR_HIS, year,ps.getId());
	}

	private void updateMonth(PowerStationInfo ps, Date date) {
		String month = DateFormatUtils.format(date, "yyyy-MM");
		sqlService.executeSql(SQL_EDATA.UPDATE_MONTH_ELEC, ps.getId(),month,ps.getId());
		sqlService.executeSql(SQL_EDATA.UPDATE_MONTH_HIS_DEL, ps.getId(),month);
		sqlService.executeSql(SQL_EDATA.UPDATE_MONTH_HIS, month,ps.getId());
	}

	private void updateDayHis(PowerStationInfo ps, Date date) {
		String day = DateFormatUtils.format(date, "yyyy-MM-dd");
		sqlService.executeSql(SQL_EDATA.UPDATE_DAY_HIS_DEL, ps.getId(),day);
		sqlService.executeSql(SQL_EDATA.UPDATE_DAY_HIS, day,ps.getId());
	}

	public void updateTotalData() {
		sqlService.executeSql(SQL_EDATA.UPDATE_TOTAL);
	}
	
}
