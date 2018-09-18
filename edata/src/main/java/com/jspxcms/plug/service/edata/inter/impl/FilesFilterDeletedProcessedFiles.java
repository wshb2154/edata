package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.jspxcms.plug.service.edata.inter.IFilesFilter;

/**
 * 文件搜集类，处理后得到需要处理的文件集合
 * 数据导入后文件处理方式是：处理完文件后删除，所以目录下的所有文件都是需要处理的文件集合
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class FilesFilterDeletedProcessedFiles implements IFilesFilter {
	
	private Logger log = Logger.getLogger(FilesFilterDeletedProcessedFiles.class);

	private String dataDirpath;
	private EdataFileParser edataFileParser;
	
	public FilesFilterDeletedProcessedFiles(String dataDirpath) {
		this.dataDirpath = dataDirpath;
		this.edataFileParser = new EdataFileParser();
	}

	@Override
	public List<File> pickFilesToProcess() {
		File dir = new File(dataDirpath);
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if(edataFileParser.isEdataFile(file)) return true;
				return false;
			}
		});
		if(files == null) return new ArrayList<>();
		return Arrays.asList(files);
	}

}

