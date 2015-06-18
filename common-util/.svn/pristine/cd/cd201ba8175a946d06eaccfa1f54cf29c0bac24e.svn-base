package com.newroad.util.apiresult;

import net.sf.json.JSONObject;


/**
 * 接口异常类,截获此对象的代码将得到已定义的错误返回码和错误信息
 *
 */
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = -5724913343978233993L;

	public static final ReturnCode DEFAULT_RETURN_CODE = ReturnCode.OK;
	
	private ReturnCode returnCode = DEFAULT_RETURN_CODE;
	private String returnMessage ;
	
	public ApiException(){
	}
	
	public ApiException(ReturnCode returnCode, String returnMessage){
		super(returnMessage);
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}
	
	public ApiException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}
	
	public ApiException(ReturnCode returnCode, Throwable e) {
		super(e);
		this.returnCode = returnCode;
		this.returnMessage = e.getMessage();
	}
	
	public ApiException(ReturnCode returnCode, String returnMessage, Throwable e) {
		super(returnMessage,e);
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}
	
	public ReturnCode getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("returnCode", this.returnCode.toString());
		o.put("returnMessage", this.returnMessage);
		return o;
	}
	
	public String toString(){
		return toJSONObject().toString();
	}
	
}
