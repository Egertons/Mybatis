package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {

    @Test
    public void getAllUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        for (User u : userList) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getUserListByAnno(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userListByAnno = mapper.getUserListByAnno();
        for (User user: userListByAnno) {
            System.out.println(user.toString());
        }
        sqlSession.close();
    }
}