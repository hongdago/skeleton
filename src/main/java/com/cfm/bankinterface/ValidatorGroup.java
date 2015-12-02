package com.cfm.bankinterface;

import java.util.HashMap;

/**
 * XML报文生成字段校验工具
 * @author admin
 *
 */
public class ValidatorGroup {
	
	private HashMap<String,ValidatorTool> vdGroup;
	
	private static ValidatorGroup instance ;
	
	private ValidatorGroup(){
		this.vdGroup = new HashMap<String,ValidatorTool>();
	}
	
	public static ValidatorGroup getInstance(){
		if(instance == null){
			instance = new ValidatorGroup();
		}
		return instance;
	}
	
		
	private HashMap<String, ValidatorTool> getVdGroup() {
		return vdGroup;
	}

	private void setVdGroup(HashMap<String, ValidatorTool> vdGroup) {
		this.vdGroup = vdGroup;
	}

	ValidatorTool getValidator(String bankType,String operType){
		String key = bankType+"_"+operType;
		HashMap<String,ValidatorTool> map = this.instance.getVdGroup();
		if(!map.containsKey(key)){
			ValidatorTool tool = new ValidatorTool(bankType, operType);
			map.put(key, tool);
		}
		return map.get(key);
	}
	
	
}