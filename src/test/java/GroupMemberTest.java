import com.czh.dao.GroupMemberMapper;
import com.czh.po.common.GroupMember;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class GroupMemberTest {

    @Test
    public void addMemberTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMemberMapper memberMapper = sqlSession.getMapper(GroupMemberMapper.class);
            System.out.println(memberMapper.addMember(new GroupMember("u1", "10002")));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteMemberTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMemberMapper memberMapper = sqlSession.getMapper(GroupMemberMapper.class);
            System.out.println(memberMapper.deleteMember("u1", "10002"));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryMemberByIdTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMemberMapper memberMapper = sqlSession.getMapper(GroupMemberMapper.class);
            System.out.println(memberMapper.queryMemberById("u4", "10001"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryAllMemberTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMemberMapper memberMapper = sqlSession.getMapper(GroupMemberMapper.class);
            List<GroupMember> memberList = memberMapper.queryAllMember("10001");

            for(GroupMember gm : memberList){
                System.out.println(gm.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryMemberLikeNameTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMemberMapper memberMapper = sqlSession.getMapper(GroupMemberMapper.class);
            List<GroupMember> memberList = memberMapper.queryMemberLikeName("n", "10001");

            for(GroupMember gm : memberList){
                System.out.println(gm.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
