package com.jspxcms.plug.service.edata.entity;

/**
 * 只用做导入的时候用，不适用数据库的edata
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class EdataFileData {
	
	private String changm;//厂名
	private String dianh; //点号
	private String dianhz;//点号值
	private String zhilbz;//质量标志 1正常0异常
	
	
	public String getChangm() {
		return changm;
	}
	public void setChangm(String changm) {
		this.changm = changm;
	}
	public String getDianh() {
		return dianh;
	}
	public void setDianh(String dianh) {
		this.dianh = dianh;
	}
	public String getDianhz() {
		return dianhz;
	}
	public void setDianhz(String dianhz) {
		this.dianhz = dianhz;
	}
	public String getZhilbz() {
		return zhilbz;
	}
	public void setZhilbz(String zhilbz) {
		this.zhilbz = zhilbz;
	}

}
