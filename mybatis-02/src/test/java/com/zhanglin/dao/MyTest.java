package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {

    @Test
    public void test1(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        for (User u : userList) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();
    }

    @Test
    public void insertUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int uu = mapper.insertUser(new User(4, "uu", "99787685yv"));
        if (uu>0){
            System.out.println("数据插入成功");
        }
        //增删改必须在操纵完数据库后提交事物。
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int oo = mapper.updateUser(new User(4, "oo", "999999999"));
        if (oo>0){
            System.out.println("修改成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.deleteUser(4);
        if (i>0){
            System.out.println("用户删除成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    //万能的Map
    @Test
    public void insertUser2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId",8);
        map.put("userName","kkkk");
        map.put("passWord","233333");

        int i = mapper.insertUser2(map);
        if (i>0){
            System.out.println("万能的Map插入成功！！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    //模糊查询
    @Test
    public void getUserLike(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> map = mapper.getUserLike("李");
        for (User u : map) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }
}
