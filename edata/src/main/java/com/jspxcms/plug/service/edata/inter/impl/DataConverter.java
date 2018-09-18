package com.jspxcms.plug.service.edata.inter.impl;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jspxcms.core.service.SqlServiceA;
import com.jspxcms.plug.service.edata.CacheHelper;
import com.jspxcms.plug.service.edata.entity.EdataFile;
import com.jspxcms.plug.service.edata.entity.EdataFileData;
import com.jspxcms.plug.service.edata.inter.IDataConverter;

public class DataConverter implements IDataConverter {

	private final String EDATA_TABLE = "bd_powerstation_edata";
	private final String[] EDATA_COLUMNS = new String[] {"changm","dianh","dianhmc","dianhz","zhilbz","diancjc","filesj","lursj"};
	private final String EDATA_SQL_ADD; //要执行的sql语句
	private SqlServiceA sqlService = CacheHelper.getSqlService();

	public DataConverter() {
		String[] EDATA_PARAMS = new String[EDATA_COLUMNS.length];
		Arrays.fill(EDATA_PARAMS, "?");
		EDATA_SQL_ADD = format("insert into %s (%s) values (%s)",
				EDATA_TABLE,
				StringUtils.join(EDATA_COLUMNS, ","),
				StringUtils.join(EDATA_PARAMS,","));
	}

	@Override
	public void batchConvertEFiledataToDBdata(List<EdataFile> efiles) {
		for(EdataFile efile:efiles) {
			convertEFiledataToDBdata(efile);
		}
	}

	@Override
	public void convertEFiledataToDBdata(EdataFile efile) {
		if(EdataFile.ERROR_PARSE == efile.getErrorType()) return;
		try {
			String changm, dianh, dianhmc="", zhilbz, diancjc;
			double dianhz;
			Date filesj, lursj;
			diancjc = efile.getDiancjc();
			filesj = DateUtils.parseDate(efile.getFilesj(), "yyyyMMddHHmmss","yyyyMMddHHmm");
			lursj = new Date();
			List<Object[]> bathParams = new ArrayList<Object[]>();
			for(EdataFileData edata:efile.getEdatas()) {
				changm = edata.getChangm();
				dianh = edata.getDianh();
				zhilbz = edata.getZhilbz();
				dianhz = ObjectUtils.defaultIfNull(Double.parseDouble(edata.getDianhz()), 0.0);
				bathParams.add(new Object[] {changm,dianh,dianhmc,dianhz,zhilbz,diancjc,filesj,lursj});
			}
			sqlService.executeBatchSql(EDATA_SQL_ADD,bathParams);
			efile.setSuccess(true);
		} catch (Exception e) {
			efile.setErrorType(EdataFile.ERROR_DB);
			e.printStackTrace();
		} 
		
	}
	
	

}
