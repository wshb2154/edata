package com.jspxcms.core.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 常用数据库sql操作
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public interface SqlServiceA {
	
	public Map<String, Object> selectToMap(String sql, Object... params) ;

	public void executeProcedure(String sql,Object... params);

	public void executeSql(String sql, Object... params) ;

	public List<Map<String, Object>> selectToListWithMaps(String sql, Object... params) ;

	public <T> List<T> selectToListWithBeans(String sql, Class<T> beanClass, Object... params) ;

	<T> T selectToEntity(String sql, Class<T> clazz, Object... params);

	public String buildCriteriaSql(String commonSql, Map<String,Object> wherePart);
	
	Connection getConnection() throws SQLException;

	public void executeBatchSql(String batchSql, List<Object[]> bathParams);
	

}
