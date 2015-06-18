package com.newroad.user.sns.service.login;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.newroad.user.sns.model.login.LoginContext;
import com.newroad.user.sns.model.user.User;
import com.newroad.util.apiresult.ReturnCode;

/**
 * @info : ST认证登录
 * @author: xiangping_yu
 * @data : 2013-10-30
 * @since : 1.5
 */
@Service
public class LenovoSTLogin implements OpenAuthIf {

  private static final Logger log = LoggerFactory.getLogger(LenovoSTLogin.class);

  private static final String URL = "http://passport.lenovo.com/interserver/authen/1.2/getaccountid";

  /**
   * 认证解析器
   */
  private Map<String, TokenResponseAnalyze> analyzeMap;

  public LoginContext auth(Map<String, Object> para) {
    log.info("auth lenovo st: authinfo[" + para + "]");

    LoginContext context = new LoginContext();

    String lpsust = para.get("LenovoToken") == null ? (String) para.get("lpsust") : (String) para.get("LenovoToken");
    if (StringUtils.isBlank(lpsust)) {
      context.setReturnCode(ReturnCode.BAD_REQUEST);
      context.setReturnMsg("parameter \"lpsust\" not found!");
      return context;
    }

    if (StringUtils.isBlank((String) para.get("realm"))) {
      context.setReturnCode(ReturnCode.BAD_REQUEST);
      context.setReturnMsg("parameter \"realm\" not found!");
      return context;
    }

    // Check whether we need to access COS to get Connector or not.
    // if(para.get("skipCloud")!=null){
    // context.setSkipCloudAccess(true);
    // }
    context.setUserAuthType(User.UserType.lenovo.getCode());
    context.setLpsust(lpsust);
    context.setRealm((String) para.get("realm"));
    try {
      // 解析认证结果
      distribute(executePostAuthTask(context).getResponse(), context);
    } catch (Exception e) {
      log.error("lenovo st auth error:", e);
      context.setReturnCode(ReturnCode.AUTH_BAD);
      context.setReturnMsg("auth lenovo st error!");
    }
    return context;
  }

  /**
   * connect lenovo id service
   */
  public PostAuthResponse executePostAuthTask(LoginContext context) throws Exception {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(URL);
    post.addParameter("lpsust", context.getLpsust());
    post.addParameter("realm", context.getRealm());
    try {
      client.executeMethod(post);
      return new PostAuthResponse(post.getStatusCode(), post.getResponseBodyAsString());
    } finally {
      post.releaseConnection();
    }
  }

  
  
  /**
   * 分发处理结果
   */
  private void distribute(String response, LoginContext context) throws DocumentException {
    SAXReader reader = new SAXReader();
    Document doc = reader.read(new ByteArrayInputStream(response.getBytes()));

    Element root = doc.getRootElement();
    String status = root.getName();

    analyzeMap.get(status).analyze(root, context);
  }

  /**
   * token 验证返回结果分析接口
   */
  public static interface TokenResponseAnalyze {
    public void analyze(Element root, LoginContext res);
  }
  
  /**
   * token 验证失败处理
   */
  @SuppressWarnings("unused")
  private static class TokenResponseErrorAnalyze implements TokenResponseAnalyze {
    @Override
    public void analyze(Element root, LoginContext context) {
      Element eleCode = root.element("Code");
      // Element eleTimestamp = root.element("Timestamp");
      // Element eleMessage = root.element("Message");
      // Element eleDetail = root.element("Detail");
      // Element eleSource = root.element("Source");
      // Element eleURL = root.element("URL");

      log.error("auth lenovo st error, code=" + eleCode.getText());

      context.setReturnCode(ReturnCode.AUTH_BAD);
      context.setReturnMsg("auth lenovo st error, code=" + eleCode.getText());
    }
  }

  /**
   * token 验证成功处理
   */
  @SuppressWarnings("unused")
  private static class TokenResponseSuccessAnalyze implements TokenResponseAnalyze {
    @Override
    public void analyze(Element root, LoginContext context) {
      Element eleAccountID = root.element("AccountID");
      Element eleUsername = root.element("Username");
      Element eleDeviceID = root.element("DeviceID");
      Element eleVerified = root.element("verified");
      Element eleThirdname = root.element("Thirdname");
      // Element elePID = root.element("PID");
      // Element eleIdcLocation = root.element("idcLocation");

      context.setThirdPartyAccount(eleAccountID.getText());
      context.setDeviceID(eleDeviceID == null ? null : eleDeviceID.getText());
      context.setUserName(eleUsername == null ? null : eleUsername.getText());
      context.setThirdName(eleThirdname == null ? null : eleThirdname.getText());
      context.setVerified(eleVerified == null ? null : eleVerified.getText());
    }
  }

  public void setAnalyzeMap(Map<String, TokenResponseAnalyze> analyzeMap) {
    this.analyzeMap = analyzeMap;
  }
}
