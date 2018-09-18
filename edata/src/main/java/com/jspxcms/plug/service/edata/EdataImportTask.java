package com.jspxcms.plug.service.edata;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jspxcms.plug.service.edata.entity.EdataFile;
import com.jspxcms.plug.service.edata.inter.EdataTask;
import com.jspxcms.plug.service.edata.inter.IDataConverter;
import com.jspxcms.plug.service.edata.inter.IEdataFileParser;
import com.jspxcms.plug.service.edata.inter.IFilesFilter;
import com.jspxcms.plug.service.edata.inter.IImportedEdataFilesProcessor;
import com.jspxcms.plug.service.edata.inter.impl.DataConverter;
import com.jspxcms.plug.service.edata.inter.impl.EdataFileParser;
import com.jspxcms.plug.service.edata.inter.impl.FilesFilterForMultiDir;
import com.jspxcms.plug.service.edata.inter.impl.ImportedEdataFilesProcessor;

/**
 * 导入电厂数据任务
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class EdataImportTask implements EdataTask {

	private Logger log = Logger.getLogger(EdataImportTask.class);
	
	public static long period;//1*60*60*1000; // 任务执行间隔时间
	private String edataDirpath; //存放电厂数据文件的文件夹地址
	private IFilesFilter filesFilter;
	private IEdataFileParser fileParser;
	private IDataConverter dataConverter;
	private IImportedEdataFilesProcessor importedEdataFilesProcessor;
	private Map<String,String> edataConfig = new HashMap<>();
	
	public EdataImportTask() {
		edataConfig = CacheHelper.getDbConfig();
		edataDirpath = edataConfig.get("dirpath");
		period = Math.round(Double.parseDouble(edataConfig.get("period"))*60*1000);
		filesFilter = new FilesFilterForMultiDir(edataDirpath);
		fileParser = new EdataFileParser();
		dataConverter = new DataConverter();
		importedEdataFilesProcessor = new ImportedEdataFilesProcessor(edataDirpath);
	}

	@Override
	public void execute() {
		try {
			Date beginTime = new Date();
			//选出将要处理的文件
			List<File> filesToProcess = filesFilter.pickFilesToProcess();
			if (filesToProcess.size() == 0) return;
			//解析文件
			List<EdataFile> efiles = new ArrayList<EdataFile>();
			for (File file : filesToProcess) {
				EdataFile efile = fileParser.parse(file);
				efiles.add(efile);
			}
			//批量入库
			dataConverter.batchConvertEFiledataToDBdata(efiles);
			//最后处理操作过的数据文件,包含操作日志处理
			importedEdataFilesProcessor.processAndLog(efiles,beginTime);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	public String getEdataDirpath() {
		return edataDirpath;
	}

	public static void main(String[] args) {
		new EdataImportTask().execute();
	}

}
