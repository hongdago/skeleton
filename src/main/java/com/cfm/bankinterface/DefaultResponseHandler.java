package com.cfm.bankinterface;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import com.cfm.bankinterface.parse.ParseGroup;

public class DefaultResponseHandler implements ResponseHandler<ResponseObj> {
	private RequestObj requestobj ;
	
	public DefaultResponseHandler( RequestObj requestobj){
		this.requestobj=requestobj;
	}

	@Override
	public ResponseObj handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(
                    statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }       
		return parse(entity);
	}
	
	
	private ResponseObj parse(HttpEntity entity){
		String res_name = requestobj.getClass().getName().replace("_Req", "_Res");
		ResponseObj obj = null;
		try {
			obj = (ResponseObj)Class.forName(res_name).newInstance();
			ParseGroup group = ParseGroup.getInstance();
			group.parser(obj, entity.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
