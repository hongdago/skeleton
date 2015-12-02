package com.cfm.bankinterface.util;

/**
 * 
 * @author admin
 *
 */
public class FrameworkProperties {

	/**
	 * 序列号
	 */
	private String seqno;

	/**
	 * 交互日期
	 */
	private String tranDate;

	/**
	 * 交互时间
	 */
	private String tranTime;

	private GenerateIdentify generator;
	private static FrameworkProperties instance = null;

	private FrameworkProperties() {
		this.generator = new UuidIdentifyGenerator();
	}

	public GenerateIdentify getGenerator() {
		return generator;
	}

	public void setGenerator(GenerateIdentify generator) {
		this.generator = generator;
	}

	public static FrameworkProperties getInstance() {
		if (instance == null) {
			instance = new FrameworkProperties();
		}
		return instance;
	}

	public String getSeqno() {
		return generator.generateIdentify();
	}

	public String getTranDate() {
		return AppTools.getDataTimeStr("yyyyMMdd");
	}

	public String getTranTime() {
		return AppTools.getDataTimeStr("HHmmssSSS");
	}

}
