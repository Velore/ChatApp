package com.czh.bo;

import java.io.Serializable;

/**
 * 用户登录凭证
 * @author chenzhuohong
 */
public class LoginBo implements Serializable {

    /**
     * 登录用户的id
     */
    private String loginUid;

    public LoginBo(String loginUid) {
        this.loginUid = loginUid;
    }

    public LoginBo() {

    }

    @Override
    public String toString() {
        return "LoginBo{" +
                "loginUid='" + loginUid + '\'' +
                '}';
    }

    public String getLoginUid() {
        return loginUid;
    }

    public void setLoginUid(String loginUid) {
        this.loginUid = loginUid;
    }

}