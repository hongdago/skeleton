package com.cfm.bankinterface;

import com.cfm.bankinterface.util.PrintUtil;

public abstract class ResponseObj {
	
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
	public ResponseObj(){
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
	

	@Override
	public String toString() {
		return PrintUtil.objToString(this);
	}
	
	
	
}
