package com.newroad.cos.pilot;

interface OssManagerListener {
	
	boolean onProgress(long completed, long length, Object userData);
	
	long getProgressInterval();
}
