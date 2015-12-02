package com.cfm.bankinterface;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

public class ParseTool {
	
	/**
	 * 银行类型
	 */
	private String bankType;
	
	/**
	 * 操作类型
	 */
	private String operType;
	
	/**
	 * 解析器
	 */
	private Digester digester ;
	
	
	/**
	 * 规则文件后缀
	 */
	private static final String SUFFIX = "rule";
	
	
	ParseTool(String bankType , String operType){
		this.bankType = bankType;
		this.operType = operType;
		init();
	}
	
	private void init(){
		String rootPath = this.getClass().getClassLoader().getResource("").getPath();
		final File file = new File(rootPath+File.separator+bankType+File.separator+bankType+"_"+operType+"_"+SUFFIX+".xml");
		digester = DigesterLoader.newLoader( new FromXmlRulesModule()
        {
            @Override
            protected void loadRules()
            {
            	loadXMLRules(file);
            }

        } ).newDigester();
	}
	
	void parse(ResponseObj obj,String message){
		this.digester.push(obj);
		try {
			this.digester.parse(new ByteArrayInputStream(message.getBytes(obj.getEncoding())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
