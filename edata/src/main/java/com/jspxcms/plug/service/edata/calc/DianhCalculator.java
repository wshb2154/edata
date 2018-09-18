package com.jspxcms.plug.service.edata.calc;

import java.util.Date;

public interface DianhCalculator {

	Double calcDianhValue(String dianh);

	Double calcDianhValue(String dianh, Date date);

}
