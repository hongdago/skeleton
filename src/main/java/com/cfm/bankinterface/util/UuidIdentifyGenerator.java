package com.cfm.bankinterface.util;

import java.util.UUID;

public class UuidIdentifyGenerator implements GenerateIdentify {

	@Override
	public String generateIdentify() {
		String identify = UUID.randomUUID().toString();
		return identify.replace("-","");
	}

}
