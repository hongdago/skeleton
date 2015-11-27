package com.cfm.bankinterface;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


public class XMLGenerator {
	
	private final static VelocityEngine ve;
	private RequestObj obj ;
	
	static {
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
	}
	
	public XMLGenerator(RequestObj obj){
		this.obj=obj;
	}
	
	public String generateXML(){
		VelocityContext ctx = new VelocityContext();
		StringWriter writer = new StringWriter();
		Template template = ve.getTemplate(obj.getTemplatePath());
		obj.setValue(ctx);
		template.merge(ctx, writer);
		return writer.toString();
	}
}
