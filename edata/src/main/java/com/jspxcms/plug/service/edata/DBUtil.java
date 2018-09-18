package com.jspxcms.plug.service.edata;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBUtil {

	DataSource getDatasource_mysql() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL("jdbc:mysql://localhost:3306/jspxcms_yqbigdata?characterEncoding=utf-8");
		dataSource.setUser("root");
		dataSource.setPassword("root");
		return dataSource;
	}

}
