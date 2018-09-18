package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.service.edata.inter.IFilesFilter;
import com.jspxcms.plug.sql.SQL_EDATA;
import com.jspxcms.plug.tool.ApplicationContextUtil;

/**
 * 文件搜集类
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class FilesFilter implements IFilesFilter {
	
	private Logger log = Logger.getLogger(FilesFilter.class);

	private String dataDirpath;
	private EdataFileParser edataFileParser;
	
	public FilesFilter(String dataDirpath) {
		this.dataDirpath = dataDirpath;
	}

	@Override
	public List<File> pickFilesToProcess() {
		File dir = new File(dataDirpath);
		return Arrays.asList(dir.listFiles(new EdataFileFilter()));
	}
	
	private class EdataFileFilter implements FileFilter{
		
		
		//电厂简称_最新数据的导入时间
		//当前数据库中每个电厂对应的最新导入时间
		//用最新导入时间作为筛选下次导入文件的依据。
		private Map<String,Date> stationJC_importedDate; 
		private String ed_suffix = ".DT";
		private SqlServiceA sqlService;

		public EdataFileFilter() {
			sqlService = ApplicationContextUtil.getContext().getBean(SqlServiceA.class);
			stationJC_importedDate = getStationJC_importedDate();
		}
		
		private Map<String, Date> getStationJC_importedDate() {
			HashMap<String, Date> ret = new HashMap<>();
			List<Map<String, Object>> list = sqlService.selectToListWithMaps(SQL_EDATA.LATEST_IMPORTDATA_SQL);
			for(Map<String, Object> map:list) {
				ret.put(map.get("diancjc").toString(), (Date)map.get("filesj"));
			}
			return ret;
		}

		@Override
		public boolean accept (File file) {
			if(!edataFileParser.isEdataFile(file)) {
				return false;
			}
			String[] fileNameInfos = edataFileParser.analysisFileName(file);
			String jc = fileNameInfos[0],sj=fileNameInfos[1];
			try {
				Date hasImportDate = ObjectUtils.defaultIfNull(stationJC_importedDate.get(jc), DateUtils.parseDate("19970101", "yyyyMMdd"));
				Date date = DateUtils.parseDate(sj, "yyyyMMddHHmmss");
				if (date.compareTo(hasImportDate)<=0) {
					return false;
				}
			} catch (ParseException e) {
				log.error("导入文件时，解析文件出错",e);
				return false;
			}
			return true;
		}
		
	}

}

