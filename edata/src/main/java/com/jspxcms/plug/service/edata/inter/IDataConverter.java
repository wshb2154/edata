package com.jspxcms.plug.service.edata.inter;

import java.util.List;

import com.jspxcms.plug.service.edata.entity.EdataFile;

public interface IDataConverter {

	void batchConvertEFiledataToDBdata(List<EdataFile> efiles);
	
	void convertEFiledataToDBdata(EdataFile efile);

}