package com.newroad.user.sns.service.login;

import java.util.Map;

import com.newroad.user.sns.model.login.LoginContext;


public interface OpenAuthIf {

  public LoginContext auth(Map<String, Object> para);
  
  public PostAuthResponse executePostAuthTask(LoginContext context) throws Exception;

}
