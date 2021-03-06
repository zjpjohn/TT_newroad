package com.lenovo.fulltext.service;

import net.sf.json.JSONObject;

import com.lenovo.tool.apiresult.ServiceResult;

/**
 * @info Search Service Interface
 * @author tangzj1
 * @date Aug 26, 2013
 * @version
 */

public interface SearchServiceIf {

	public ServiceResult<JSONObject> search(String userID,String queryKeyword,Integer searchFrom,Integer searchRange);
}
