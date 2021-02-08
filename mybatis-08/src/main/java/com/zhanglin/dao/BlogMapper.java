package com.zhanglin.dao;

import com.zhanglin.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    //新建测试
   public Blog getAll();

   public int add(Blog blog);

   public List<Blog> quaryBlogByIf(Map map);

   public List<Blog> quaryBlogPowerByChoose(Map map);
}
