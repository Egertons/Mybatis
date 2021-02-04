package com.zhanglin.dao;

import com.zhanglin.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //查询全部用户
    public List<User> getUserList();

    //分页
    public List<User> getUserByLimit(Map<String,Object> map);

    //RowBounds
    public List<User> getUserByRowBounds();
}
