package com.cfm.bankinterface.sign;

import com.cfm.bankinterface.RequestObj;

import sun.misc.BASE64Encoder;

public class DefaultSign implements SignTool {

	@Override
	public String processSign(RequestObj obj) {
		String result = "";
		byte[] b = null;
		try {
			String sourceStr = obj.gerneateXML();
			b = sourceStr.getBytes(obj.getEncoding());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (b != null) {
			result = new BASE64Encoder().encode(b);
		}
		return result;
	}

}
