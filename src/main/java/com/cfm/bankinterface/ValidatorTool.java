package com.cfm.bankinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;

import com.cfm.bankinterface.exception.ValidateException;

class ValidatorTool {
	/**
	 * 资源文件名称
	 */
	private static final String PROPERTIES = "VDResources";
	
	/**
	 * 校验文件后缀
	 */
	private static final String SUFFIX = "vld";
	
	
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
	
	ValidatorTool(String bankType , String operType){
		this.bankType = bankType;
		this.operType = operType;
		init();
	}
	
	private void init(){
		InputStream in = null;
		try {
			String rootPath = this.getClass().getClassLoader().getResource("").getPath();
			File ruleFile = new File(rootPath+File.separator + bankType + File.separator+bankType+"_"+operType+"_"+SUFFIX+".xml");
			if(ruleFile.exists()){
				in = new FileInputStream(ruleFile);
				validatorResources = new ValidatorResources(in);
			}else{
				
			}
			
			bundle = ResourceBundle.getBundle(this.bankType+"/"+PROPERTIES);
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
	
	boolean getValidatorResult(RequestObj obj) throws ValidateException{
		boolean success = true;
		ValidatorResults results = validate(obj);
		if(results != null &&!results.isEmpty()){
			success=false;
			StringBuffer errmsg = new StringBuffer();
			Iterator<String> iter = results.getPropertyNames().iterator();
			while(iter.hasNext()){
				String propertyName =iter.next();
				ValidatorResult result = results.getValidatorResult(propertyName);
				Field field = result.getField();
				Iterator<String> keys = result.getActions();
				while (keys.hasNext()) {
					String actName = keys.next();
					errmsg.append(getErrmsg(actName, field));
				}
			}
			throw new ValidateException(errmsg.toString());
		}
		return success;
	}
	
	
	private String getErrmsg(String actName, Field field) {
		ValidatorAction action = validatorResources.getValidatorAction(actName);
		String message = bundle.getString(action.getMsg());
		List infos = new ArrayList();
		Pattern pattern = Pattern.compile("\\{[0-3]\\}");  
		Matcher matcher = pattern.matcher(message);   
		while(matcher.find()){
			String index=matcher.group();
        	index=index.substring(1, index.length()-1);
        	Arg arg = field.getArg(field.getDepends(),Integer.valueOf(index));
        	
        	infos.add(bundle.getString(arg.getKey()));
		}
		return MessageFormat.format(message, infos.toArray());
		
		
		
		
		
	}

	private ValidatorResults validate(RequestObj obj){
		ValidatorResults results = null;
		if(validatorResources != null){
			Validator validator = new Validator(validatorResources, obj.getClass().getSimpleName());
			validator.setParameter(Validator.BEAN_PARAM, obj);
			try {
				validator.setOnlyReturnErrors(true);
				results = validator.validate();			
			} catch (ValidatorException e) {
				e.printStackTrace();
			}
		}else
		{
			//TODO
			System.out.println("未配置["+bankType+"]:["+operType+"]的规则文件！");
		}
		
		return results;
	}	

}
