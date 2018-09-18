package com.jspxcms.core.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jspxcms.core.service.SqlServiceA;

@Service
@Transactional
public class SqlServiceAImpl implements SqlServiceA {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public SqlServiceAImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Map<String, Object> selectToMap(String sql, Object... params) {
		List<Map<String, Object>> listWithMap = jdbcTemplate.queryForList(sql, params);
		if (CollectionUtils.isEmpty(listWithMap)) {
			return null;
		}
		return listWithMap.get(0);
	}

	@Override
	public <T> T selectToEntity(String sql,Class<T> clazz,Object... params) {
		try {
			Map<String, Object> map = selectToMap(sql, params);
			T bean = clazz.newInstance();
			BeanUtils.populate(bean, map);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException("map装换到"+clazz.getSimpleName()+"出错！",e);
		}
	}
	
	@Override
	public void executeProcedure(String sql,Object... params) {
		jdbcTemplate.update(sql, params);
	}

	@Override
	public void executeSql(String sql, Object... params) {
		jdbcTemplate.update(sql, params);
	}

	@Override
	public List<Map<String, Object>> selectToListWithMaps(String sql, Object... params) {
		return jdbcTemplate.queryForList(sql, params);
	}

	@Override
	public <T> List<T> selectToListWithBeans(String sql, Class<T> beanClass, Object... params) {
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(beanClass), params);
	}

	@Override
	public String buildCriteriaSql(String commonSql, Map<String,Object> wherePart) {
		String cs = "select * from ("+commonSql+") where 1=1";
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return jdbcTemplate.getDataSource().getConnection();
	}

	@Override
	public void executeBatchSql(String batchSql, List<Object[]> bathParams) {
		jdbcTemplate.batchUpdate(batchSql, bathParams);
	}
	

}
