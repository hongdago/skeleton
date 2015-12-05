package com.cfm.bankinterface.config;

import com.cfm.bankinterface.RequestObj;
import com.cfm.bankinterface.util.AppTools;
import org.apache.http.client.utils.URIBuilder;
import org.w3c.dom.Entity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkInfo {
    private static final Pattern REQPARAM = Pattern.compile("\\{\\{REQ.(\\w+)\\}\\}");

    private static final Pattern CONFPARAM = Pattern.compile("\\{\\{(\\w+)\\}\\}");
    /**
     * name
     */
    private String name;

    /**
     * 交互协议
     */
    private String schema;

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 请求路径
     */
    private ReqURL reqURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ReqURL getReqURL() {
        return reqURL;
    }

    public void setReqURL(ReqURL reqURL) {
        this.reqURL = reqURL;
    }

    public String toString(){
        URIBuilder builder = new URIBuilder();
        builder.setScheme(schema);
        builder.setHost(host);
        if(port !=null && !"".equals(port)) {
            builder.setPort(Integer.parseInt(port));
        }
        if(reqURL !=null){
            if(reqURL.getPath() !=null && !"".equals(reqURL.getPath())){
                builder.setPath(reqURL.getPath());
            }
            if(reqURL.getParams() !=null){
                Iterator<Map.Entry<String,String>> iter = reqURL.getParams().entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry<String,String> entry = iter.next();
                    builder.setParameter(entry.getKey(),entry.getValue());
                }
            }
        }
        URI uri = null;
        try {
            uri = builder.build();
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return uri.toString();
    }

    /**
     * 根据RequestObj获取正规的URL地址
     * @return
     */
    public String getRegURL(RequestObj obj) throws NoSuchFieldException, IllegalAccessException, URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(schema);
        builder.setHost(host);
        if(port !=null && !"".equals(port)) {
            builder.setPort(Integer.parseInt(port));
        }
        if(reqURL !=null){
            if(reqURL.getPath() !=null && !"".equals(reqURL.getPath())){
                builder.setPath(reqURL.getPath());
            }
            if(reqURL.getParams() !=null){
                Iterator<Map.Entry<String,String>> iter = reqURL.getParams().entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry<String,String> entry = iter.next();
                    builder.setParameter(entry.getKey(),transform(entry.getValue(),obj));
                }
            }
        }
        URI uri = builder.build();
        return uri.toString();

    }

    private String transform(String value,RequestObj obj) throws NoSuchFieldException, IllegalAccessException {
        String result = value;
        if(value != null && !"".equals(value)){
            Matcher reqMatch = REQPARAM.matcher(value);
            if(reqMatch.matches()){
                String key = reqMatch.group(1);
                return AppTools.getValue(obj,key);

            }else{
                Matcher confMatch = CONFPARAM.matcher(value);
                if(confMatch.matches()){
                    String key = confMatch.group(1);
                    return AppTools.getValue(this,key);
                }
            }
        }
        return  result;
    }

    public static void main(String[] args){
        String[] strs = new String[]{"abcde","{{abcde}}","{{REQ.abcde}}"};
        Pattern pattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
        for (String str : strs){
            System.out.print(str+":");
            Matcher match = pattern.matcher(str);
            System.out.println(match.matches());
            if(match.matches()){
                System.out.println(match.group(1));
            }
            System.out.println();
        }
    }
}
