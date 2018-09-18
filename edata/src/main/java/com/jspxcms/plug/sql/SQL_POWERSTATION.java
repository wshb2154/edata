package com.jspxcms.plug.sql;

public class SQL_POWERSTATION {
	
	public static final String dataForStationVideos = "select v.*,i.name psname "
			+ "from bd_powerstation_video v inner join bd_powerstation_info i on v.psid = i.id "
			+ "where videoshow=1 order by videoorder";

	public static String dataForStationsSql = 
	"select info.id stationid,info.name stationname,info.address,info.scale,\n" +
	"	day1.fadl dayfadl,day1.qdl dayqidl,\n" +
	"	month1.fadl monthfadl,month1.qdl monthqidl,\n" +
	"	year1.fadl yearfadl,year1.qdl yearqidl\n" +
	"	from bd_powerstation_info info \n" +
	"	left join bd_powerstation_day day1 on info.id=day1.psid\n" +
	"	left join bd_powerstation_month month1 on info.id=month1.psid\n" +
	"	left join bd_powerstation_year year1 on info.id=year1.psid order by info.sortnum";
	
	public static String dataForTotalSql = "select * from bd_powerstation_total";
	
	public static String dataForStationInfo = "select * from bd_powerstation_info";
	
	//日发电量（echart用）、
	public static String echart_day_ele = "select * from (select sj,fadl from bd_powerstation_day_h where psid=? order by sj desc)t limit 0,30";
	//月发电量（echart用）
	public static String echart_month_ele = "select * from (select sj,fadl from bd_powerstation_month_h where psid=? order by sj desc)t limit 0,12";
}
