package com.jspxcms.plug.service.edata;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TomcatListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TaskManager taskManager = new TaskManager();
		if (taskManager.isStart()) {
			taskManager.startImportAndUpdateForRealtime();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
