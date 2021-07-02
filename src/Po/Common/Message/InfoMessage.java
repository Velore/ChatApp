package Po.Common.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 客户端发送给服务器需要查询的对象
 * 服务器返回客户端查询的结果信息
 * @author chenzhuohong
 */
public class InfoMessage extends Message{

    /**
     * 客户端向服务器查询的信息类型
     * u:用户信息，g:群组信息，c:聊天信息
     */
    private char infoType;

    /**
     * 查询的具体参数
     * 若infoType是u，specType内存放uid(可多个)，代表查询用户信息
     * 若infoType是g，specTYpe内存放gid(可多个)，代表查询群组信息
     * 若infoType是c，specType内存放群组gid(唯一)，代表查询聊天信息
     */
    private ArrayList<String> specType;
    /**
     * 服务器端向客户端响应的信息列表（用户信息 或 群组信息 或 聊天信息）
     */
    private ArrayList<String> infoList;

    /**
     * 系统给客户端发送的通知信息，无需回复
     * @param info 系统通知
     */
    public InfoMessage(String info){
        this.sendTime = LocalDateTime.now();
        this.msgType = 'i';
        this.infoList = new ArrayList<>();
        this.addInfo(info);
    }

    public InfoMessage(char infoType, ArrayList<String> specType){
        this.sendTime = LocalDateTime.now();
        this.msgType = 'i';
        this.infoType = infoType;
        this.specType = specType;
        this.infoList = new ArrayList<>();
    }

    public char getInfoType() {
        return infoType;
    }

    public void setInfoType(char infoType) {
        this.infoType = infoType;
    }

    public ArrayList<String> getSpecType() {
        return specType;
    }

    public void setSpecType(ArrayList<String> specType) {
        this.specType = specType;
    }

    @Override
    public ArrayList<String> getInfo() {
        return infoList;
    }

    public void addInfo(String info){
        this.infoList.add(info);
    }

    public void setInfoList(ArrayList<String> infoList) {
        this.infoList = infoList;
    }

    @Override
    public String toString() {
        return "InfoMessage{" +
                "infoType=" + infoType +
                ", specType=" + specType +
                ", infoList=" + infoList +
                ", loginBo=" + loginBo +
                ", msgType=" + msgType +
                ", sendTime=" + sendTime +
                '}';
    }
}
