package com.cfm.bankinterface.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

public class ConfigInfo {

	private static final String VALIDATOR_RULES = "digester-rules.xml";

	
	private HashMap<String,LinkInfo> connInfo = new HashMap<String,LinkInfo>();

	/**
	 * 扩展信息
	 */
	private HashMap<String,String> extraInfo;
	
	public ConfigInfo(InputStream in) throws IOException, SAXException{
		Digester digester = initDigester();
		digester.push(this);
		digester.parse(in);
	}
	

	public HashMap<String, LinkInfo> getConnInfo() {
		return connInfo;
	}

	public void setConnInfo(HashMap<String, LinkInfo> connInfo) {
		this.connInfo = connInfo;
	}


	public HashMap<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(HashMap<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	private Digester initDigester() {
		URL rulesUrl = this.getClass().getResource(VALIDATOR_RULES);
		if (rulesUrl == null) {
			rulesUrl = ConfigInfo.class.getResource(VALIDATOR_RULES);
		}
		Digester digester = DigesterLoader.createDigester(rulesUrl);
		return digester;
	}
}
