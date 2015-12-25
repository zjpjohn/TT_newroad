package com.newroad.cos.pilot.monitor;

/**
 * @author tangzj1
 * @version 2.0
 * @since May 12, 2014
 */
public class FileObject {

	private String key;

	private String fileName;

	private Long fileSize;

	private String fileType;

	private Boolean executeStatus;

	private Long downLoadExecuteTime;

	private Long uploadExecuteTime;

	public FileObject() {

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Boolean getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Boolean executeStatus) {
		this.executeStatus = executeStatus;
	}

	public Long getDownLoadExecuteTime() {
		return downLoadExecuteTime;
	}

	public void setDownLoadExecuteTime(Long downLoadExecuteTime) {
		this.downLoadExecuteTime = downLoadExecuteTime;
	}

	public Long getUploadExecuteTime() {
		return uploadExecuteTime;
	}

	public void setUploadExecuteTime(Long uploadExecuteTime) {
		this.uploadExecuteTime = uploadExecuteTime;
	}

}
