package com.zhanglin.dao;

import com.zhanglin.pojo.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper {
    public List<Teacher> getAllTeacher();

    //获取指定老师下的所有学生及老师的信息；
    public Teacher getDesignatedTeacher(@Param("tid") int id);
}
