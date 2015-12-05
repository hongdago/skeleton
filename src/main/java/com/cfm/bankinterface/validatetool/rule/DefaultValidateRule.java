package com.cfm.bankinterface.validatetool.rule;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;

public class DefaultValidateRule {

	public static boolean validateRequired(Object bean, Field field) {
		Object component = null;
		boolean result =true;
		try{
			component = PropertyUtils.getProperty(bean, field.getProperty());
			if(component.getClass().isArray()){
				int component_length = Array.getLength(component);
				Class component_Class = component.getClass().getComponentType();
				String propertyName = field.getVarValue("propertyName");
				PropertyDescriptor pd = new PropertyDescriptor(propertyName, component_Class); 
				for (int i = 0 ; i<component_length; i++){
					Object item = Array.get(component, i);
					Method method = pd.getReadMethod();
					String item_value = (String)method.invoke(item);
					if(GenericValidator.isBlankOrNull(item_value)){
						result = false;
					}
				}
				
			}else{
				String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
				return !GenericValidator.isBlankOrNull(value);
			}
		}catch(Exception e){
			e.printStackTrace();
			result=false;
		}
		return result;
		
	}

	public static boolean validateData(Object bean, Field field) {
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		String pattern = field.getVarValue("datePattern");
		return GenericValidator.isDate(value, pattern, false);
	}

	public static boolean validateDateAfter(Object bean, Field field) {
		boolean result = false;
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		String compareField_name = field.getVarValue("compareDate");
		String pattern = field.getVarValue("datePattern");
		String compareField_value = ValidatorUtils.getValueAsString(bean, compareField_name);

		try {
			DateFormat df = new SimpleDateFormat(pattern);
			Date sourceDate = df.parse(value);
			Date compareDate = df.parse(compareField_value);
			result = sourceDate.compareTo(compareDate) >= 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
