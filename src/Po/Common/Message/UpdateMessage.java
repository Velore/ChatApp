package Po.Common.Message;

import Po.Common.Message.Message;
import Po.Common.User;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
public class UpdateMessage extends Message {

    private User updateUser;

    public UpdateMessage(){}

    public UpdateMessage(User user){
        this.sendTime = LocalDateTime.now();
        this.msgType = 'u';
        this.updateUser = user;
    }

    @Override
    public User getInfo() {
        return this.updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "loginBo=" + loginBo +
                ", msgType=" + msgType +
                ", sendTime=" + sendTime +
                ", updateUser=" + updateUser +
                '}';
    }
}
