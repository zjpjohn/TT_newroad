package com.newroad.util.xml;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtil {
	
    private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	
	private XmlUtil(){
	}

	public static Document parseFromString(String str) {
		Document doc = null;
		if(str == null) {
			return null;
		}
		try {
			return DocumentHelper.parseText(str);
		} catch (Exception e) {
		    logger.error("parse xml doc exception :", e);
		    logger.error("xml string is :"+str);
		}
		return doc;
	}
}
