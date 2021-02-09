package com.zhanglin.dao;

import com.zhanglin.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    public User queryUserById(@Param("id") int id);

    public int updateUser(Map map);
}
