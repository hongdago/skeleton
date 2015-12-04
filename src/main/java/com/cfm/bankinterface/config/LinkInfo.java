package com.cfm.bankinterface.config;

public class LinkInfo {
	
	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 端口
	 */
	private String port;

	/**
	 * 交互协议
	 */
	private String schema;
	
	/**
	 * 请求URL
	 */
	private String reqURL;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getReqURL() {
		return reqURL;
	}

	public void setReqURL(String reqURL) {
		this.reqURL = reqURL;
	}

}
