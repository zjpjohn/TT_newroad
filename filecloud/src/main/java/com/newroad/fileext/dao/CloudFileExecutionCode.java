package com.newroad.fileext.dao;

public enum CloudFileExecutionCode {
	GET_OBJECT(1),
	PUT_OBJECT(2),
	DELETE_OBJECT(3),
	GET_OBJECT_INFO(4),
	CREATE_PUBLICLINK(5),
	DELETE_PUBLICLINK(6),
	GET_OBJECT_THUMBNAIL(7),
	GET_OBJECT_DOCTHUMBNAIL(8),
	BATCH_GET_OBJECTS(9),
	BATCH_PUT_OBJECTS(10),
	BATCH_CREATE_PUBLICLINKS(11),
	BATCH_CREATE_OBJECTS(12);
	
	Integer signal;
	
	private CloudFileExecutionCode(Integer signal){
		this.signal=signal;
	}

	public Integer getSignal() {
		return signal;
	}
}
