package com.cfm.bankinterface.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import com.cfm.bankinterface.util.PrintUtil;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

public class ConfigInfo {

	/**
	 * 默认的解析文件
	 */
	private static final String VALIDATOR_RULES = "digester-rules.xml";

	/**
	 * 连接信息
	 */
	private HashMap<String,LinkInfo> linkInfos = new HashMap<String,LinkInfo>();

	/**
	 * 例外的连接信息列表
	 */
	private HashMap<String,String> excludLinkInfos;

	/**
	 * 扩展信息
	 */
	private HashMap<String,String> extraInfo;
	
	public ConfigInfo(InputStream in) throws IOException, SAXException{
		Digester digester = initDigester();
		digester.push(this);
		digester.parse(in);
	}

	public HashMap<String, LinkInfo> getLinkInfos() {
		return linkInfos;
	}

	public HashMap<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(HashMap<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	public HashMap<String, String> getExcludLinkInfos() {
		return excludLinkInfos;
	}

	public void setExcludLinkInfos(HashMap<String, String> excludLinkInfos) {
		this.excludLinkInfos = excludLinkInfos;
	}

	public  void addLinkInfo(LinkInfo info){
		this.linkInfos.put(info.getName(),info);
	}
	
	private Digester initDigester() {
		URL rulesUrl = this.getClass().getResource(VALIDATOR_RULES);
		if (rulesUrl == null) {
			rulesUrl = ConfigInfo.class.getResource(VALIDATOR_RULES);
		}
		Digester digester = DigesterLoader.createDigester(rulesUrl);
		return digester;
	}

	public static void main(String[] args) throws  Exception {
		File file = new File("/home/hongda/IdeaProjects/skeleton/src/main/resources/test.xml");
		ConfigInfo info = new ConfigInfo(new FileInputStream(file));
		System.out.println(PrintUtil.objToString(info));
	}
}
