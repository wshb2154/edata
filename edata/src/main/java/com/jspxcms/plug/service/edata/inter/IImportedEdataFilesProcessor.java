package com.jspxcms.plug.service.edata.inter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.jspxcms.plug.service.edata.entity.EdataFile;

/**
 * 用来操作已处理数据文件的类
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public interface IImportedEdataFilesProcessor {

	void process(List<EdataFile> efiles) throws IOException;

	void processAndLog(List<EdataFile> efiles, Date beginTime)throws IOException;


}
