package com.cfm.bankinterface.validatetool;

import java.io.InputStream;

import org.apache.commons.validator.FormSet;
import org.apache.commons.validator.ValidatorResources;

/**
 * 对apache ValidatorResources的扩展，使用动态加载银行规则
 * @author admin
 *
 */
public class DefaultValidatorResource extends ValidatorResources {

	
	DefaultValidatorResource(InputStream in) throws Exception{
		super(in);
	}
	
	
	
	public FormSet getDefaultFormSet(){
		return this.defaultFormSet;
	}
	
}
