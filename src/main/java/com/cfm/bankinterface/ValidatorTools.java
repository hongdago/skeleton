package com.cfm.bankinterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;

class ValidatorTools {
	/**
	 * 资源文件名称
	 */
	private static final String PROPERTIES = "VDResources";
	
	/**
	 * 校验文件目录
	 */
	private static final String VDPREFIX = "validate";
	
	
	/**
	 * 银行类型
	 */
	private String bankType;
	
	/**
	 * 操作类型
	 */
	private String operType;
	
	private ValidatorResources validatorResources;
	private ResourceBundle bundle;
	
	ValidatorTools(String bankType , String operType){
		this.bankType = bankType;
		this.operType = operType;
		init();
	}
	
	private void init(){
		InputStream in = null;
		try {
			in = new FileInputStream("src/main/resources/" + bankType + "/"+VDPREFIX+ "/"+operType+".xml");
			validatorResources = new ValidatorResources(in);
			bundle = ResourceBundle.getBundle(this.bankType+"/"+VDPREFIX+"/"+PROPERTIES);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	boolean getValidatorResult(RequestObj obj){
		boolean success = true;
		ValidatorResults results = validate(obj);
		if(!results.isEmpty()){
			StringBuffer errmsg = new StringBuffer();
			Iterator<String> iter = results.getPropertyNames().iterator();
			while(iter.hasNext()){
				String propertyName =iter.next();
				ValidatorResult result = results.getValidatorResult(propertyName);
				Iterator<String> keys = result.getActions();
				while (keys.hasNext()) {
					String actName = keys.next();
					ValidatorAction action = validatorResources.getValidatorAction(actName);
					String message = bundle.getString(action.getMsg());
					errmsg.append(MessageFormat.format(message, new Object[]{propertyName}));
				}
			}
			System.out.println(errmsg);
		}
		return success;
	}
	
	
	private ValidatorResults validate(RequestObj obj){
		ValidatorResults results = null;
		Validator validator = new Validator(validatorResources, obj.getClass().getSimpleName());
		validator.setParameter(Validator.BEAN_PARAM, obj);
		try {
			validator.setOnlyReturnErrors(true);
			results = validator.validate();			
		} catch (ValidatorException e) {
			e.printStackTrace();
		}
		return results;
	}	

}
