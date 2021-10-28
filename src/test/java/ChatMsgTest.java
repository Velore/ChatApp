import com.czh.dao.ChatMsgMapper;
import com.czh.po.common.message.ChatMessage;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatMsgTest {

    @Test
    public void addChatMsgTest(){
        SqlSession session = MybatisUtils.getSqlSession();
        ChatMsgMapper mapper = session.getMapper(ChatMsgMapper.class);
        ChatMessage message = new ChatMessage("10001", "addChatMsgTest");
        message.setSenderId("u1");
        System.out.println(mapper.addChatMsg(message));
        session.commit();
    }

    @Test
    public void queryChatMsgByGroupIdTest(){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        List<ChatMessage> msgList = mapper.queryChatMsgByGroupId("10001");
        for(ChatMessage message : msgList){
            System.out.println(message);
        }
    }

    @Test
    public void queryChatMsgByGroupIdAndSenderIdTest(){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        List<ChatMessage> msgList = mapper.queryChatMsgByGroupIdAndSenderId("10001", "u1");
        for(ChatMessage message : msgList){
            System.out.println(message);
        }
    }

    @Test
    public void queryChatMsgLikeMsgStrTest(){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        List<ChatMessage> msgList = mapper.queryChatMsgLikeMsgStr("10001", "add");
        for(ChatMessage message : msgList){
            System.out.println(message);
        }
    }

    /**
     * 测试有问题
     */
    @Test
    public void queryChatMsgWithSendTimeTest(){
        LocalDateTime begin = LocalDateTime.parse("20000101101010", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        LocalDateTime end = LocalDateTime.parse("20211101101010", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(begin+"\t"+end);
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        List<ChatMessage> msgList = mapper.queryChatMsgWithSendTime("10001", begin, end);
        for(ChatMessage message : msgList){
            System.out.println(message);
        }
    }
}
