package com.czh.po.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chenzhuohong
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MessageType {

    /**
     * 用户状态信息
     */
    STATUS_TYPE("用户状态", 's'),

    /**
     * 用户查询信息
     */
    INFO_TYPE("用户查询", 'i'),

    /**
     * 用户信息更新
     */
    UPDATE_TYPE("用户信息更新", 'u'),

    /**
     * 用户聊天信息
     */
    CHAT_TYPE("用户聊天", 'c');

    private String info;

    private char type;

    @Override
    public String toString() {
        return "MessageType{" +
                "info='" + info + '\'' +
                ", type=" + type +
                '}';
    }
}
