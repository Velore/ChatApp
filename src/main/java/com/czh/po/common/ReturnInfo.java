package com.czh.po.common;

/**
 * 返回信息的类
 * 目前暂时用不上，只是新建放在这里
 * @author chenzhuohong
 */
public class ReturnInfo {

    /**
     * 状态码
     */
    public enum StatusCode{
        /**
         * 错误代码 1
         */
        ERROR_CODE,
        /**
         * 没有找到对应信息的代码 -1
         */
        NOT_FOUND_CODE,
        /**
         * 权限不足的代码 -2
         */
        NO_PERMISSION_CODE
    }

    /**
     * 返回信息的简述
     */
    public String briefInfo;

}
