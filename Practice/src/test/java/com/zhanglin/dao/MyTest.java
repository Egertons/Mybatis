package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class MyTest {

    @Test
    public void getUserList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();
        for (User u : userList) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById.toString());
        sqlSession.close();
    }

    @Test
    public void insertUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        int i = mapper.insertUser(new User(5, "demo", "heidamo"));
        if (i>0){
            System.out.println("添加用户成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        int i = mapper.updateUser(new User(5, "xi", "Phut_Hon"));
        if (i>0){
            System.out.println("用户修改成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void addUserforMap(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Userid",4);
        map.put("Name","ooo");
        map.put("PassWord","igytfrthdx");
        int i = mapper.addUserforMap(map);
        if (i > 0){
            System.out.println(i+"插入成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void mohuQuary(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> quary = mapper.mohuQuary("李");
        for (User user : quary) {
            System.out.println(user.toString());
        }
        sqlSession.close();
    }
}
