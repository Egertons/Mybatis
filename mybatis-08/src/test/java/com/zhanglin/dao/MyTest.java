package com.zhanglin.dao;

import com.zhanglin.pojo.Blog;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {
    @Test
    public void t(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog all = mapper.getAll();
        System.out.println(all.toString());
        sqlSession.close();
    }
}
