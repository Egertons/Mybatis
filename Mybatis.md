# Mybatis

## 一、CRUD

### 1、	namespace(命名空间)

namespace中的包名要和  Dao/Mapper  接口的包名一致！

### 2、	select

选择，查询语句；

+ id ：就是对应的namespace中的方法名；
+ resultType ：sql语句执行的返回值；
+ parameterType ：对应参数类型；

+ 框架书写流程：

  + 编写接口；

    ```java
    public List<User> getUserList();
    ```

  + 编写xml配置（sql语句）；

    ```xml
    <mapper namespace="com.zhanglin.dao.UserMapper">
        <select id="getUserList" resultType="com.zhanglin.pojo.User">
            select * from mybatis.user;
        </select>
    </mapper>
    ```

  + 测试（使用sqlsession）；

    ```java
    @Test
        public void test1(){
            SqlSession sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User u : userList) {
                System.out.println(u.toString());
            }
            sqlSession.close();
        }
    ```

### 3、  Insert

如上

```xml
<insert id="insertUser" parameterType="com.zhanglin.pojo.User">
    insert into mybatis.user (id,name,pwd) values (#{id},#{name},#{pwd});
</insert>
```

> 注意：
>
> 最后需要提交事务才能将sql语句生效！

### 4、   Update

如上

```xml
<update id="updateUser" parameterType="com.zhanglin.pojo.User">
    update mybatis.user
    set name = #{name},pwd=#{pwd}
    where id = #{id};
</update>
```

> 注意：
>
> 最后需要提交事务才能将sql语句生效！

### 5、   Delete 

如上

```xml
<delete id="deleteUser" parameterType="int">
    delete from mybatis.user where id=#{id};
</delete>
```

> 注意：
>
> 最后需要提交事务才能将sql语句生效！



### 6、错误排查

+ 标签不要匹配错误。

+ 在mybatis的核心文件中有个挂载各个分sqlxml文件的选项

  + 如何写resource的话，其中各分级目录要用"/"分隔，不要用点“.”！！

+ 程序各个配置文件必须符合规范！

  + 具体可结合报错日志来看（从下往上看为佳）；

+ NullPointerException（空指针异常）

  + 可能是Mybatis工具类中在static里重新定义了事物工厂，如此，下面方法里肯定是会报错的。

    + 解决方法：在静态代码块中直接引用最初声明的静态变量。

  + 有可能是因为框架读取不到配置文件

    + 如果是这样的话有可能会伴随着报IOException；

    + 解决方法：

      + 将resources资源文件夹整体剪贴到和java以及text文件夹的同级目录；

      + 并且在pom.xml中写明指定路径的文件夹扫描配置（如下）

      + ```xml
        <build>
            <resources>
                <resource>
                    <directory>src/main/resources</directory>
                    <includes>
                        <include>**/*.properties</include>
                        <include>**/*.xml</include>
                        //这里可以写的更加灵活，实在不行就直接指定到文件名（肯定就好使了）
                        <include>mybatis-config.xml</include>
                        <include>usermapper.xml</include>
                    </includes>
                    <filtering>true</filtering>
                </resource>
                <resource>
                    <directory>src/main/java</directory>
                    <includes>
                        <include>**/*.properties</include>
                        <include>**/*.xml</include>
                    </includes>
                    <filtering>true</filtering>
                </resource>
            </resources>
        </build>
        ```

+ 中文乱码问题

  + 将xml文件中所有中文注释删除试试看
    + 应该是**编码**的问题

### 7、万能的Map

UserMapper.java

```java
//万能的Map
public int insertUser2(Map<String,Object> map);
```

userMapper.xml

```xml
<insert id="insertUser2" parameterType="map">
    //这里的values中的值可以随意写，重要的是要与实际Map集合中的键值对向匹配！
    insert into mybatis.user (id,name,pwd) values (#{userId},#{userName},#{passWord});
</insert>
```

Text.java

```java
//万能的Map
@Test
public void insertUser2(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userId",8);
    map.put("userName","kkkk");
    map.put("passWord","233333");

    int i = mapper.insertUser2(map);
    if (i>0){
        System.out.println("万能的Map插入成功！！");
    }
    sqlSession.commit();
    sqlSession.close();
}
```

**好处：**

> 以往在CRUD的操作中都需要传入对应的对象类型，在该类型的属性过多的情况下，而我们在大多数时候都不需要同时操纵大多数属性，
>
> 故而，为了操纵一个属性而new了一个全属性的对象是不划算的！
>
> Map则不受这些束缚。可以在”改“或”删“以及”查“的操作中简化步骤，降低风险。

**总结：**

> Map传递参数，直接在sql中取出key即可！
>
> 对象传递参数，直接在sql中取出对象的属性即可！
>
> 只有一个基本类型参数的情况下，可以直接在sql中取到！
>
> 总之：
>
> ​		多个参数用Map，或者**注解！**

### 8、模糊查询

1、Java代码执行的时候，传递通配符%value%

> 也就是在最后的测试类中传参之类的时候用字符串的形式将带有通配符的字符串传进去。
>
> 不常用、有点low、但很安全~

2、在sql拼接过程中使用通配符；

> 这里的安全是指，用户可以在外界用传**特定**的参数的形式来影响sql执行结果！！

## 二、配置解析（核心配置文件）

>  mybatis-config.xml

>  Mybatis的核心配置文件包含了会深深影响Mybatis行为的设置和属性信息。

### Configuration（配置）

#### 1、properties（属性）

- 方式一：

  - 我们可以将一些数据库的配置信息例如账号密码驱动信息等用键值对的形式写入properties文件中；
  - 用resource属性来读取properties文件用以加载相关配置信息。

- 方式二：

  - 可以直接在该标签内用<property>子标签来进行属性的配置工作。
  - 这样写就不需要引入外部文件了。

- 注意：

  - 这时数据源（dataSource）中的属性配置还是要写的，形式如下：

    - ```xml
      <dataSource type="POOLED">
          <property name="driver" value="${driver}"/>
          <property name="url" value="${url}"/>
          <property name="username" value="${username}"/>
          <property name="password" value="${password}"/>
      </dataSource>
      ```

#### 2、settings（设置）

> 这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 下表描述了设置中各项设置的含义、默认值等。

- cacheEnabled
  - 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。（默认为true）
- lazyLoadingEnabled
  - 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。（默认为false）
- logImpl
  - 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。（默认未设置，具体可查文档）
- mapUnderscoreToCamelCase
  - 开启自动转驼峰命名映射；
    - 允许数据库字段的经典命名规则（create_time）在开启该设置后自动与Javabean中的驼峰命名产生映射关系。

#### 3、typeAliases（类型别名）

- 类型别名是为Java类型设置一个短名称，它只与xml配置有关。

- 存在的意义在于用来减少类完全限定名的冗余！

- ```xml
  <typeAliases>
      <typeAlias type="com.zhanglin.pojo.User" alias="user"/>
  </typeAliases>
  ```

- 当这样配置时，别名可在任意地方使用！

- 也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean，比如：

- ```xml
  <typeAliases>
      <package name="com.zhanglin.pojo"/>
  </typeAliases>
  ```

- 每一个在包 `domain.blog` 中的 Java Bean，在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名。 比如 `domain.blog.Author` 的别名为 `author`；若有注解，则别名为其注解值。注解写法如下：

  - ```java
    @Alias("stu")
    public class Student {
        private int id;
        private String sex;
    }
    ```

- 另外，Java自身有一些常用的类型，如：int等，这些Mybatis都自动进行了配置，为了避免冲突，请设定自定义别名时加以规避。

#### 4、typeHandlers（类型处理器）

#### 5、objectFactory（对象工厂）

#### 6、plugins（插件）

- mybatis-generator-core
- mybatis-plus
- 通用mapper

#### 7、environments（环境配置）

> 可以同时配置多套环境，但同时只能使用一个环境配置。（通过default属性切换）

- environment（环境变量）
  - transactionManager（事务管理器）
    - 如果使用Spring等现代框架，这个完全没有必要配置，其默认为JDBC；
    - 如果使用一些老框架，有可能其需要将“事务管理器”设置为MANAGED；
  - dataSource（数据源）
    - 链接数据库的一系列配置选项
    - type属性：有三个选项，默认是POOLED（有池连接-高性能）、还有无池连接，和普通连接形式。

#### 8、databaseIdProvider（数据库厂商标识）

#### 9、mappers（映射器）

> 注册绑定我们的Mapper文件。

+ **注意点**：

  + 接口和它的Mapper文件必须同名！

  + 接口和它的Mapper配置文件必须在一个包下！

  + 以下几种方法进行配置时，绝对不能用两种方法分别把接口和xml配置文件同时都注册咯！

    + 否则一个CRUD都别想成功！（报错如下）

      ```file
      Type interface com.zhanglin.dao.UserMapper is already known to the MapperRegistry.
      ```

+ 方式一：

  + ```xml
    <!-- 使用相对于类路径的资源引用 -->
    <mappers>
      <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
      <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
      <mapper resource="org/mybatis/builder/PostMapper.xml"/>
    </mappers>
    ```

+ 方式二：

  + ```xml
    <!-- 使用映射器接口实现类的完全限定类名 -->
    <mappers>
      <mapper class="org.mybatis.builder.AuthorMapper"/>
      <mapper class="org.mybatis.builder.BlogMapper"/>
      <mapper class="org.mybatis.builder.PostMapper"/>
    </mappers>
    ```

  + 注意点：

    + 接口和它的Mapper文件必须同名！
    + 接口和它的Mapper配置文件（建议）放在一个包下！
      + 也可以放在资源文件目录下（在resoures目录中建立同级目录结构）

+ 方式三：

  + ```xml
    <!-- 将包内的映射器接口实现全部注册为映射器 -->
    <mappers>
      <package name="org.mybatis.builder"/>
    </mappers>
    ```


### 生命周期和作用域

> 不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

#### SqlSessionFactoryBuilder

+ 一旦创建了SqlSessionFactory，就不需要它了；
+ 局部变量

#### SqlSessionFactory

+ 可以理解为 ：数据库连接池；
+ 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
+ 有很多方法可以做到，**最简单的就是使用单例模式或者静态单例模式。**

#### SqlSession

+ 可以理解为 ：连接到连接池中的一个请求；
+ 因为它并不是线程安全的；故而，每次操纵完成后，**需要关闭！**

## 三、ResultMap

**当属性名和数据库字段名不一致时：**

### 解决方法：

+ 起别名：

  + ```xml
    <select id="getUserList" resultType="com.zhanglin.pojo.User">
        select id,name,pwd as password from mybatis.user;
    </select>
    ```

  + 优点：

    + 简单、暴力、好用；

  + 缺点：

    + LOW！

+ ResultMap（结果集映射）

  > `resultMap` 元素是 MyBatis 中最重要最强大的元素。
  >
  > ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。

  + 是Mapper.xml中Select标签中的一个属性，用以配置接口方法的返回值类型；

  + 在select中resultMap指定本配置文件中自己的一个标签配置；

  + ```xml
    <select id="getUserLike" resultMap="demo">
        select * from mybatis.user where name like "%"#{a}"%";
    </select>
    ```

  + ```xml
    <resultMap id="demo" type="com.zhanglin.pojo.User">//这里的type如果有别名的话可以写别名;
        //这里的column对应的是数据库中的字段，property对应Java Bean中的属性；
        <result column="pwd" property="password"/>
    </resultMap>
    ```

  + 这里的resultMap标签中只写“有区别”的字段映射就可以了，当然也可以把所有属性都写一遍，但没啥用；



### 注意：

+ 该标签属性id：
  + 作为一个ID结果，标记出该字段属性为id，用以区别开其他的字段属性，**用以提高整体性能**！
  + 当然，正常用result写也行。



## 四、日志

### 4.1 日志工厂

> 如果一个数据库操作，出现了异常，需要进行排错。那么**日志**就是最好的助手！！
>
> 之前：sout、debug....
>
> 现在：日志工厂！

在Mybatis的主配置文件中，<setting>标签中可设置其内置的日志工厂类型；

+ logImpl（具体使用哪一个日志实现，在设置中设定，依项目而具体设置）

  + SLF4J 

  + LOG4J【重点】

    > 什么是LOG4J ：
    >
    > Log4j是**Apache**的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是**控制台**、文件、**GUI**组件，甚至是套接口服务器
    >
    > 我们也可以控制每一条日志的输出格式；
    >
    > 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。
    >
    > 最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

    + 先导包；

      ```xml
      <dependency>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
          <version>1.2.17</version>
      </dependency>
      ```

    + 建立log4j.properties配置文件

      ```properties
      ### set log levels ###
      log4j.rootLogger=DEBUG
      
      ### direct log messages to stdout ###
      log4j.appender.A1=org.apache.log4j.ConsoleAppender
      log4j.appender.A1.Target=System.out
      log4j.appender.A1.layout=org.apache.log4j.PatternLayout
      log4j.appender.A1.layout.ConversionPattern=%-2p %m%n
      
      ### direct messages to file framework.log ###
      log4j.appender.A2=org.apache.log4j.DailyRollingFileAppender
      ##log4j.appender.A2.File=D:/logs/resmanm.log
      log4j.appender.A2.DatePattern='.'yyyy-MM-dd
      log4j.appender.A2.layout=org.apache.log4j.PatternLayout
      log4j.appender.A2.layout.ConversionPattern=%-5p(%10c{1}) %m%n
      
      ### application log config ###
      #log4j.logger.com.linkage=ERROR,A2
      log4j.logger.com.ch1=DEBUG,A1,A2
      ##log4j.logger.org.quartz.impl.StdSchedulerFactory=DEBUG,A1,A2
      ```

    + Log4j的使用；

      + 导包；

      + 建立静态对象：

        ```java
      static Logger logger = Logger.getLogger(MyTest.class);
        ```

      + 在不同的事物等级下使用logger对象的不同方法；（日志级别）

        ```java
        logger.info("info:进入了Log4j！");
        logger.debug("debug:进入了Log4j！");
        logger.error("error:进入了Log4j！");
        ```
  
      + 
  
  + LOG4J2
  
  + JDK_LOGGING
  
  + COMMONS_LOGGING
  
  + STDOUT_LOGGING【重点】
  
    > 标准日志输出
  
    ```file
    //例如：
    "C:\Program Files\Java\jdk-14.0.2\bin\java.exe" -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:C:\IDEA\IntelliJ IDEA 2020.1.4\lib\idea_rt.jar=54444:C:\IDEA\IntelliJ IDEA 2020.1.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\IDEA\IntelliJ IDEA 2020.1.4\lib\idea_rt.jar;C:\IDEA\IntelliJ IDEA 2020.1.4\plugins\junit\lib\junit5-rt.jar;C:\IDEA\IntelliJ IDEA 2020.1.4\plugins\junit\lib\junit-rt.jar;C:\IDEA_Data\Mybatis\mybatis-04\target\test-classes;C:\IDEA_Data\Mybatis\mybatis-04\target\classes;C:\Users\egert\.m2\repository\junit\junit\4.13.1\junit-4.13.1.jar;C:\Users\egert\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\egert\.m2\repository\mysql\mysql-connector-java\8.0.23\mysql-connector-java-8.0.23.jar;C:\Users\egert\.m2\repository\com\google\protobuf\protobuf-java\3.11.4\protobuf-java-3.11.4.jar;C:\Users\egert\.m2\repository\org\mybatis\mybatis\3.5.6\mybatis-3.5.6.jar" com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit4 com.zhanglin.dao.MyTest,getAllUser
    Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImpl' adapter.
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    //从这里开始需要关注：
    Opening JDBC Connection
    Created connection 1455695758.
    Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@56c4278e]
    ==>  Preparing: select * from mybatis.user;
    ==> Parameters: 
    <==    Columns: id, name, pwd
    <==        Row: 1, 张霖, zhanglin1111
    <==        Row: 2, 李四, 767sgdc
    <==        Row: 3, 薛五, cjwie9hfw9
    <==        Row: 4, oo, 999999999
    <==        Row: 5, xi, Phut_Hon
  <==        Row: 6, 李五, whsauiedg
    <==        Row: 7, Surface, newMap
    <==        Row: 8, kkkk, 233333
    <==      Total: 8
    User{id=1, name='张霖', pwd='zhanglin1111'}
    User{id=2, name='李四', pwd='767sgdc'}
  User{id=3, name='薛五', pwd='cjwie9hfw9'}
    User{id=4, name='oo', pwd='999999999'}
    User{id=5, name='xi', pwd='Phut_Hon'}
    User{id=6, name='李五', pwd='whsauiedg'}
    User{id=7, name='Surface', pwd='newMap'}
    User{id=8, name='kkkk', pwd='233333'}
    Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@56c4278e]
    Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@56c4278e]
    Returned connection 1455695758 to pool.
    
    Process finished with exit code 0
    ```
  
    - **问题点：**
      - Mybatis：Reader entry: ���� 4（控制台输出乱码）--不重要
        - 原因：mybatis-config.xml里面配置了包的别名引发的，把别名中的包扫描去掉就好了！
        - 注意：这里并不是包扫描机制和自定义别名冲突，只用包扫描也出乱码。
        - 暂不清楚原理是什么。
  
  + NO_LOGGING

## 五、分页

### 5.1 传统Limit实现分页：

```sql
select * from user limit startIndex,pageSize;
```

### 5.2 Mybatis实现分页：

+ 接口

  ```java
  public List<User> getUserByLimit(Map<String,Object> map);
  ```

+ Mapper.xml

  ```xml
  <select id="getUserByLimit" resultType="user" parameterType="map">
      select * from mybatis.user limit #{startIndex},#{fill};
  </select>
  ```

+ 测试

  ```java
  @Test
  public void getUserByLimit(){
      SqlSession sqlSession = MybatisUtils.getSqlSession();
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("startIndex",0);
      map.put("fill",2);
      List<User> userByLimit = mapper.getUserByLimit(map);
      for (User u : userByLimit) {
          System.out.println(u.toString());
      }
      sqlSession.close();
  }
  ```

### 5.3 RowBounds分页

> 相对较老的技术，通过绝对的面向对象来实现分页查询！

+ 接口

  ```java
  //RowBounds
  public List<User> getUserByRowBounds();
  ```

+ Mapper.xml

  ```xml
  <select id="getUserByRowBounds" resultMap="demo">
      select * from user;
  </select>
  ```

+ 测试

  ```java
  @Test
  public void getUserByRowBounds(){
      SqlSession sqlSession = MybatisUtils.getSqlSession();
      RowBounds rowBounds = new RowBounds(1, 2);
      //通过Java代码层面实现分页！
      List<User> userList = sqlSession.selectList("com.zhanglin.dao.UserMapper.getUserByRowBounds",null,rowBounds);
      for (User u: userList) {
          System.out.println(u.toString());
      }
      sqlSession.close();
  }
  ```

### 5.4分页插件

![image-20210204145505813](C:%5CUsers%5Cegert%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20210204145505813.png)

## 六、使用注解开发

> 可以直接在Dao层的接口方法上使用相应的注解，把sql直接写到注解的参数列表里。
>
> 剩下的步骤除了不需要写xml之外，和写xml没有任何区别！

**注意：**

1. 如果失败，可以尝试在Mybatis的核心配置文件中绑定Dao层接口；（如果已经绑定了对应的配置文件一般不用绑接口）

   ```xml
   <mappers>
       <mapper class="com.zhanglin.dao.UserMapper"/>
   </mappers>
   ```

2. 注解开发虽简单，但是复杂点的情况就不行了；

   1. 例如，不支持**resultMap**；

3. 在工具类中用sqlSessionFactory创建SqlSession时，可以直接用形参设置**自动提交事务**！

   ```java
   public static SqlSession getSqlSession(){
       return sqlSessionFactory.openSession(true);
   }
   ```

4. 在接口抽象方法的参数多于两个时，写@Param注解，**绝对的规范！！**

   > 引用对象类型不需要写@Param

   ```java
   @Select("select * from user where id = #{id}")
   public List<User> getUserById(@Param("id") int id,@Param("name") String name);
   ```

5. 总结：

   + 简单SQL用注解（更加简单方便）
   + 复杂SQL必须使用XML配置，利人利己！

本质：反射机制实现；

底层：动态代理！



## 七、Mybatis执行流程





![image-20210204195858067](C:%5CUsers%5Cegert%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20210204195858067.png)





## 八、零七八碎的知识点（debug）

1. #{} 和 ${}的区别

   答 ：他俩在功能上都一样；没啥区别

   但是，优先用**#{}**写法；

   优点：

   + 语法更加现代、规范；
   + 有效的防止了SQL注入（更安全）；

   另：

   + Mybatis排序时使用order by 动态参数时需要注意，使用${}而不用#{}；
   
2. 慢SQL

   >  一般来说，生产级的sql代码都要经过层层优化，对于大型数据库来说，简单暴力（不加索引）的查询是一种灾难，非常耗时！

3. 当出现类型转化问题时：

   很有可能是mapper.xml配置中把resultType和parameterType写反了！！！



## 九、Lombok

> Lombok 是一种 Java™ 实用工具，可用来帮助开发人员消除 Java 的冗长，尤其是对于简单的 Java 对象（POJO）。它通过注解实现这一目的。

使用：

1. 在IDEA中安装Lombok插件；

2. 在项目中导入lombok的依赖；

   ```xml
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.10</version>
   </dependency>
   ```

3. @Data（类注解）

   可以在POJO类中自动生成无参构造、get、set、toString、hashcode、equals等；

   

4. @AllArgsConstructor

   自动生成全参数构造；

   并自动屏蔽@Data生成的无参构造；

   

5. @NoArgsConstructor

   自动生成无参构造；

   

6. 全部支持的注解列表：

   **@Getter and @Setter**（既是类注解也是方法注解）
   @FieldNameConstants
   **@ToString**
   **@EqualsAndHashCode**
   **@AllArgsConstructor**, @RequiredArgsConstructor and **@NoArgsConstructor**
   @Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
   **@Data**
   @Builder
   @SuperBuilder
   @Singular
   @Delegate
   @Value
   @Accessors
   @Wither
   @With
   @SneakyThrows
   @val
   @var
   experimental @var
   @UtilityClass

## 十、多对一的处理：

### **测试环境搭建：**

1. 导入lombok；
2. 新建实体类；
3. 建立实体类对应的Mapper接口；
4. 建立每个接口对应的Mapper.xml配置文件；
5. 在Mybatis的核心配置文件中绑定注册其对应的xml配置文件或者Mapper接口；
6. 测试成功与否；



### 按照查询嵌套处理：

```xml
<select id="getStudentWithT" resultMap="demo">
    select * from student;
</select>
<resultMap id="demo" type="student">
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
</resultMap>
<select id="getTeacher" resultType="teacher">
    select * from teacher where id = #{id};
</select>
```



### 按照结果嵌套处理：

```xml
<select id="getStudentWithT2" resultMap="demo2">
    select s.id sid,s.name sname,t.name tname from student s,teacher t where s.tid = t.id;
</select>
<resultMap id="demo2" type="student">
    <result property="id" column="sid"/>
    <result property="name" column="sname"/>
    <association property="teacher" javaType="Teacher">
        <result property="name" column="tname"/>
    </association>
</resultMap>
```

## 十一、一对多处理

### 第一种形式：

+ 接口

  ```java
  //获取指定老师下的所有学生及老师的信息；
  public Teacher getDesignatedTeacher(@Param("tid") int id);
  ```

+ xml

  ```xml
  <select id="getAllTeacher" resultType="teacher">
      select * from teacher;
  </select>
  
  <select id="getDesignatedTeacher" resultMap="demo">
      select s.id sid,s.name sname, t.name tname,t.id tid from student s,teacher t where s.tid = tid and tid=#{tid};
  </select>
  <resultMap id="demo" type="Teacher">
      <result property="id" column="tid"/>
      <result property="name" column="tname"/>
      <collection property="students" ofType="Student">
          <result property="id" column="sid"/>
          <result property="name" column="sname"/>
          <result property="tid" column="tid"/>
      </collection>
  </resultMap>
  ```

+ Teacher实体类

  ```java
  @Data
  public class Teacher {
      private int id;
      private String name;
      private List<Student> students;
  }
  ```

### 第二种形式：

+ 接口

  ```java
  public Teacher getDesignatedTeacher2(@Param("tid") int id);
  ```

+ xml

  ```xml
  <select id="getDesignatedTeacher2" resultMap="demo2">
      select * from teacher where id = #{tid};
  </select>
  <resultMap id="demo2" type="Teacher">
      <collection property="students" column="id" javaType="ArrayList" ofType="Student" select="a"/>
  </resultMap>
  <select id="a" resultType="Student">
      select * from student where tid = #{id};
  </select>
  ```

+ Teacher实体类同上

### 小结：

1. 关联 - association 【多对一】
2. 集合 - collection 【一对多】
3. javaType  &  ofType
   1. javaType用来指定实体类中“直接”属性的类型；
   2. ofType用来指定映射到List或者集合中的pojo类型，泛型中的约束类型！
4. 注意点：
   1. 保证SQL的可读性，尽量保证通俗易懂。
   2. 注意一对多和多对一中，属性名和字段名的问题！
   3. 如果问题不好排查，可以使用日志输出，建议使用Log4J；



## 十二、动态SQL

**什么是动态SQL：**

​		动态SQL就是指根据不同的条件生成不同的SQL语句。

> 动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。
>
> 使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。
>
> 如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。



### if

> 这其中where 1=1 是为了让SQL语句保有where条件查询的前提情况下，永久为真，并不影响后续的**动态SQL**；
>
> 正常情况下都是用<where>标签来实现；

xml：

```xml
<select id="quaryBlogByIf" parameterType="map" resultType="blog">
    select * from blog where 1=1
    <if test="title!=null">
        and title = #{title}
    </if>
    <if test="author!=null">
        and author = #{author}
    </if>
</select>
```

测试：

```java
@Test
public void quaryBlogByIf(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    Map map = new HashMap();
    map.put("title","text");
    List<Blog> blogs = mapper.quaryBlogByIf(map);
    for (Blog blog : blogs) {
        System.out.println(blog.toString());
    }
    sqlSession.close();
}
```

### choose (when, otherwise)

> 其实就是Java中的switch / if else 的一个变种语法。
>
> **执行起来也和Java中的if是一样的，只要满足一个when时，后续的所有when或者otherwise都不会去执行。**
>
> ​		**这在SQL层面是需要去注意的！！**

```xml
<select id="quaryBlogPowerByChoose" resultType="blog" parameterType="map">
    select * from blog where 100=100
    <choose>
        <when test="demo == 1">
            and title = 'text'
        </when>
        <when test="demo == 2">
            and author = 'zhanglin'
        </when>
    </choose>
</select>
```

### trim (where, set)

- where

  该标签用于代替上述例子中where1=1这样并不规范的写法；

  *where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

- set

  该标签在update时用于动态的设定更新值。

  示例：

  ```xml
  <update id="updateBlog" parameterType="map">
      update blog
      <set>
          <if test="title != null">
              title = #{title},
          </if>
      </set>
      where id=#{id};
  </update>
  ```

- **trim**

  该标签可以**高度定制化**替换范围以及替换内容；

  以上的where和set就是基于trim实现的。

### foreach

+ 接口

  ```java
  //xml-foreach尝试
  public List<Blog> selectBlogByforeach(Map map);
  ```

+ xml

  ```xml
  <select id="selectBlogByforeach" resultType="blog" parameterType="map">
      select * from blog
      <where>
          <foreach collection="ids" item="id" open="and (" close=")" separator="or">
              id = #{id}
          </foreach>
      </where>
      ;
  </select>
  ```

+ 测试

  ```java
  @Test
  public void selectBlogByforeach(){
      SqlSession sqlSession = MybatisUtils.getSqlSession();
      BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
      ArrayList<Integer> ids = new ArrayList<Integer>();
      ids.add(1);
      ids.add(2);
      Map map = new HashMap();
      map.put("ids",ids);
      List<Blog> blogs = mapper.selectBlogByforeach(map);
      for (Blog blog : blogs) {
          System.out.println(blog.toString());
      }
      sqlSession.close();
  }
  ```

+ 讲解

  > 重点在于xml的写法

  + collection是指从控制层传过来的一个集合，一般是List，从中来提取使用foreach所需要的遍历的集合；
  + id ：这个是指最后要显式显示在SQL中的字段，可以理解为SQL**拼接**中的一部分；
  + open、close、separator分别指的是拼接中的起始字段、终止字段、以及**间隔字段**；
  + 在<foreach>标签体中来进行属性的赋值；



### SQL片段

> 有的时候，我们可能会将一些公共的部分抽取出来，方便复用。

在xml的mapper标签中有一个<sql>的子标签；

可以放入随意的代码片段；

在需要复用的时候，用<include>标签来进行复用就可以了；

示例如下：

```xml
<update id="updateBlog" parameterType="map">
    update blog
    <set>
        <include refid="text"/>
    </set>
    where id=#{id};
</update>

<sql id="text">
    <if test="title != null">
        title = #{title},
    </if>
</sql>
```

SQL片段使用注意事项：

+ 最好基于**单表**来定义SQL片段；
  + 也就是说，SQL片段不易过于复杂；
  + 过于复杂的SQL片段本身也不宜于代码的重用；
+ 不要包含<where>标签；
  + 该标签的可重用性太低，定制性太高；片段化意义不大；

### 注意点：

+ 在有动态SQL标签的情况下，主SQL语句**不做闭合处理**（就是不写最后的；）
  + 否则后面的动态SQL框架读不到。
+ 所谓的动态SQL，本质上还是SQL语句，只是我们可以在SQL层面，去执行一个逻辑代码；
+ 动态SQL就是在拼接SQL语句，我们只要保证SQL的正确性，按照SQL的格式，去排列组合就可以了。
+ 建议：
  + **先在Mysql中写出完整的SQL语句**，再对应的去修改成为我们的动态SQL通用实现即可！！





## 十三、缓存机制

### 1、Mybatis缓存

+ Mybatis包含一个非常强大的查询缓存特性，它可以非常方便地定制和配置缓存。缓存可以**极大的提升查询效率**。
+ Mybatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**
  + 默认情况下，一级缓存自动开启。（SqlSession级别的缓存，也称为本地缓存）
  + 二级缓存需要手动开启和配置，他是基于namespace级别的缓存。
  + 为了提高扩展性，Mybatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存。
+ 执行顺序：
  + 查询时先看二级缓存中有没有相应的数据；
  + 再看剩余的一级缓存中有没有；
  + 如果都查不到的话，就按照正常查询数据库的流程执行；

### 2、一级缓存

+ 一级缓存也叫本地缓存 ：SqlSession

  + 与数据库同一次会话期间查询到的数据会放在本地缓存中。
  + 以后如果需要获取相同的数据，直接从缓存中取，没必要再去查询数据库；

+ 调试缓存的过程中：

  + 开启Mybatis的日志功能；
  + 可以测试在一次Session过程中查询两次**相同的**记录；（以验证观点）

+ 缓存失效的情况：

  + 查询不同的东西；

  + **增删改**等行为会刷新一级缓存；

    ```java
    @Test
    public void queryUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.queryUserById(1);
        System.out.println(user.toString());
        System.out.println("==========================");
        Map map = new HashMap();
        map.put("id",2);
        map.put("pwd","11111");
        int i = mapper.updateUser(map);
        if (i>0)
            System.out.println("更新成功");
        User user1 = mapper.queryUserById(1);
        System.out.println(user1.toString());
        System.out.println(user==user1);
        sqlSession.close();
    }
    ```

    ```file
    "C:\Program Files\Java\jdk-14.0.2\bin\java.exe" -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:C:\IDEA\IntelliJ IDEA 2020.1.4\lib\idea_rt.jar=58016:C:\IDEA\IntelliJ IDEA 2020.1.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\IDEA\IntelliJ IDEA 2020.1.4\lib\idea_rt.jar;C:\IDEA\IntelliJ IDEA 2020.1.4\plugins\junit\lib\junit5-rt.jar;C:\IDEA\IntelliJ IDEA 2020.1.4\plugins\junit\lib\junit-rt.jar;C:\IDEA_Data\Mybatis\mybatis-09\target\test-classes;C:\IDEA_Data\Mybatis\mybatis-09\target\classes;C:\Users\egert\.m2\repository\org\projectlombok\lombok\1.18.10\lombok-1.18.10.jar;C:\Users\egert\.m2\repository\junit\junit\4.13.1\junit-4.13.1.jar;C:\Users\egert\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\egert\.m2\repository\mysql\mysql-connector-java\8.0.23\mysql-connector-java-8.0.23.jar;C:\Users\egert\.m2\repository\com\google\protobuf\protobuf-java\3.11.4\protobuf-java-3.11.4.jar;C:\Users\egert\.m2\repository\org\mybatis\mybatis\3.5.6\mybatis-3.5.6.jar" com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit4 com.zhanglin.dao.MyTest,queryUserById
    Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImpl' adapter.
    Class not found: org.jboss.vfs.VFS
    JBoss 6 VFS API is not available in this environment.
    Class not found: org.jboss.vfs.VirtualFile
    VFS implementation org.apache.ibatis.io.JBoss6VFS is not valid in this environment.
    Using VFS adapter org.apache.ibatis.io.DefaultVFS
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/pojo
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/pojo
    Reader entry: User.class
    Listing file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/pojo
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/pojo/User.class
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/pojo/User.class
    Reader entry: ����   : U
    Checking to see if class com.zhanglin.pojo.User matches criteria [is assignable to Object]
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    PooledDataSource forcefully closed/removed all connections.
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/test-classes/com/zhanglin/dao
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/test-classes/com/zhanglin/dao
    Reader entry: MyTest.class
    Listing file:/C:/IDEA_Data/Mybatis/mybatis-09/target/test-classes/com/zhanglin/dao
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/test-classes/com/zhanglin/dao/MyTest.class
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/test-classes/com/zhanglin/dao/MyTest.class
    Reader entry: ����   : e
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao
    Reader entry: UserMapper.class
    Reader entry: UserMapper.xml
    Listing file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao/UserMapper.class
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao/UserMapper.class
    Reader entry: ����   :   com/zhanglin/dao/UserMapper  java/lang/Object 
    Find JAR URL: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao/UserMapper.xml
    Not a JAR: file:/C:/IDEA_Data/Mybatis/mybatis-09/target/classes/com/zhanglin/dao/UserMapper.xml
    Reader entry: <?xml version="1.0" encoding="UTF-8" ?>
    Checking to see if class com.zhanglin.dao.MyTest matches criteria [is assignable to Object]
    Checking to see if class com.zhanglin.dao.UserMapper matches criteria [is assignable to Object]
    Opening JDBC Connection
    Created connection 872669868.
    ==>  Preparing: select * from user where id = ?;
    ==> Parameters: 1(Integer)
    <==    Columns: id, name, pwd
    <==        Row: 1, 张霖, zhanglin1111
    <==      Total: 1
    User(id=1, name=张霖, pwd=zhanglin1111)
    ==========================
    ==>  Preparing: update user set pwd = ? where id=?;
    ==> Parameters: 11111(String), 2(Integer)
    <==    Updates: 1
    更新成功
    ==>  Preparing: select * from user where id = ?;
    ==> Parameters: 1(Integer)
    <==    Columns: id, name, pwd
    <==        Row: 1, 张霖, zhanglin1111
    <==      Total: 1
    User(id=1, name=张霖, pwd=zhanglin1111)
    false
    Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@3403e2ac]
    Returned connection 872669868 to pool.
    
    Process finished with exit code 0
    ```

  + 查询不同的Mapper.xml（这样做连二级缓存都会被刷新）

  + 手动清理缓存

    ```java
    sqlSession.clearCache();//手动清理缓存
    ```

+ 小知识：
  + 一级缓存默认是开启的且无法关闭~
  + 只在一次SqlSession中有效，也就是从拿到连接到关闭连接这区间段内！



### 3、二级缓存

> 默认只开启一级缓存；
>
> 如果想要开启二级缓存需要在Mapper.xml中增加<cache/>标签就可以了！（遵循下述步骤）

+ 二级缓存又称为全局缓存，由于一级缓存作用域太低了，所以诞生了二级缓存

+ 基于namespace级别的缓存，一个命名空间，对应一个二级缓存；

+ 工作机制：

  + 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
  + 如果当前会话关闭了，这个会话对应的一级缓存就被自动清理了；但是现在，会话关闭后，一级缓存中的数据会被保存到二级缓存中；
  + 新的会话查询信息，就可以从二级缓存中获取内容；
  + 不同的mapper查出的数据会放在自己对应的缓存（map）中；

+ 在Mapper.xml文件中类似于select的标签都可以针对性的关闭二级缓存；

  ```xml
  <select id="queryUserById" resultType="user" useCache="false">
      select * from user where id = #{id};
  </select>
  ```

+ 小结：
  + 只要开启了二级缓存，在同一个Mapper下就有效；
  + 所有的数据都会先存放在一级缓存中；
  + 只有当会话提交，或者关闭的时候才会提交到二级缓存中；

**在<cache/>标签中有一些可以配置的东西：**

+ eviction（清除策略）

  + LRU（默认）

    > 最近最少使用：移除最长时间不被使用的对象。

  + FIFO

    > 先进先出：按照对象进入缓存的顺序来移除它们。

  + SOFT（软引用-不常用）

  + WEAK（弱引用-不常用）

+ flushInterval

  + 刷新时间

    > Eg：flushInterval=“60000” 意为每隔60秒刷新一次；

+ size

  + 表示最多可以存储多少个结果对象或列表；

+ Eg

  ```xml
  <cache eviction="FIFO" flushInterval="60000" size="512" readOnly="true"/>
  ```

  



**步骤：**

1. 开启全局缓存

   在mybatis-config.xml中

   ```xml
   <settings>
   	<setting name="cacheEnabled" value="true"/>
   </settings>
   ```

2. 在Mapper.xml中开启本Mapper的二级缓存。



问题：

+ 如果不写cache标签的清除策略的话，会报错（实体类序列化问题）；
  + 解决办法1：显式的开启二级缓存清除策略；
  + 解决办法2：将相关实体类实现序列化接口（Serializable）；



















