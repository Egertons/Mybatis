package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {

    static Logger logger = Logger.getLogger(MyTest.class);
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
    public void testLog4j(){
        logger.info("info:进入了Log4j！");
        logger.debug("debug:进入了Log4j！");
        logger.error("error:进入了Log4j！");
    }

    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startIndex",0);
        map.put("fill",2);
        List<User> userByLimit = mapper.getUserByLimit(map);
        for (User u : userByLimit) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        RowBounds rowBounds = new RowBounds(1, 2);
        //通过Java代码层面实现分页！
        List<User> userList = sqlSession.selectList("com.zhanglin.dao.UserMapper.getUserByRowBounds",null,rowBounds);
        for (User u: userList) {
            System.out.println(u.toString());
        }
        sqlSession.close();
    }
}