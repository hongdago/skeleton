package com.cfm.bankinterface.validatorrule;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;

public class DefaultValidateRule {

	public static boolean validateRequired(Object bean, Field field) {
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		return !GenericValidator.isBlankOrNull(value);
	}
	
	public static boolean validateData(Object bean, Field field,String pattern){
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		return GenericValidator.isDate(value, pattern, false);
	}

}
