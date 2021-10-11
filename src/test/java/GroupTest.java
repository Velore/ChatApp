import com.czh.dao.GroupMapper;
import com.czh.po.common.Group;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class GroupTest {

    @Test
    public void addGroupTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            Group group = new Group("u1");
            group.setGroupName("testName");
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            System.out.println(groupMapper.addGroup(group));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateGroupTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            Group group = new Group("5L068", "u1");
            group.setGroupName("updateTest");
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            System.out.println(groupMapper.updateGroup(group));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteGroupTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            System.out.println(groupMapper.deleteGroup("5L068"));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryGroupById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            Group group =  groupMapper.queryGroupById("10001");

            System.out.println(group.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryGroupByOwnerId(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            List<Group> groupList =  groupMapper.queryGroupByOwnerId("u1");

            for(Group g : groupList){
                System.out.println(g.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryAllGroup(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            List<Group> groupList =  groupMapper.queryAllGroup();

            for(Group g : groupList){
                System.out.println(g.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryGroupLikeName(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            List<Group> groupList =  groupMapper.queryGroupLikeName("name");

            for(Group g : groupList){
                System.out.println(g.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
