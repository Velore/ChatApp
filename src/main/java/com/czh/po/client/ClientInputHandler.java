package com.czh.po.client;

import com.czh.po.common.User;
import com.czh.po.common.message.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenzhuohong
 */
public class ClientInputHandler {

    /**
     * 最短的输入命令长度：2;
     * 一般用来快速发送聊天信息(省略最前面的send命令)：[group chatMsg];
     * 发送聊天信息标准格式：[send group chatMsg];
     */
    public static final int SHORTEST_COMMAND_LENGTH = 2;

    /**
     * 正则表达式的匹配规则1;
     * 匹配多个大小写字母和数字;
     */
//    public static final Pattern MSG_PATTERN = Pattern.compile("(\\w+)");

    /**
     * 正则表达式的匹配规则2;
     * 匹配多个中文字符,大小写字母和数字;
     */
//    public static final Pattern MSG_PATTERN = Pattern.compile("([\\u4e00-\\u9fa5_a-zA-Z0-9]+)");

    /**
     * 正则表达式的匹配规则3;
     * 匹配多个中文字符,大小写字母和数字和某些标点符号;
     */
    public static final Pattern MSG_PATTERN = Pattern.compile("([\\u4e00-\\u9fa5_a-zA-Z0-9\\p{P}]+)");

    /**
     * 匹配字符串;
     */
    public static final Pattern STR_PATTERN = Pattern.compile("(^\t+)");

    /**
     * 匹配日期;
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 将用户输入的[字符串]通过正则匹配分割为[命令序列];
     * @param input 要切割的输入字符串
     * @param pattern 正则匹配的匹配规则
     * @return 完成分割的字符串数组prevMsg
     */
    public static ArrayList<String> inputSplit(String input, Pattern pattern){
        ArrayList<String> prevMsg = new ArrayList<>();
//        去除输入两端的空格后进行正则匹配
        Matcher m = pattern.matcher(input.strip());
        while(m.find()){
//            将正则匹配后得到的结果返回数组
            prevMsg.add(m.group());
        }
        return prevMsg;
    }

    /**
     * 打包用户需要修改的信息;
     * @param prevMsg 需要修改的信息
     * @return UpdateMessage
     */
    public static Message packAlterMsg(ArrayList<String> prevMsg){
        User alterUser = new User();
        try{
            //修改用户信息
            //格式[alter 属性1 值1 属性2 值2...]
            for(int i = 1 ; i < prevMsg.size() ; i += 2){
                switch (prevMsg.get(i)){
                    case "name":
                        //修改用户名字
                        alterUser.setName(prevMsg.get(i+1));
                        break;
                    case "pwd":
                        //修改用户密码
                        alterUser.setPwd(prevMsg.get(i+1));
                        break;
                    case "addg":
                        //若群组存在，则申请进入群组
                        break;
                    case "delq":
                        //若群组存在且为群组成员，则退出群组
                        break;
                    case "addf":
                        //新增关注
                        break;
                    case "delf":
                        //取消关注
                        break;
                    default:
                        System.out.println("未识别的参数"+prevMsg.get(i));
                }
            }
            //发送更新用户的信息
            return new UpdateMessage(alterUser);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println("请按格式输入[alter 属性1 值1 属性2 值2...]");
        }
        return null;
    }


    /**
     * 客户端输入转换为Message;
     * @param prevMsg 要分析的字符串数组
     * @return 不同类型的Message
     */
    public static Message inputAnalyse(ArrayList<String> prevMsg){
        StringBuilder msg = new StringBuilder();
        if(prevMsg.size() == SHORTEST_COMMAND_LENGTH){
            //发送信息简写：格式[gid msg]
            //省略了send
            return new ChatMessage(prevMsg.get(0), prevMsg.get(1));
        }else if(prevMsg.size() > SHORTEST_COMMAND_LENGTH){
            if(ClientCommand.SEND_CHAT_MSG.getCommand().equals(prevMsg.get(0))){
                //格式[send gid msg]
                //格式[send 群组id 聊天信息]
                return new ChatMessage(prevMsg.get(1), msg.append(prevMsg.get(2)).toString());
            }else if(ClientCommand.QUERY_INFO.getCommand().equals(prevMsg.get(0))){
                //默认返回用户信息
                ArrayList<String> specType = new ArrayList<>();
                //将查询对象的查询类型添加到InfoMessage中
                char infoType = prevMsg.get(1).charAt(0);
                //添加查询的具体信息到InfoMessage中
                for(int i = 2;i< prevMsg.size();i++){
                    specType.add(prevMsg.get(i));
                }
                //向服务器发送查询信息
                return new InfoMessage(infoType, specType);
            }else if(ClientCommand.ALTER_USER_INFO.getCommand().equals(prevMsg.get(0))){
                return packAlterMsg(prevMsg);
            }
        }else{
            System.out.println("请输入正确的指令");
        }
        //没有识别对应的命令时，返回null
        return null;
    }

    /**
     * 将聊天记录的对象转换为可输出的字符串;
     * @deprecated
     * @param chatMsgList 聊天记录的列表
     * @return 字符串列表
     */
    public static ArrayList<String> chatMsgToString(ArrayList<ChatMessage> chatMsgList){
        ArrayList<String> msgStrList= new ArrayList<>();
        for(ChatMessage cm : chatMsgList){
            msgStrList.add(cm.toString());
        }
        return msgStrList;
    }

    /**
     * 将字符串转换为聊天记录的对象，用于从文件中读取聊天记录时;
     * @deprecated
     * @param s 要转换的字符串
     * @param pattern 正则表达式的匹配规则，一般用MsgUtils自带的静态规则匹配即可
     * @return 聊天记录的对象
     */
    public static ChatMessage stringToChatMsg(String s, Pattern pattern){
        ChatMessage cm = new ChatMessage();
        cm.setMsgType(MessageType.CHAT_TYPE);
        ArrayList<String> strList = ClientInputHandler.inputSplit(s, pattern);
        cm.setGid(strList.get(0));
        cm.setSendTime(LocalDateTime.parse(strList.get(1), DATE_TIME_FORMATTER));
        cm.setSenderId(strList.get(2));
        cm.setMsgStr(strList.get(3));
        return cm;
    }
}
