package com.newroad.user.sns.constant;

import org.springframework.http.MediaType;

/**
 * @info  : SNS 常量定义 
 * @author: xiangping_yu
 * @data  : 2013-10-29
 * @since : 1.5
 */
public class SnsConstant {
  
  private SnsConstant() {}
	
	/** The Constant CONTENT_TYPE_JSON. */
	public static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8" ;
	
	/** 用户状态 （黑名单） */
	public static final int USER_STATUS_BLACK = 1;
	
	/**
	 * 数据采集HTTP KEY 
	 */
	public static final String COLLECT_HEADER_KEY = "Collect-Data";
	
	public static final String USER_ID_KEY = "userID";
	
	/** 缺省群组图标 */
	public static final String DEFAULT_GROUP_ICON = "group-icon-default.ico";
	
	/** 账号类型,ID */
	public static final int ACCOUNT_TYPE_ID = 1;
	/** 账号类型,邮箱地址 */
	public static final int ACCOUNT_TYPE_EMAIL = 2;
	/** 账号类型,手机号码 */
	public static final int ACCOUNT_TYPE_PHONE_NUMBER = 3;
	
	public static final Integer[] ACCOUNT_TYPE = {ACCOUNT_TYPE_ID, ACCOUNT_TYPE_EMAIL, ACCOUNT_TYPE_PHONE_NUMBER};
	
	/** 申请方向,1,申请 */
	public static final int REQUEST_DIRECTION_APPLY = 1;
	
	/** 申请方向,2,邀请 */
	public static final int REQUEST_DIRECTION_INVITE = 2;
	
	public static final Integer[] REQUEST_DIRECTION = {REQUEST_DIRECTION_APPLY, REQUEST_DIRECTION_INVITE};
	
	/** 申请状态 1:等待 */
	public static final int REQUEST_STATUS_WAIT = 1;
	/** 申请状态 2:同意 */
	public static final int REQUEST_STATUS_APPROVE = 2;
	/** 申请状态 3:驳回 */
	public static final int REQUEST_STATUS_REJECT = 3;
	/** 用于status参数校验, 不包括初始状态(wait:1)*/
	public static final Integer[] REQUEST_STATUS = { REQUEST_STATUS_APPROVE, REQUEST_STATUS_REJECT};
	
	/** 群组状态 1:开放 */
	public static final int GROUP_STATUS_OPEN = 1;
	/** 群组状态 2:关闭 */
	public static final int GROUP_STATUS_CLOSE = 2;
	/** 群组状态 3:删除 */
	public static final int GROUP_STATUS_DELETE = 3;
	
	/** 群组类型 1:普通群组 */
	public static final int GROUP_TYPE_NORMAL = 1;
	/** 群组类型 2:超级群组 */
	public static final int GROUP_TYPE_SUPER = 2;
	public static final Integer[] GROUP_TYPE = {GROUP_TYPE_NORMAL, GROUP_TYPE_SUPER};

	/**
	 * 分页 KEY 当前第几页
	 */
	public static final String PAGE_KEY = "page";
	
	/**
	 * 分页大小 KEY
	 */
	public static final String PAGE_SIZE_KEY = "size";
}
