package com.cfm.bankinterface;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.velocity.VelocityContext;

import com.cfm.bankinterface.exception.ValidateException;
import com.cfm.bankinterface.util.FrameworkProperties;
import com.cfm.bankinterface.util.PrintUtil;
import com.cfm.bankinterface.validatetool.DefaultValidatorGroup;



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
	 * 请求对象编码
	 */
	protected String encoding = "GBK";
	
	/**
	 * 构造方法
	 */
	public RequestObj(){
		this.bankType=getBankType();
		this.operType=getOperType();
	}
	
	
	/**
	 * 根据类名获取具体银行类型，子类名称采用"银行类型_操作类型"命名
	 * @return
	 */
	public  String getBankType(){
		String cls_name = this.getClass().getSimpleName();
		int split_index= cls_name.indexOf("_");
		if(split_index > 0){
			return cls_name.substring(0, split_index);
		}
		return cls_name;
		
	}
	
	/**
	 * 根据类名获取具体操作，子类名称采用"银行类型_操作类型"命名
	 * @return
	 */
	public String getOperType(){
		String cls_name = this.getClass().getSimpleName();
		int split_index= cls_name.indexOf("_");
		if(split_index > 0){
			return cls_name.substring(split_index+1);
		}
		return cls_name;
	}
	
	protected String getEncoding() {
		return encoding;
	}

	protected void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取生成XML的模版路径
	 * @return
	 */
	protected  String getTemplatePath(){
		return bankType+File.separator+bankType+"_"+operType+".vm";
	}
	
	
	/**
	 * 在上下文中设置框架属性值
	 * @param ctx
	 */
	private void setDefaultValue(VelocityContext ctx){
		ctx.put("BANK", bankType);
		ctx.put("system", FrameworkProperties.getInstance());
	}
	
	protected  boolean getvalidate() throws ValidateException{
		/*ValidatorGroup toolGroup = ValidatorGroup.getInstance();
		return toolGroup.getValidatorResult(this);*/
		DefaultValidatorGroup group = DefaultValidatorGroup.getInstance();
		return group.getValidatorResult(this);
		
	} 
	
	


	@Override
	public String toString() {
		return PrintUtil.objToString(this);
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
				field.setAccessible(true);
				ctx.put(field.getName(), field.get(this));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 * @throws ValidateException 
	 */
	public  String gerneateXML() throws Exception {
		String result = "";
		try{
			if(getvalidate()){
				XMLGenerator gen = new XMLGenerator(this);
				result = gen.generateXML();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	

	
	
	/**
	 * 
	 * @return
	 */
	public abstract String post();
	
	

}
