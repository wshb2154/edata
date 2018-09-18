package com.jspxcms.plug.service.edata.entity;

/**
 * @table bd_edata_ps
 * 
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class EdataConfigForStation {
	
	private Integer id;
	private Integer psid;
	private String diancjc;
	private String diancm;
	private String factor;
	private String dianh;
	private String math;
	private String type;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPsid() {
		return psid;
	}
	public void setPsid(Integer psid) {
		this.psid = psid;
	}
	public String getDiancjc() {
		return diancjc;
	}
	public void setDiancjc(String diancjc) {
		this.diancjc = diancjc;
	}
	public String getDiancm() {
		return diancm;
	}
	public void setDiancm(String diancm) {
		this.diancm = diancm;
	}
	public String getFactor() {
		return factor;
	}
	public void setFactor(String factor) {
		this.factor = factor;
	}
	public String getDianh() {
		return dianh;
	}
	public void setDianh(String dianh) {
		this.dianh = dianh;
	}
	public String getMath() {
		return math;
	}
	public void setMath(String math) {
		this.math = math;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
