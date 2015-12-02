package com.cfm.bankinterface;

import java.util.HashMap;

/**
 * 报文解析工具组
 * @author admin
 *
 */
public class ParseGroup {
	private static HashMap<String,ParseTool> prGroup;
	
	private static ParseGroup instance ;
	
	private ParseGroup(){
		this.prGroup = new HashMap<String,ParseTool>();
	}
	
	
	public static ParseGroup getInstance(){
		if(instance == null){
			instance = new ParseGroup();
		}
		return instance;
	}


	private HashMap<String, ParseTool> getPrGroup() {
		return prGroup;
	}


	private void setPrGroup(HashMap<String, ParseTool> prGroup) {
		this.prGroup = prGroup;
	}
	
	private ParseTool getParser(String bankType,String operType){
		String key = bankType+"_"+operType;
		HashMap<String,ParseTool> map = this.instance.getPrGroup();
		if(!map.containsKey(key)){
			ParseTool tool = new ParseTool(bankType, operType);
			map.put(key, tool);
		}
		return map.get(key);
	}
	
	public void parser(ResponseObj obj, String message){
		ParseTool tool = getParser(obj.getBankType(), obj.operType);
		tool.parse(obj, message);

	}
	
}
