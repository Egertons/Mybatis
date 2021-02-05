package com.zhanglin.dao;

import com.zhanglin.pojo.Student;

import java.util.List;

public interface StudentMapper {
    //查询所有的学生信息，以及其对应的教师信息。
    public List<Student> getStudentWithT();

    public List<Student> getStudentWithT2();
}
