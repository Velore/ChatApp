import com.czh.dao.AttentionMapper;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.time.LocalDateTime;

public class AttentionTest {

    @Test
    public void addAttentionTest(){
        SqlSession session = MybatisUtils.getSqlSession();
        AttentionMapper mapper = session.getMapper(AttentionMapper.class);
        System.out.println(mapper.addAttention("u2", "u1", LocalDateTime.now()));
        session.commit();
    }

    @Test
    public void deleteAttentionTest(){
        SqlSession session = MybatisUtils.getSqlSession();
        AttentionMapper mapper = session.getMapper(AttentionMapper.class);
        System.out.println(mapper.deleteAttention("u1", "u2"));
        session.commit();
    }

    @Test
    public void queryFollowerByIdTest(){
        AttentionMapper mapper = MybatisUtils.getSqlSession().getMapper(AttentionMapper.class);
        System.out.println(mapper.queryFollowerById("u1"));
    }

    @Test
    public void queryAttentionByIdTest(){
        AttentionMapper mapper = MybatisUtils.getSqlSession().getMapper(AttentionMapper.class);
        System.out.println(mapper.queryAttentionById("u1"));
    }

    @Test
    public void queryAttentionBeforeTimeTest(){
        AttentionMapper mapper = MybatisUtils.getSqlSession().getMapper(AttentionMapper.class);
        System.out.println(mapper.queryAttentionBeforeTime("u1", LocalDateTime.now()));
    }

    @Test
    public void queryAttentionAfterTimeTest(){
        AttentionMapper mapper = MybatisUtils.getSqlSession().getMapper(AttentionMapper.class);
        System.out.println(mapper.queryAttentionAfterTime("u1", LocalDateTime.now()));
    }
}
