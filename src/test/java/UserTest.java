import com.czh.dao.UserMapper;
import com.czh.po.common.User;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class UserTest {

    @Test
    public void addUserTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            User u = new User("n1", "pwd1");
            u.setLastOnlineTime(LocalDateTime.now());
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            System.out.println(userMapper.addUser(u));
            sqlSession.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateUserTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            User u = new User("name2", "pwd2");
            u.setUid("71735");
            u.setLastOnlineTime(LocalDateTime.now());
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            System.out.println(userMapper.updateUser(u));
            sqlSession.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteUserTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            System.out.println(userMapper.deleteUser("b0le8"));
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryAllUserTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.queryAllUser();

            for(User user : userList){
                System.out.println(user.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryUserByIdTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.queryUserById("u1");
            System.out.println(user.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void queryUserLikeName(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.queryUserLikeName("n");

            for(User user : userList){
                System.out.println(user.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
