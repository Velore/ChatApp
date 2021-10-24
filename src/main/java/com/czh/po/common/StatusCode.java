package com.czh.po.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回信息中携带的状态码
 * @author chenzhuohong
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StatusCode {

    /**
     * 成功运行结束代码 0
     */
    SUCCESS_CODE("成功运行", 0),
    /**
     * 运行过程中出现错误代码 1
     */
    ERROR_CODE("程序异常终止", 1),
    /**
     * 没有找到对应信息的代码 -1
     */
    NOT_FOUND_CODE("没有找到对应信息", -1),
    /**
     * 权限不足的代码 -2
     */
    NO_PERMISSION_CODE("权限不足", -2);

    private String info;

    private int code;
}
