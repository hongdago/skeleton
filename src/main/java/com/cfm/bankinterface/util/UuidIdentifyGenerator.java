package com.cfm.bankinterface.util;

import java.util.UUID;

public class UuidIdentifyGenerator implements GenerateIdentify {

	@Override
	public String generateIdentify() {
		String identify = UUID.randomUUID().toString();
		return identify.replace("-","");
	}
	
	
	public static void main(String[] args){
		
		UuidIdentifyGenerator u1 = new UuidIdentifyGenerator();
		UuidIdentifyGenerator u2 = new UuidIdentifyGenerator();
		
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		System.out.println(u1.generateIdentify()+"  "+u2.generateIdentify());
		
	}


}
