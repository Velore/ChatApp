package Po.Common.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 返回客户端需要查看的信息
 * @author chenzhuohong
 */
public class InfoMessage extends Message{

    /**
     * 客户端向服务器请求的信息类型
     * u:用户信息，g:群组信息，c:聊天信息
     */
    private char infoType;

    /**
     * 请求的具体参数
     * 若infoType是u，specType内存放uid(可多个)
     * 若infoType是g，specTYpe内存放gid(可多个)
     * 若infoType是c，specType内存放群组gid(唯一)
     */
    private ArrayList<String> specType;
    /**
     * 服务器端向客户端响应的信息列表（用户信息 或 群组信息 或 聊天信息）
     */
    private ArrayList<String> infoList;

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
