package com.zhanglin.dao;

import com.zhanglin.pojo.Blog;
import com.zhanglin.utils.IDutils;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

public class MyTest {
    @Test
    public void t(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog all = mapper.getAll();
        System.out.println(all.toString());
        sqlSession.close();
    }

    @Test
    public void a(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = new Blog();
        blog.setId(IDutils.getId());
        blog.setTitle("text");
        blog.setAuthor("zhanglin");
        blog.setCreateTime(new Date());
        blog.setViews(888);
        int add = mapper.add(blog);

        blog.setId(IDutils.getId());
        blog.setTitle("java");
        blog.setCreateTime(new Date());
        blog.setViews(9999);
        int add1 = mapper.add(blog);

        blog.setId(IDutils.getId());
        blog.setTitle("python");
        blog.setCreateTime(new Date());
        blog.setViews(6666);
        int add2 = mapper.add(blog);

        blog.setId(IDutils.getId());
        blog.setTitle("C");
        blog.setCreateTime(new Date());
        blog.setViews(7777);
        int add3 = mapper.add(blog);

        blog.setId(IDutils.getId());
        blog.setTitle("JS");
        blog.setCreateTime(new Date());
        blog.setViews(2222);
        int add4 = mapper.add(blog);

        if (add>0 && add1>0 && add2>0 && add3>0 && add4>0){
            System.out.println("全部添加成功");
        }
        sqlSession.close();
    }

    @Test
    public void quaryBlogByIf(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();
        map.put("title","text");
        map.put("author","zhanglin");
        List<Blog> blogs = mapper.quaryBlogByIf(map);
        for (Blog blog : blogs) {
            System.out.println(blog.toString());
        }
        sqlSession.close();
    }

    @Test
    public void quaryBlogPowerByChoose(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();
        map.put("demo",1);
        List<Blog> blogs = mapper.quaryBlogPowerByChoose(map);
        for (Blog blog : blogs) {
            System.out.println(blog.toString());
        }
        sqlSession.close();
    }

    @Test
    public void updateBlog(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();
        map.put("title","aaa");
        map.put("id","1");
        int i = mapper.updateBlog(map);
        if (i>0){
            System.out.println("更新成功");
        }
        sqlSession.close();
    }

    @Test
    public void selectBlogByforeach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        Map map = new HashMap();
        map.put("ids",ids);
        List<Blog> blogs = mapper.selectBlogByforeach(map);
        for (Blog blog : blogs) {
            System.out.println(blog.toString());
        }
        sqlSession.close();
    }
}
