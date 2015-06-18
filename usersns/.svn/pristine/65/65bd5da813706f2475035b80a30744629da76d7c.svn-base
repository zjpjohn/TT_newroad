package com.newroad.user.sns.model.user;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 用户 (User info in DB)
 */
public class User implements Serializable {

  private static final long serialVersionUID = -9073355599858333309L;
  private Long userID;
  /**
   * 短ID,用于推广链接
   */
  private String shortID;
  /**
   * 用户类型
   */
  private Integer userType;
  /**
   * 登录名
   */
  private String loginName;
  /**
   * 密码
   */
  // private transient String passWord;
  private transient String passWord;
  /**
   * 当前状态
   */
  private Integer status;
  /**
   * 昵称
   */
  private String nickName;
  /**
   * 真实名字
   */
  private String name;
  /**
   * 性别
   */
  private Integer gender;
  /**
   * 生日
   */
  private Date birthday;
  /**
   * 电子邮箱
   */
  private String email;
  /**
   * QQ
   */
  private String qq;
  /**
   * MSN
   */
  private String msn;
  /**
   * 电话
   */
  private String phone;
  /**
   * 邮编
   */
  private String zip;
  /**
   * 地址
   */
  private String address;
  /**
   * 故乡
   */
  private String hometown;
  /**
   * 所在地
   */
  private String location;
  /**
   * 血型
   */
  private int bloodType;
  /**
   * 初中
   */
  private String juniorHighSchool;
  /**
   * 高中
   */
  private String seniorHighSchool;
  /**
   * 大学
   */
  private String college;
  /**
   * 工作单位
   */
  private String workUnit;
  /**
   * 个人介绍
   */
  private String description;
  /**
   * 头像
   */
  private String portrait = "http://zy.lenovomm.com/images/header-1.png";
  /**
   * 最后登录时间
   */
  private Date lastLoginTime;
  /**
   * 最后操作时间
   */
  private Date lastOperateTime;

  /**
   * @info : 用户类型
   * @author: xiangping_yu
   * @data : 2013-11-4
   * @since : 1.5
   */
  public static enum UserType {
    def(0), lenovo(1), mobile(2), qq(3), weibo(4), wechat(5);

    private int code;

    private UserType(int code) {
      this.code = code;
    }

    public static UserType fromCode(Integer code) {
      if (code == null)
        return def;
      for (UserType ut : values()) {
        if (code.equals(ut.getCode()))
          return ut;
      }
      return def;
    }

    public int getCode() {
      return code;
    }
  }

  public User() {
    super();
  }

  public User(Integer userType, String loginName, String passWord, String nickName, String phone) {
    super();
    this.userType = userType;
    this.loginName = loginName;
    this.passWord = passWord;
    this.nickName = nickName;
    this.phone = phone;
  }



  // Mobile account info
  public User(String phone, String passWord) {
    super();
    this.phone = phone;
    this.passWord = passWord;
  }

  public Long getUserID() {
    return userID;
  }

  public void setUserID(Long userID) {
    this.userID = userID;
  }

  public String getShortID() {
    return shortID;
  }

  public void setShortID(String shortID) {
    this.shortID = shortID;
  }

  public Integer getUserType() {
    return userType;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getQq() {
    return qq;
  }

  public void setQq(String qq) {
    this.qq = qq;
  }

  public String getMsn() {
    return msn;
  }

  public void setMsn(String msn) {
    this.msn = msn;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getHometown() {
    return hometown;
  }

  public void setHometown(String hometown) {
    this.hometown = hometown;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getBloodType() {
    return bloodType;
  }

  public void setBloodType(int bloodType) {
    this.bloodType = bloodType;
  }

  public String getJuniorHighSchool() {
    return juniorHighSchool;
  }

  public void setJuniorHighSchool(String juniorHighSchool) {
    this.juniorHighSchool = juniorHighSchool;
  }

  public String getSeniorHighSchool() {
    return seniorHighSchool;
  }

  public void setSeniorHighSchool(String seniorHighSchool) {
    this.seniorHighSchool = seniorHighSchool;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getWorkUnit() {
    return workUnit;
  }

  public void setWorkUnit(String workUnit) {
    this.workUnit = workUnit;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPortrait() {
    if (StringUtils.isBlank(portrait)) {
      portrait = "http://zy.lenovomm.com/images/header-1.png";
    }
    return portrait;
  }

  public void setPortrait(String portrait) {
    this.portrait = portrait;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Date getLastOperateTime() {
    return lastOperateTime;
  }

  public void setLastOperateTime(Date lastOperateTime) {
    this.lastOperateTime = lastOperateTime;
  }
}
