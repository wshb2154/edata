package com.jspxcms.plug.service.edata.calc.impl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.ObjectUtils;

import com.jspxcms.plug.service.edata.calc.DianhCalculator;
import com.jspxcms.plug.service.edata.calc.ElectricCalculator;
import com.jspxcms.plug.service.edata.entity.EdataConfigForStation;

/**
 * 计算日实时发电量
 * @author wangshaobin
 * @email wshb2154@163.com
 */
public class ElectricCalculatorImpl implements ElectricCalculator {

	private String diancjc;//
	private String diancm;//厂名
	private String factor;//计算电量需要的系数
	private String[] dianhs; //计算电量需要的点号
	private String math;//计算电量的公式
	private DianhCalculator dianhCalculator;
	
	
	public ElectricCalculatorImpl(EdataConfigForStation edataConfig,DianhCalculator dianhCalculator) {
		this.diancjc = edataConfig.getDiancjc();
		this.diancm = edataConfig.getDiancm();
		this.factor = edataConfig.getFactor();
		this.dianhs = dianhArray(edataConfig.getDianh());
		this.math = edataConfig.getMath();
		this.dianhCalculator = dianhCalculator;
	}


	@Override
	public Double calcGenerateElectric() throws Exception {
		String math = parseMath();
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine jsEngine = engineManager.getEngineByName("js");
		return (Double) jsEngine.eval(math);
	}
	
	/**
	 * 解析公式
	 * @return
	 */
	private String parseMath() {
		String math = this.math.replaceAll("\\{factor\\}", this.factor);
		for(int i=0;i<dianhs.length;i++) {
			Double dianhz = getValue(dianhs[i]);
			math = math.replaceAll("\\{dianh\\("+dianhs[i]+"\\)\\}", String.valueOf(dianhz));
		}
		return math;
	}
	
	private Double getValue(String dianh) {
		return dianhCalculator.calcDianhValue(dianh);
	}


	private String[] dianhArray(String dianStr) {
		return ObjectUtils.defaultIfNull(dianStr, "").split(",");
	}
	
	public static void main(String[] args) throws Exception {
		EdataConfigForStation config = new EdataConfigForStation();
		config.setDiancm("阿特斯高科光伏电厂");
		config.setDianh("0,2");
		config.setFactor("1000");
		config.setMath("{factor}*({dianh(0)}+{dianh(2)}*2)");
		
		Double ret = new ElectricCalculatorImpl(config,new DayDianhCalculator(config)).calcGenerateElectric();
		System.out.println(ret);
	}


	public DayDianhCalculator getDianhCalculator() {
		return (DayDianhCalculator) dianhCalculator;
	}


	public String getMath() {
		return math;
	}
	
	
}
