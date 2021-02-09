package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MyTest {
    @Test
    public void queryUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.queryUserById(1);
        System.out.println(user.toString());
        System.out.println("==========================");
        Map map = new HashMap();
        map.put("id",2);
        map.put("pwd","11111");
        int i = mapper.updateUser(map);
        if (i>0)
            System.out.println("更新成功");
        User user1 = mapper.queryUserById(1);
        System.out.println(user1.toString());
        sqlSession.clearCache();//手动清理缓存
        System.out.println(user==user1);
        sqlSession.close();
    }
}
