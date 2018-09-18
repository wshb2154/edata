package com.jspxcms.plug.service.edata.entity;

import java.util.Date;

/**
 * @table bd_powerstation_edata
 */
public class Edata {
	
	private Integer id;
	private String changm;//厂名
	private String dianh; //点号
	private String dianhmc;
	private Double dianhz;//点号值
	private String zhilbz;//质量标志 1正常0异常
	private String diancjc;//电厂英文简称
	private Date filesj;
	private Date lursj;
	
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
	public Double getDianhz() {
		return dianhz;
	}
	public String getZhilbz() {
		return zhilbz;
	}
	public void setZhilbz(String zhilbz) {
		this.zhilbz = zhilbz;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDianhmc() {
		return dianhmc;
	}
	public void setDianhmc(String dianhmc) {
		this.dianhmc = dianhmc;
	}
	public String getDiancjc() {
		return diancjc;
	}
	public void setDiancjc(String diancjc) {
		this.diancjc = diancjc;
	}
	public Date getFilesj() {
		return filesj;
	}
	public void setFilesj(Date filesj) {
		this.filesj = filesj;
	}
	public Date getLursj() {
		return lursj;
	}
	public void setLursj(Date lursj) {
		this.lursj = lursj;
	}
	public void setDianhz(Double dianhz) {
		this.dianhz = dianhz;
	}
	
}
