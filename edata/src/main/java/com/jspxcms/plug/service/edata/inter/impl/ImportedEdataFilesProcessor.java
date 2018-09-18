package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jspxcms.plug.service.edata.TaskManager;
import com.jspxcms.plug.service.edata.entity.EdataFile;
import com.jspxcms.plug.service.edata.inter.IImportedEdataFilesProcessor;

public class ImportedEdataFilesProcessor implements IImportedEdataFilesProcessor {

	private String edataDirpath;

	public ImportedEdataFilesProcessor(String edataDirpath) {
		this.edataDirpath = edataDirpath;
	}

	@Override
	public void process(List<EdataFile> efiles) {
	}

	@Override
	public void processAndLog(List<EdataFile> efiles, Date beginTime) throws IOException {
		File bakDir = new File(edataDirpath, "文件备份");//正常备份
		File bakErrorDir = new File(edataDirpath, "文件备份-error");//出错备份
		String taskbh = DateFormatUtils.format(beginTime, "yyyyMMddHHmmss");
		int successNum=0;
		int parseErrorNum=0;
		int dbErrorNum=0;
		for(EdataFile efile:efiles) {
			File srcFile = new File(efile.getFileName());
			if(efile.isSuccess()) {
				successNum++;
				FileUtils.moveFile(srcFile, new File(bakDir, "T"+taskbh+"_"+srcFile.getName()));
			}else {
				if(EdataFile.ERROR_PARSE == efile.getErrorType()) {
					parseErrorNum++;
				}else if (EdataFile.ERROR_DB == efile.getErrorType()) {
					dbErrorNum++;
				}
				FileUtils.moveFile(srcFile, new File(bakErrorDir, "T"+taskbh+"_"+srcFile.getName()));
			}
		}
		Date endTime = new Date();
		StringBuffer logMsg = new StringBuffer();
		logMsg.append("执行"+taskbh+"号文件导入任务，");
		logMsg.append("此次转换文件个数："+efiles.size()+"，");
		logMsg.append("用时(ms)："+(endTime.getTime()-beginTime.getTime())+"。");
		logMsg.append("成功["+successNum+"]，");
		logMsg.append("文件解析失败["+parseErrorNum+"]，");
		logMsg.append("数据插入失败["+dbErrorNum+"]。");
		dbLog(logMsg.toString());
	}

	private void dbLog(String logMsg) {
		TaskManager.dbLog(logMsg, "1");
	}

}
