package com.cfm.bankinterface.validatetool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.xml.sax.SAXException;

import com.cfm.bankinterface.RequestObj;
import com.cfm.bankinterface.exception.ValidateException;


public class DefaultValidatorGroup {
	
	private static HashMap<String,Validator> BANKVALIDATOR = new HashMap<String,Validator>();
	
	private static final String GLOBALVLD_FILE_NAME = "BASE_Req_vld.xml";
	
	private static final String SUFFIX = "vld";
	
	private static ValidatorResources GLOBALRESOURCE ;
	
	private static final String PROPERTIES = "VDResources";
	
	private ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES);
	
	private static DefaultValidatorGroup INSTANCE ;
	
	static{
		InputStream in = DefaultValidatorGroup.class.getResourceAsStream(GLOBALVLD_FILE_NAME);
		if(in != null){
			try {
				GLOBALRESOURCE=new ValidatorResources(in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}else{
			//TODO
			System.out.println(GLOBALVLD_FILE_NAME+" 不存在");
		}
		
		
	}
	
	private DefaultValidatorGroup(){
		
	}
	
	
	private Validator getValidator(RequestObj obj){
		String key=obj.getBankType()+"_"+obj.getOperType();
		if(!BANKVALIDATOR.containsKey(key)){
			loadNewResource(obj);
			Validator validator = new Validator(GLOBALRESOURCE, obj.getClass().getSimpleName());
			BANKVALIDATOR.put(key, validator);
		}
		return BANKVALIDATOR.get(key);
	}
	
	private void loadNewResource(RequestObj obj) {
		String resourceName = obj.getBankType() + File.separator+obj.getBankType()+"_"+obj.getOperType()+"_"+SUFFIX+".xml";
		InputStream in = obj.getClass().getClassLoader().getResourceAsStream(resourceName);
		DefaultValidatorResource newResource = null;
		if(in != null){
			try{
				newResource  = new DefaultValidatorResource(in);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			//TODO
			System.out.println(resourceName+" 不存在");
		}
		if(newResource != null){
			GLOBALRESOURCE.addFormSet(newResource.getDefaultFormSet());
		}
	}

	private ValidatorResults validate(RequestObj obj){
		ValidatorResults results = null;
		Validator validator= getValidator(obj);
		if(validator != null){
			validator.setParameter(Validator.BEAN_PARAM, obj);
			try {
				validator.setOnlyReturnErrors(true);
				results = validator.validate();			
			} catch (ValidatorException e) {
				e.printStackTrace();
			}
		}else{
			
		}
		return results;
	}
	
	private Object getErrmsg(String actName, Field field) {
		ValidatorAction action = GLOBALRESOURCE.getValidatorAction(actName);
		String message = bundle.getString(action.getMsg());
		List<String> infos = new ArrayList<String>();
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
	
	public static DefaultValidatorGroup getInstance(){
		if(INSTANCE==null){
			INSTANCE = new DefaultValidatorGroup();
		}
		return INSTANCE;
	}

	public boolean getValidatorResult(RequestObj obj) throws ValidateException{
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

	
}
