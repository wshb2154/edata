package com.jspxcms.plug.service.edata;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.service.edata.entity.EdataConfigForStation;
import com.jspxcms.plug.sql.SQL_EDATA;
import com.jspxcms.plug.tool.ApplicationContextUtil;

/**
 * 系统中需要缓冲的数据和单例
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class CacheHelper {
	
	private static Properties fileConfig;  //文件配置：对应src/main/resources/config.properties文件
	private static Map<String, String> dbConfig; //数据库配置：对应bd_config表
	private static Map<Integer, EdataConfigForStation> psConfig; //电厂ID_计算电量相关的配置。数据库配置：bd_edata_ps表
	private static SqlServiceA sqlService ;

	/**
	 * 文件配置：对应src/main/resources/config.properties文件
	 * @return
	 */
	public static Properties getFileConfig() {
		if(fileConfig==null) {
			InputStream configIn = null;
			try {
				configIn = TomcatListener.class.getClassLoader().getResourceAsStream("config.properties");
				fileConfig = new Properties();
				fileConfig.load(configIn);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					configIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileConfig;
	}
	
	/**
	 * //数据库配置：对应bd_config表
	 * @return
	 */
	public static Map<String, String> getDbConfig() {
		if(dbConfig ==null) {
			dbConfig = new LinkedHashMap<>();
			List list = getSqlService().selectToListWithMaps(SQL_EDATA.EDATA_CONFIG);
			for(Object object:list) {
				Map<String,String> map = (Map<String,String>)object;
				dbConfig.put(map.get("cname"), map.get("cvalue"));
			}
		}
		return dbConfig;
	}

	public static SqlServiceA getSqlService() {
		if(sqlService==null) {
			sqlService = ApplicationContextUtil.getContext().getBean(SqlServiceA.class);
//			sqlService = new SqlServiceAImpl(new DBUtil().getDatasource_mysql());
		}
		return sqlService;
	}

	
	/**
	 * //电厂ID_计算电量相关的配置。数据库配置：bd_edata_ps表
	 * @return
	 */
	public static Map<Integer, EdataConfigForStation> getStaticPsConfig() {
		if (psConfig == null) {
			//psConfig = new HashMap<>();
			List<EdataConfigForStation> eConfigs = getSqlService().selectToListWithBeans(SQL_EDATA.EDATA_CONFIG_STATION, 
					EdataConfigForStation.class);
			psConfig = Maps.uniqueIndex(eConfigs, new Function<EdataConfigForStation, Integer>() {
				
				@Override
				public Integer apply(EdataConfigForStation config) {
					return config.getPsid();
				}
			});
			
		}
		return psConfig;
	}
	public static Map<Integer, EdataConfigForStation> getPsConfig() {
			List<EdataConfigForStation> eConfigs = getSqlService().selectToListWithBeans(SQL_EDATA.EDATA_CONFIG_STATION, 
					EdataConfigForStation.class);
			return Maps.uniqueIndex(eConfigs, new Function<EdataConfigForStation, Integer>() {
				
				@Override
				public Integer apply(EdataConfigForStation config) {
					return config.getPsid();
				}
			});
	}
	
}
