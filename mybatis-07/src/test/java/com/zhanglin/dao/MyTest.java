package com.zhanglin.dao;

import com.zhanglin.pojo.Teacher;
import com.zhanglin.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {

    @Test
    public void getAllTeacher(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> allTeacher = mapper.getAllTeacher();
        for (Teacher teacher : allTeacher) {
            System.out.println(teacher.toString());
        }
        sqlSession.close();
    }

    @Test
    public void getDesignatedTeacher(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher designatedTeacher = mapper.getDesignatedTeacher(1);
        System.out.println(designatedTeacher.toString());
        sqlSession.close();
    }

    @Test
    public void getDesignatedTeacher2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher designatedTeacher2 = mapper.getDesignatedTeacher2(1);
        System.out.println(designatedTeacher2.toString());
        sqlSession.close();
    }
}
