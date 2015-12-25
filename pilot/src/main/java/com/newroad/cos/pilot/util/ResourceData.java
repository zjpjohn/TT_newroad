package com.newroad.cos.pilot.util;

/**
 * @info
 * @author tangzj1
 * @date  Dec 8, 2013 
 * @version 
 */
public class ResourceData {

	private String key;
	private String path;
	private long size;
	private String contentType;
	
	/**
	 * @param key
	 * @param path
	 * @param size
	 * @param contentType
	 */
	public ResourceData(String key, String path, long size, String contentType) {
		super();
		this.key = key;
		this.path = path;
		this.size = size;
		this.contentType = contentType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
