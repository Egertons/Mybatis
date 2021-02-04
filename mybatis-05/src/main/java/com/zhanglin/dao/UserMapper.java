package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    //查询全部用户
    public List<User> getUserList();

    //使用注解开发
    @Select("select * from user")
    public List<User> getUserListByAnno();

    //在此处参数多于两个时，写@Param注解，绝对的规范。
    @Select("select * from user where id = #{id}")
    public List<User> getUserById(@Param("id") int id,@Param("name") String name);
}
