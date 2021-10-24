package com.czh.po.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返回信息的类
 * @author chenzhuohong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnInfo {

    /**
     * 状态码
     */
    private StatusCode statusCode;

    /**
     * 返回信息
     */
    private String briefInfo;

    @Override
    public String toString() {
        return "ReturnInfo{" +
                "statusCode=" + statusCode +
                ", briefInfo='" + briefInfo + '\'' +
                '}';
    }
}
