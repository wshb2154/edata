package com.jspxcms.plug.service.edata;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.jspxcms.plug.service.edata.inter.EdataTask;
import com.jspxcms.plug.sql.SQL_EDATA;

public class TaskManager {
	private Logger logger = Logger.getLogger(TaskManager.class);
	/**
	 * 开始导入电厂数据
	 * @deprecated 导入文件和更新实时的日发电量是同时进行的，请使用startImportAndUpdateForRealtime
	 */
	@Deprecated
	public void startEdataImport() {
		new Timer().schedule(new TimerTask() {
			private EdataTask task = new EdataImportTask();
			@Override
			public void run() {
				task.execute();
			}
		},0, EdataImportTask.period);
	}
	
	/**
	 * 同时开启文件导入和更新实时发电量
	 */
	public void startImportAndUpdateForRealtime() {
		new Timer().schedule(new TimerTask() {
			private EdataTask iTask = new EdataImportTask();
			private EdataTask uTask = new EdataUpdateRealtimeTask();
			@Override
			public void run() {
				iTask.execute();
				if(isStartUpdate()) {
					uTask.execute();
				}
			}
			/**
			 * 调试时期的临时方法处理
			 */
			private boolean isStartUpdate() {
				return "1".equals(CacheHelper.getFileConfig().getProperty("startUpdate","0"));
			}
		},0, EdataImportTask.period);
	}
	
	public boolean isStart() {
		return "1".equals(CacheHelper.getFileConfig().getProperty("startEdataTask","0"));
	}
	
	public static void dbLog(String msg,String type) {
		CacheHelper.getSqlService().executeSql(SQL_EDATA.LOG_SQL, msg,type);
	}

}
