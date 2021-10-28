package com.czh.po.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户端输入的命令
 * @author chenzhuohong
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ClientCommand {

    /**
     * 发送聊天信息
     */
    SEND_CHAT_MSG("send", "发送聊天信息"),

    /**
     * 修改用户信息
     */
    ALTER_USER_INFO("alter", "修改用户信息"),

    /**
     * 查询用户、群组、聊天信息
     */
    QUERY_INFO("info", "查询用户、群组、聊天信息"),

    /**
     * 客户端注销
     */
    SIGN_OUT("end", "客户端注销");

    /**
     * 命令字符串
     */
    private String command;

    private String info;
}
