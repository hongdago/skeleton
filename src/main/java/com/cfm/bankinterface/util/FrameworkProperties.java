package com.cfm.bankinterface.util;

/**
 * 
 * @author admin
 *
 */
public class FrameworkProperties {

	private static FrameworkProperties instance = null;

	private FrameworkProperties() {}



	public static FrameworkProperties getInstance() {
		if (instance == null) {
			instance = new FrameworkProperties();
		}
		return instance;
	}


	public String getTranDate() {
		return AppTools.getDataTimeStr("yyyyMMdd");
	}

	public String getTranTime() {
		return AppTools.getDataTimeStr("HHmmssSSS");
	}

}
