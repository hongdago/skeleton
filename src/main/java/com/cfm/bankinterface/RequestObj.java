package com.cfm.bankinterface;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.validator.ValidatorResults;
import org.apache.velocity.VelocityContext;

import com.cfm.bankinterface.util.FrameworkProperties;



public abstract class RequestObj {
	/**
	 * 银行类型
	 */
	protected String bankType; 

	/**
	 * 操作类型
	 */
	protected String operType; 
	
	/**
	 * 根据类名获取具体银行类型，子类名称采用"银行类型_操作类型"命名
	 * @return
	 */
	public  String getBankType(){
		String cls_name = this.getClass().getSimpleName();
		String[] names = cls_name.split("_");
		return names.length == 2?names[0]:"";
		
	}
	
	/**
	 * 根据类名获取具体操作，子类名称采用"银行类型_操作类型"命名
	 * @return
	 */
	public String getOperType(){
		String cls_name = this.getClass().getSimpleName();
		String[] names = cls_name.split("_");
		return names.length == 2?names[1]:"";
	}
	
	
	/**
	 * 构造方法
	 */
	public RequestObj(){
		this.bankType=getBankType();
		this.operType=getOperType();
	}
	
	
	/**
	 * 获取生成XML的模版路径
	 * @return
	 */
	protected  String getTemplatePath(){
		return bankType+File.separator+operType+".vm";
	}
	
	
	/**
	 * 在上下文中设置框架属性值
	 * @param ctx
	 */
	private void setDefaultValue(VelocityContext ctx){
		ctx.put("BANK", bankType);
		ctx.put("system", FrameworkProperties.getInstance());
	}
	
	protected  boolean getvalidate(){
		ValidatorGroup toolGroup = ValidatorGroup.getInstance();
		ValidatorTools validator = toolGroup.getValidator(bankType, operType);
		return validator.getValidatorResult(this);
	} 


	/**
	 * 
	 * @param ctx
	 */
	public void setValue(VelocityContext ctx) {
		setDefaultValue(ctx);
		Class cl = this.getClass();
		Field[] fields = cl.getDeclaredFields();
		try{
			for(Field field:fields){
				Class fieldClass = field.getType();
				if(fieldClass.getName().startsWith("java.lang") || fieldClass.getName().startsWith("java.util")
						||fieldClass.getName().startsWith("[L"+cl.getName())){
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cl);  
					Method method = pd.getReadMethod();
					ctx.put(field.getName(), method.invoke(this));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public  String gerneateXML(){
		String result = "";
		if(getvalidate()){
			XMLGenerator gen = new XMLGenerator(this);
			result = gen.generateXML();
		}
		return result;
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract ValidatorResults validate();
	
	/**
	 * 
	 * @return
	 */
	public abstract String post();
	
	

}
