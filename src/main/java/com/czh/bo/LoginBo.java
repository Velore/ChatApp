package com.czh.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户登录凭证
 * @author chenzhuohong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginBo implements Serializable {

    /**
     * 登录用户的id
     */
    private String loginUid;

    @Override
    public String toString() {
        return "LoginBo{" +
                "loginUid='" + loginUid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginBo loginBo = (LoginBo) o;
        return loginUid.equals(loginBo.loginUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginUid);
    }
}