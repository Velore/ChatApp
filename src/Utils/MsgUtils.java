package Utils;

import Bo.LoginBo;
import Po.Common.Message.ChatMessage;
import Po.Common.Message.InfoMessage;
import Po.Common.Message.Message;
import Po.Common.Message.UpdateMessage;
import Po.Common.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenzhuohong
 */
public class MsgUtils {

    /**
     * 正则表达式的匹配规则1
     * 匹配多个大小写字母和数字
     */
//    public static final Pattern MSG_PATTERN = Pattern.compile("(\\w+)");

    /**
     * 正则表达式的匹配规则2
     * 匹配多个中文字符,大小写字母和数字
     */
//    public static final Pattern MSG_PATTERN = Pattern.compile("([\\u4e00-\\u9fa5_a-zA-Z0-9]+)");

    /**
     * 正则表达式的匹配规则3
     * 匹配多个中文字符,大小写字母和数字和某些标点符号
     */
    public static final Pattern MSG_PATTERN = Pattern.compile("([\\u4e00-\\u9fa5_a-zA-Z0-9\\p{P}]+)");


    public static final Pattern STR_PATTERN = Pattern.compile("(^\t+)");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");;

    /**
     * @param input 要切割的输入字符串
     * @return 完成切割的字符串数组prevMsg
     */
    public static ArrayList<String> inputSplit(String input){
        ArrayList<String> prevMsg = new ArrayList<>();
//        去除输入两端的空格后进行正则匹配
        Matcher m = MsgUtils.MSG_PATTERN.matcher(input.strip());
        while(m.find()){
//            将正则匹配后得到的结果返回数组
            prevMsg.add(m.group());
        }
        return prevMsg;
    }

    /**
     * 客户端输入转换为Message
     * @param prevMsg 要分析的字符串数组
     * @return 不同类型的Message
     */
    public static Message inputAnalyse(ArrayList<String> prevMsg, User userTemp){
        StringBuilder msg = new StringBuilder();
        if(prevMsg.size() == 2){
            //发送信息简写：格式[gid msg]
            //省略了send
            return new ChatMessage(prevMsg.get(0), prevMsg.get(1));
        }else if(prevMsg.size() > 2){
            if("send".equals(prevMsg.get(0))){
                //格式[send gid msg]
                //格式[send 群组id 聊天信息]
                return new ChatMessage(prevMsg.get(1), msg.append(prevMsg.get(2)).toString());
            }else if("info".equals(prevMsg.get(0))){
                //默认返回用户信息
                char infoType = 'u';
                ArrayList<String> specType = new ArrayList<>();
                switch (prevMsg.get(1)){
                    case "u":
                        infoType = 'u';
                        break;
                    case "g":
                        infoType = 'g';
                        break;
                    case "c":
                        infoType = 'c';
                        break;
                    default:
                        System.out.println("未识别的参数"+prevMsg.get(1));
                }
                for(int i = 2;i< prevMsg.size();i++){
                    specType.add(prevMsg.get(i));
                }
                return new InfoMessage(infoType, specType);
            }else if("alter".equals(prevMsg.get(0))){
                //格式[alter 属性1 值1 属性2 值2...]
                for(int i = 1 ; i < prevMsg.size() ; i = i + 2){
                    switch (prevMsg.get(i)){
                        case "name":
                            //修改用户名字
                            userTemp.setName(prevMsg.get(i+1));
                            break;
                        case "pwd":
                            //修改用户密码
                            userTemp.setPwd(prevMsg.get(i+1));
                            break;
                        case "addg":
                            //若群组存在，则申请进入群组；若群组不存在，则新建聊天组
                            userTemp.addGroup(prevMsg.get(i+1));
                            break;
                        case "addf":
                            //新增好友
                            //目前只对好友做了重复添加判断，但是可以加自己为好友
                            userTemp.addFriend(prevMsg.get(i+1));
                            break;
                        case "delf":
                            //删除好友
                            userTemp.delFriend(prevMsg.get(i+1));
                            break;
                        default:
                            System.out.println("未识别的参数"+prevMsg.get(i));
                    }
                }
                return new UpdateMessage(userTemp);
            }
        }else{
            System.out.println("请输入正确的指令");
        }
        return null;
    }

    /**
     * 将聊天记录的对象转换为可输出的字符串
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
     * 将字符串转换为聊天记录的对象，用于从文件中读取聊天记录时
     * @param s 要转换的字符串
     * @param pattern 正则表达式的匹配规则，一般用MsgUtils自带的静态规则匹配即可
     * @return 聊天记录的对象
     */
    public static ChatMessage stringToChatMsg(String s, Pattern pattern){
        ChatMessage cm = new ChatMessage();
        cm.msgType = 'c';
        ArrayList<String> strList = MsgUtils.inputSplit(s);
        cm.setGid(strList.get(0));
        cm.sendTime = LocalDateTime.parse(strList.get(1), DATE_TIME_FORMATTER);
        cm.loginBo = new LoginBo();
        cm.loginBo.setLoginUid(strList.get(2));
        cm.setMsgStr(strList.get(3));
        return cm;
    }
}
