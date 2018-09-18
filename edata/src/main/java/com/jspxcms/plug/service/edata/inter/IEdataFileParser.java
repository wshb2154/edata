package com.jspxcms.plug.service.edata.inter;

import java.io.File;

import com.jspxcms.plug.service.edata.entity.EdataFile;

public interface IEdataFileParser {

	EdataFile parse(File file);

}