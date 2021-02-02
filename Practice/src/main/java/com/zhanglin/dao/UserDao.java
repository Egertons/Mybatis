package com.zhanglin.dao;

import com.zhanglin.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public List<User> getUserList();

    //附条件查询
    public User getUserById(int id);

    //添加用户
    public int insertUser(User user);

    //修改用户
    public int updateUser(User user);

    //万能的Map
    public int addUserforMap(Map<String,Object> map);

    //模糊查询
    public List<User> mohuQuary(String msg);
}
