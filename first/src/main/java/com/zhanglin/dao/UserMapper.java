package com.zhanglin.dao;

import com.zhanglin.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //查询全部用户
    public List<User> getUserList();

    //根据id查询用户
    public User getUserById(int id);

    //insert一个用户
    public int insertUser(User user);

    //修改一个用户
    public int updateUser(User user);

    //删除一个用户
    public int deleteUser(int id);

    //万能的Map
    public int insertUser2(Map<String,Object> map);

    //模糊查询
    public List<User> getUserLike(String a);
}
