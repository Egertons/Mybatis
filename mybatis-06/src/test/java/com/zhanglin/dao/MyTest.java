package com.zhanglin.dao;

import com.zhanglin.pojo.Student;
import com.zhanglin.pojo.Teacher;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {

    @Test
    public void getTeacherList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> teacherList = mapper.getTeacherList();
        for (Teacher teacher: teacherList) {
            System.out.println(teacher.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getStudentWithT(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentWithT = mapper.getStudentWithT();
        for (Student student : studentWithT) {
            System.out.println(student.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getStudentWithT2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentWithT2 = mapper.getStudentWithT2();
        for (Student student : studentWithT2) {
            System.out.println(student.toString());
        }
        sqlSession.close();
    }
}
