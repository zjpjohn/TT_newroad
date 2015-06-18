package com.newroad.util.log;

/**
 * http request 信息
 * @author xupeng
 *
 */
public class RequestInfo {
	/** tomcat 端口 */
	int port;
	/** 请求的接口地址 */
	String url;
	
	public RequestInfo() {}
	public RequestInfo(int port, String url) {
		this.port = port;
		this.url = url;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "RequestData [port=" + port + ", url=" + url + "]";
	}
}
