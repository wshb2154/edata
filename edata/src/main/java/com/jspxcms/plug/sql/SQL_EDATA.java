package com.jspxcms.plug.sql;

public interface SQL_EDATA {
	
	/**
	 * 电厂_Edata数据文件的配置信息
	 */
	String EDATA_CONFIG_STATION = "select * from bd_edata_ps";
	
	/**
	 * 文件导入-过滤文件
	 */
	String LATEST_IMPORTDATA_SQL = "select diancjc,max(filesj) filesj from bd_powerstation_edata group by diancjc";
	
	/**
	 * 获取点号值，根据点号、厂名、时间获取点号值
	 * 获取最新的时间下的点号值
	 * @param dianh 点号
	 * @param changm
	 * @param convert(filesj,date) yyyy-MM-dd格式的时间
	 */
	String DIANHZ_BY_DATE = "select dianhz,max(filesj) maxsj from bd_powerstation_edata where dianh=? and changm=? and convert(filesj,date)=? group by dianhz order by maxsj desc";

	/**
	 * 数据库日志
	 * @param msg
	 * @param type 1文件导入2数据运算
	 */
	String LOG_SQL = "insert into bd_edata_log (msg,type) values (?,?)";
	//更新累积发电量
	String UPDATE_COUNT_ELEC = "update bd_powerstation_year set countfadl=? where psid=?";
	
	//更新日(实时)发电量
	String UPDATE_DAY_ELEC = "update bd_powerstation_day set fadl=?,comment=? where psid=?";

	//更新月(实时)发电量
	String UPDATE_MONTH_ELEC = "update bd_powerstation_month set "
			+ "fadl=("
			+ "select sum(fadl) from bd_powerstation_day_h where psid=? and left(sj,7)=?"
			+ ") where psid=?";
	//更新年(实时)发电量
	String UPDATE_YEAR_ELEC = "update bd_powerstation_year set "
			+ "fadl=("
			+ "select sum(fadl) from bd_powerstation_month_h where psid=? and left(sj,4)=?"
			+ ") where psid=?";

	//删除日(历史)发电量
	//删除月(历史)发电量
	//删除年(历史)发电量
	String UPDATE_DAY_HIS_DEL = "delete from bd_powerstation_day_h where psid=? and sj=?";
	String UPDATE_MONTH_HIS_DEL = "delete from bd_powerstation_month_h where psid=? and sj=?";
	String UPDATE_YEAR_HIS_DEL = "delete from bd_powerstation_year_h where psid=? and sj=?";
	
	//更新日(历史)发电量
	String UPDATE_DAY_HIS = "insert into bd_powerstation_day_h(psid,fadl,sj,comment) "
			+ "select psid,fadl,?,comment from bd_powerstation_day where psid=?";
	//更新月(历史)发电量
	String UPDATE_MONTH_HIS = "insert bd_powerstation_month_h (psid,fadl,sj)"
			+ "select psid,fadl,? from bd_powerstation_month where psid=?";
	//更新年(历史)发电量
	String UPDATE_YEAR_HIS = "insert bd_powerstation_year_h (psid,fadl,sj) "
			+ "select psid,fadl,? from bd_powerstation_year where psid=?";

	//更新total表数据
	String UPDATE_TOTAL = "update bd_powerstation_total set fadl=(select sum(fadl) from bd_powerstation_day),"
			+ "totalfdl=(select sum(countfadl)/1000 from bd_powerstation_year)";
	
	String EDATA_CONFIG = "select * from bd_edata_config";
}
