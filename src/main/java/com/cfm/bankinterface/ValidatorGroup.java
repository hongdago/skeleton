package com.cfm.bankinterface;

import java.util.HashMap;

/**
 * XML报文生成字段校验工具
 * @author admin
 *
 */
public class ValidatorGroup {
	
	private HashMap<String,ValidatorTools> vdGroup;
	
	private static ValidatorGroup instance ;
	
	private ValidatorGroup(){
		this.vdGroup = new HashMap<String,ValidatorTools>();
	}
	
	public static ValidatorGroup getInstance(){
		if(instance == null){
			instance = new ValidatorGroup();
		}
		return instance;
	}
	
		
	public HashMap<String, ValidatorTools> getVdGroup() {
		return vdGroup;
	}

	public void setVdGroup(HashMap<String, ValidatorTools> vdGroup) {
		this.vdGroup = vdGroup;
	}

	ValidatorTools getValidator(String bankType,String operType){
		String key = bankType+"_"+operType;
		HashMap<String,ValidatorTools> map = this.instance.getVdGroup();
		if(!map.containsKey(key)){
			ValidatorTools tool = new ValidatorTools(bankType, operType);
			map.put(key, tool);
		}
		return map.get(key);
	}
	
	
}