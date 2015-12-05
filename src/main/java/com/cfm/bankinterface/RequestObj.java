package com.cfm.bankinterface;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.cfm.bankinterface.config.LinkInfo;
import org.apache.http.client.utils.URIBuilder;
import org.apache.velocity.VelocityContext;

import com.cfm.bankinterface.config.ConfigInfo;
import com.cfm.bankinterface.config.ConfigInfoGroup;
import com.cfm.bankinterface.exception.ValidateException;
import com.cfm.bankinterface.sign.DefaultSign;
import com.cfm.bankinterface.sign.SignTool;
import com.cfm.bankinterface.util.FrameworkProperties;
import com.cfm.bankinterface.util.GenerateIdentify;
import com.cfm.bankinterface.util.PrintUtil;
import com.cfm.bankinterface.util.UuidIdentifyGenerator;
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
	protected String encoding;
	
	/**
	 * 流水标识
	 */
	protected String seqno;
	
	/**
	 * 流水标识生成器
	 */
	protected GenerateIdentify identifyGer ;

	/**
	 * 签名接口
	 */
	protected SignTool signtool;

	/**
	 * 配置信息
	 */
	protected ConfigInfo configInfo;

	/**
	 * 是否签名标识
	 */
	protected boolean isSign;

	
	
	/**
	 * 构造方法
	 */
	public RequestObj(){
		this.bankType=getBankType();
		this.operType=getOperType();
		this.encoding="GBK";
		this.identifyGer = getIdentifyGer();
		this.signtool=getSign();
		this.seqno=identifyGer.generateIdentify();
		ConfigInfoGroup group = ConfigInfoGroup.getInstance();
		this.configInfo=group.getConfigInfo(this);

	}
	
	public String getSeqno() {
		return seqno;
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
	
	public String getEncoding() {
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
		ctx.put("system", FrameworkProperties.getInstance());
		if(configInfo != null){
			ctx.put("config", configInfo.getExtraInfo());
		}
	}
	
	/**
	 * 获取验证结果
	 * @return
	 * @throws ValidateException
	 */
	protected  boolean getvalidate() throws ValidateException{
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
		traversalValue(this.getClass(), ctx);
	}
	
	private void traversalValue(Class clazz,VelocityContext ctx){
		while(clazz != null ){
			Field[] fields = clazz.getDeclaredFields();
			try{
				for(Field field:fields){
					field.setAccessible(true);
					ctx.put(field.getName(), field.get(this));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			clazz=clazz.getSuperclass();
		}
	}
	
	
	
	/**
	 * 生成XML报文
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
	 * 获取签名串
	 * @return
     */
	public String getSignstr(){
		this.isSign=true;
		return signtool.processSign(this);
	}
	
	
	
	
	/**
	 * 可被子类覆盖，影响类的初始化
	 * @return
	 */
	protected GenerateIdentify getIdentifyGer(){
		return new UuidIdentifyGenerator();
	}
	
	
	/**
	 * 获取签名工具
	 * @return
	 */
	protected SignTool getSign(){
		return new DefaultSign();
	}

	/**
	 * 是否已签名
	 * @return
     */
	public boolean isSign() {
		return isSign;
	}


	public String getReqURL() throws IllegalAccessException, NoSuchFieldException, URISyntaxException {
		if(configInfo != null){
			HashMap<String, String> excluds = configInfo.getExcludLinkInfos();
			String key="default";
			String className = this.getClass().getSimpleName();
			if(excluds.containsKey(className)){
					key=excluds.get(className);
			}
			LinkInfo info = configInfo.getLinkInfos().get(key);
			return info.getRegURL(this);
		}
		return "";
	}

	/**
	 * 获取交易代码，如：工行查询当日余额：QACCBAL 招行查询当日余额：GetAccInfo
	 * @return
	 */
	public abstract String getTransCode();
	
}
