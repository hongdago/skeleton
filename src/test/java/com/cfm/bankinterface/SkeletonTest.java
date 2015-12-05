package com.cfm.bankinterface;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.cfm.bankinterface.util.AppTools;
import com.cfm.bankinterface.util.FrameworkProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SkeletonTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAppToole() {
		assertEquals(AppTools.getDataTimeStr("yyyyMMdd"), getCurDate());
	}
	
	@Test
	public void testFrameworkProperties(){
		FrameworkProperties pro = FrameworkProperties.getInstance();
		/*/String seqno = pro.getSeqno();
		assertEquals(pro.getTranDate(), "20151128");
		assertEquals(seqno.length(), 32);*/
	}

	private String getCurDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}

}
