package com.cfm.bankinterface;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.cfm.bankinterface.util.AppTools;
import com.cfm.bankinterface.util.FrameworkProperties;

public class SkeletonTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAppToole() {
		assertEquals(AppTools.getDataTimeStr("yyyyMMdd"), "20151201");
	}
	
	@Test
	public void testFrameworkProperties(){
		FrameworkProperties pro = FrameworkProperties.getInstance();
		String seqno = pro.getSeqno();
		assertEquals(pro.getTranDate(), "20151201");
		assertEquals(seqno.length(), 32);
	}

}
