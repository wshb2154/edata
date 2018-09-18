package com.jspxcms.plug.service.edata.inter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jspxcms.plug.service.edata.entity.EdataFile;
import com.jspxcms.plug.service.edata.entity.EdataFileData;
import com.jspxcms.plug.service.edata.inter.IEdataFileParser;

public class EdataFileParser implements IEdataFileParser {

	private static final String EDATA_SUFFIX = ".DT";
	
	@Override
	public EdataFile parse(File file) {
		EdataFile edataFile = new EdataFile();
		try {
			//根据文件名取文件名（包含文件路径）、电厂简称、文件时间
			edataFile.setFileName(file.getAbsolutePath());
			String[] jcAndSj = analysisFileName(file);
			edataFile.setDiancjc(jcAndSj[0]);
			edataFile.setFilesj(jcAndSj[1]);
			//获取E文件头信息(地区、数据时间)和数据信息(厂名、点号、值、质量标志)
			List<String> lines = FileUtils.readLines(file,EdataFile.fileCharset);
			boolean isEDataHeadEnd = false; //标题部分是否读取完毕
			List<EdataFileData> edatas = new ArrayList<>();
			edataFile.setEdatas(edatas);
			for(String line:lines) {
				line = line.trim();
				if(!isEDataHeadEnd) {//读取数据HEAD部分
					if(!line.startsWith("#")) {
						if(line.contains("<YmValue>")) isEDataHeadEnd = true;
						continue;
					}else {
						String[] headInfos = line.split(EdataFile.valueSplitString);
						edataFile.setArea(headInfos[1]);
						edataFile.setDatasj(headInfos[3]);
					}
				}else { //读取数据BODY部分
					if(!line.startsWith("#")) continue;
					else {
						EdataFileData edata = new EdataFileData();
						String[] datas = line.split(EdataFile.valueSplitString);
						edata.setChangm(datas[1]);
						edata.setDianh(datas[2]);
						edata.setDianhz(datas[3]);
						edata.setZhilbz(datas[4]);
						edatas.add(edata);
					}
				}
				
			}
		} catch (Exception e) {
			edataFile.setSuccess(false);
			edataFile.setErrorType(EdataFile.ERROR_PARSE);
			e.printStackTrace();
			//throw new EdataFileParserException(e.getMessage(),e);
		}
		return edataFile;
	}
	
	public boolean isEdataFile(File file) {
		if(!StringUtils.endsWithIgnoreCase(file.getName(), EDATA_SUFFIX)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 分析文件名，等到电厂英文简称和文件时间
	 * @param file
	 * @return [电厂英文简称,文件时间]
	 */
	public String[] analysisFileName(File file){
		String[] fileNameInfos = StringUtils.removeEndIgnoreCase(file.getName(), EDATA_SUFFIX).split("_");
		String ejc = StringUtils.join(ArrayUtils.subarray(fileNameInfos, 1, ArrayUtils.indexOf(fileNameInfos, "YM")),"_");
		String filesj = fileNameInfos[fileNameInfos.length-1];
		return new String[] {ejc,filesj};
	}

}
