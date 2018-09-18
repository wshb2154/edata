package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.jspxcms.plug.service.edata.inter.IFilesFilter;

/**
 * 文件搜集类，处理后得到需要处理的文件集合。
 * 处理数据文件在不同的多个文件夹下的情况。
 * 数据导入后文件处理方式是：处理完文件后删除，所以目录下的所有文件都是需要处理的文件集合
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class FilesFilterForMultiDir implements IFilesFilter {
	
	private Logger log = Logger.getLogger(FilesFilterForMultiDir.class);

	private String dataDirpath;
	private EdataFileParser edataFileParser;
	
	public FilesFilterForMultiDir(String dataDirpath) {
		this.dataDirpath = dataDirpath;
		this.edataFileParser = new EdataFileParser();
	}

	@Override
	public List<File> pickFilesToProcess() {
		File dir = new File(dataDirpath);
		return (List<File>) FileUtils.listFiles(dir, new EdataFileFilter(), new EdataDirFilter());
	}
	
	private class EdataFileFilter implements IOFileFilter{
		
		@Override
		public boolean accept(File file) {
			if (edataFileParser.isEdataFile(file)) {
				return true;
			}
			return false;
		}
		
		@Override
		public boolean accept(File dir, String name) {
			return false;
		}
		
	}
	
	private class EdataDirFilter implements IOFileFilter{
		private String[] dirs = {"CX","DZ","FZ","NX"};
		@Override
		public boolean accept(File file) {
			if (ArrayUtils.contains(dirs, file.getName())) {
				return true;
			}
			return false;
		}
		
		@Override
		public boolean accept(File dir, String name) {
			return false;
		}
		
	}
}

