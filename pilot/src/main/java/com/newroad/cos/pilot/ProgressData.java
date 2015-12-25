/**
 * @info
 * @author tangzj1
 * @date  Nov 19, 2013 
 * @version 
 */
package com.newroad.cos.pilot;

/**
 * @author tangzj1
 * 
 */
public class ProgressData {
	long total;
	long completed = 0;
	long currentProgress = -1;
	Object userData;

	protected ProgressData(long total, Object userData) {
		this.total = total;
		this.userData = userData;
	}

	public long getCompleted() {
		return completed;
	}

	public void setCompleted(long completed) {
		this.completed = completed;
	}
}
