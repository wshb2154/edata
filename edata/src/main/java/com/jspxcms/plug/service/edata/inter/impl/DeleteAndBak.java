package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jspxcms.plug.service.edata.entity.EdataFile;
import com.jspxcms.plug.service.edata.inter.IImportedEdataFilesProcessor;

public class DeleteAndBak implements IImportedEdataFilesProcessor {

	private String edataDirpath;
	
	public DeleteAndBak(String edataDirpath) {
		this.edataDirpath = edataDirpath;
	}

	@Override
	public void process(List<EdataFile> efiles) throws IOException {
		File bakDir = new File(edataDirpath, "文件备份");
		for(EdataFile efile:efiles) {
			FileUtils.moveFileToDirectory(new File(efile.getFileName()), bakDir, true);
		}
	}

	@Override
	public void processAndLog(List<EdataFile> efiles, Date beginTime) throws IOException {
		File bakDir = new File(edataDirpath, "文件备份");
		String taskbh = DateFormatUtils.format(beginTime, "yyyyMMddHHmmss");
		for(EdataFile efile:efiles) {
			File srcFile = new File(efile.getFileName());
			FileUtils.moveFile(srcFile, new File(bakDir, "T"+taskbh+"_"+srcFile.getName()));
		}
		Date endTime = new Date();
		System.out.println("执行"+taskbh+"号任务，"
				+ "此次转换文件个数："+efiles.size()+","
						+ "用时(ms)："+(endTime.getTime()-beginTime.getTime()));
	}

}
