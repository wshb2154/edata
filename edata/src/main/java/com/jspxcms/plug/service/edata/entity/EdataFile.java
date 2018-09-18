package com.jspxcms.plug.service.edata.entity;

import java.util.List;

public class EdataFile {
	
	private String fileName;//文件的绝对路径
	private String diancjc;//电厂简称
	private String filesj;//文件时间
	
	private String area;//地区
	private String datasj;//可读时间， 文件时间应该和可读时间是一样的
	
	private List<EdataFileData> edatas;
	//数据文件处理结果
	private boolean success;//是否成功导入数据
	private byte errorType;//失败类型，失败原因
	
	public static byte ERROR_PARSE=1;//文件解析错误
	public static byte ERROR_DB=2;//导入数据库错误
	
	public static String fileCharset = "gbk"; //文件编码
	public static String valueSplitString = "\t";//值的分隔符

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDiancjc() {
		return diancjc;
	}

	public void setDiancjc(String diancjc) {
		this.diancjc = diancjc;
	}

	public String getFilesj() {
		return filesj;
	}

	public void setFilesj(String filesj) {
		this.filesj = filesj;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDatasj() {
		return datasj;
	}

	public void setDatasj(String datasj) {
		this.datasj = datasj;
	}

	public List<EdataFileData> getEdatas() {
		return edatas;
	}

	public void setEdatas(List<EdataFileData> edatas) {
		this.edatas = edatas;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public byte getErrorType() {
		return errorType;
	}

	public void setErrorType(byte errorType) {
		this.errorType = errorType;
	}
	
}
