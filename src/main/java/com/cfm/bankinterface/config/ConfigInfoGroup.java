package com.cfm.bankinterface.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xml.sax.SAXException;

import com.cfm.bankinterface.RequestObj;

public class ConfigInfoGroup {
	private static String CONFIGGILESUFIX="Cfg.xml";
	
	private static ConfigInfoGroup INSTANCE;

	/**
	 * 配置信息字典，key值为银行类型，value值为银行对用的配置信息
	 */
	private HashMap<String,ConfigInfo> configInfos= new HashMap<String,ConfigInfo>();
	
	private ConfigInfoGroup(){
		
	}
	
	public static ConfigInfoGroup getInstance(){
		if(INSTANCE ==null){
			INSTANCE = new  ConfigInfoGroup();
		}
		return INSTANCE;
	}
	
	public ConfigInfo getConfigInfo(RequestObj obj){
		String bankType = obj.getBankType();
		if(!configInfos.containsKey(bankType))
		{
			String resourceName = obj.getBankType()+File.separator+obj.getBankType()+"_"+CONFIGGILESUFIX;
			InputStream input = obj.getClass().getClassLoader().getResourceAsStream(resourceName);
			if(input != null){
				try {
					ConfigInfo info = new ConfigInfo(input);
					configInfos.put(bankType, info);
				} catch (IOException e) {
					e.printStackTrace();
					//TODO
					System.out.println("读取文件内容失败");
				} catch (SAXException e) {
					// TODO 
					e.printStackTrace();
					System.out.println("解析文件失败");
				}
			}else{
				//TODO
				System.out.println(resourceName+" 不存在！");
			}
		}
		return configInfos.get(bankType);
	}
}
