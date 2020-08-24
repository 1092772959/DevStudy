# Mybatis学习笔记

## 一、 概念

mybatis是一个持久层的框架，是apache下的顶级项目。

mybatis让程序员将主要精力放在sql上，通过mybatis提供的映射方式，自由灵活生成（半自动化，大部分需要程序员编写sql）满足需要sql语句。

mybatis可以向preparedStatement中的输入参数自动进行输入映射，将查询结果集灵活映射成java对象（输入映射和输出映射）。



### 层次概念

SqlMapperConfig.xml

（是mybatis的全局配置文件）

作用：

配置数据源、事务等mybatis运行环境

配置映射文件（配置sql语句）：mapper.xml 即映射文件



SqlSessionFactory 会话工厂

作用：创建SqlSession



SqlSession 会话

一个面向用户（程序员）的接口

作用：发出增删改查sql，操作数据库

线程不安全！因此应放在方法体中作为局部变量使用。



Executor 执行器

一个接口

有两个实现：基本执行器；缓存执行器

作用：SqlSession内部通过执行器操作数据库



mapped statement(底层封装对象)

作用：对操作数据库存储封装，包括sql语句，输入参数、输出结果类型



## 二、实践

### 1. 环境

```maven
<dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.0</version>
 </dependency>
```



application.yml 指定映射类的包

```yml
mybatis:
  configuration:
    map-underscore-to-camel-case: true          #驼峰
  mapper-locations:  classpath:mapper/*.xml     #mapper xml
  type-aliases-package: com.shu.icpc.entity     #实体类包
```



全局配置文件

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    
    <!--加载映射文件-->
    <mappers>
    	<mapper resource="sql/User.xml"></mapper>
    </mappers>
</configuration>
```



开启扫描

或手动一个个加@Mapper注解

```java
@MapperScan("com.shu.icpc.dao")         //扫描mybatis bean
```



### 2. 基本使用

映射文件xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.shu.icpc.dao.StudentDao">
    <!--    <resultMap id="" type="com.vue.demo.entity.Student">-->
    <!--        <result column="id" jdbcType="INTEGER" property="id" />-->
    <!--        <result column="userName" jdbcType="VARCHAR" property="userName" />-->
    <!--        <result column="passWord" jdbcType="VARCHAR" property="passWord" />-->
    <!--        <result column="realName" jdbcType="VARCHAR" property="realName" />-->
    <!--    </resultMap>-->
    <select id="findAll" resultType="Student">
        select id, stu_id, stu_name, family_name, first_name, school_id, college, major, enroll_year,
            phone, email, sex, size, check_status
        from student
    </select>

    <select id="findById" parameterType="int" resultType= "Student">
        select *
        from student
        where id = #{id}
    </select>

    <select id="findByPhone" parameterType="String" resultType= "Student">
        select *
        from student
        where phone = #{phone}
    </select>

    <select id="findBySchoolId" parameterType="int" resultType= "Student">
        select id, stu_id, stu_name, family_name, first_name, school_id, college, major, enroll_year,
            phone, email, sex, size, check_status
        from student
        where school_id = #{schoolId}
    </select>

    <select id="findByEmail" resultType="Student">
        select *
        from student
        where email = #{email}
    </select>
    
    <select id="findByName" resultType = "Student">
    	select * 
        from student
        where name like '%${name}%'
    </select>

    <update id="update" parameterType="Student">
        update student
        <trim prefix="set" suffixOverrides=",">
            <if test="stuId!=null">stu_id = #{stuId},</if>
            <if test="familyName!=null">family_name = #{familyName},</if>
            <if test="firstName!=null">first_name=#{firstName},</if>
            <if test="college!=null">college=#{college},</if>
            <if test="major!=null">major=#{major},</if>
            <if test="enrollYear!=null">enroll_year=#{enrollYear},</if>
            <if test="email!=null">email=#{email},</if>
            <if test="size!=null">size=#{size},</if>
            <if test="sex!=null">sex=#{sex},</if>
        </trim>
        where id = #{id}
    </update>

    <update id="updateRetrieveCode">
        update student
        set retrieve_code = #{code}
        where email = #{email}
    </update>


    <update id="updatePswd" >
        update student
        set pswd = #{pswd}
        where id = #{id}
    </update>

    <update id="updateStatus">
        update student
        set check_status = #{status}
        where id = #{id}
    </update>

    <insert id="insert" parameterType="Student">
        insert into student(stu_id, stu_name, family_name, first_name, school_id, college, major, enroll_year,
            phone, email, sex, size,  pswd, check_status)
        values (#{stuId}, #{stuName}, #{familyName}, #{firstName}, #{schoolId}, #{college}, #{major}, #{enrollYear},
           #{phone}, #{email}, #{sex}, #{size}, #{pswd}, #{checkStatus})
    </insert>

    <delete id="delete">
        delete from student
        where id = #{id}
    </delete>

    <select id="find" resultType="java.util.Map">
        select x.id, x.stu_id, x.stu_name, x.family_name, x.first_name, x.school_id, y.school_name,
        x.college, x.major, x.enroll_year, x.phone, x.email, x.sex, x.size
         from student as x, school as y
        where x.school_id = y.id
    </select>

</mapper>
```



#### select

通过select执行数据库查询

id: 标识映射文件中的sql

将sql语句封装到mappedStatement对象中，所以将id称为statement的id



使用${}可能引起sql注入

${value}：接收输入参数的内容，如果传入类型是简单类型，${}中只能是value



模糊查询：

```xml

```





#### insert

插入后返回自增主键

##### 自增主键

mysql自增主键，执行sql提交前生成一个自增主键。

通过mysql的函数LAST_INSERT_ID()可获取上一次insert生成的id值，但只适用于自增主键。

新插入条目的主键会保存在对象的对应主键字段。

```xml
<insert id="insert" parameterType="School">
    	
    <!--
	select LAST_INSERT_ID(): 得到之前insert的记录主键值，只适用于自增主键

keyProperty:将查询到的主键值设置到parameterType指定的对象的哪个属性
order: LAST_INSERT_ID()相对于insert语句的执行顺序
-->
    
        <selectKey keyProperty="id" order="AFTER" resultType = "java.lang.Integer">
            select LAST_INSERT_ID() as id
        </selectKey>
        insert into school(school_name, abbr_name, address, coach_id, chief_name, phone, bill_enterprise, postcode,
            tax_num, check_status)
        values (#{schoolName}, #{abbrName}, #{address}, #{coachId}, #{chiefName}, #{phone}, #{billEnterprise}, #{postcode},
           #{taxNum}, #{checkStatus})
    </insert>
```



##### 非自增主键

使用mysql中的uuid()函数生成主键，需要修改表中id字段类型为string，长度设置为35位

执行思路：

1. 通过uuid()查询到主键
2. 将主键输入到sql语句中
3. 执行uuid顺序相对于insert语句之前执行



### 3. 与hibernate区别

hibernate：标砖的ORM（对象关系映射）框架。不需要程序员写SQL，自动生成。对SQL优化、修改比较困难。

应用场景：

- 适用于需求变化不多的中小型项目，如：后台管理系统，erp, oa（成熟的项目）。



mybatis：专注sql本身，需要程序员自己编写sql语句。

应用场景：

- 需求变化较多，如：互联网项目



### 4. 开发模式

#### 1. 原始Dao开发

原始Dao接口的方法有三种缺点：

1. dao接口实现类方法中存在大量模板方法，可提取出来减轻程序员工作量
2. 调用sqlSession方法时，statement.id硬编码
3. 可越过泛型，编译时不报错

#### 2. mapper代理开发

开发规范：

1. 在mapper.xml中namespace等于mapper接口地址
2. mapper.java接口中的方法名和mapper.xml中statement的id一致
3. mapper.java接口中的方法输入参数类型和mapper.xml中statement的parameterType类型一致
4. 返回值一致



通过反射创建



### 5. SqlMapConfig.xml

#### 1. properties

读取顺序：

1. 在properties元素体内定的属性首先被读取
2. 然后读取properties元素中resource或url加载的属性，会覆盖已读取的同名属性
3. 最后读取parameterType传递的属性，会覆盖已读取的同名属性



#### 2. settings全局配置参数

mybatis框架在运行时可以调整一些运行参数

如：二级缓存、开启延迟加载等。



#### 3. typeAliases别名

在mapper.xml中定义了很多statement，其中定义类很多的parameterType指定输入参数类型和resultType指定输出结果的映射类型。

如果在指定类型时指定类型全路径，不利于开发，可以针对这二者定义一些别名。

```xml
<typeAliases>
	<!-- 
		type:类型全限定名
		alias:别名
	-->
    <typeAlias type="com.vue.shu.entity.Student" alias="Student"></typeAlias>
</typeAliases>
```

#### 4. typeHandlers 类型处理器

mybatis中通过typeHandlers完成jdbc类型和java类型的转换

mybatis提供的类型处理器满足日常需求，一般不需要自定义

#### 5. mappers映射器

通过resource加载单个映射文件

```xml
<mappers>
    <mapper resource = "mapper/UserMapper.xml"></mapper>
</mappers>
```

通过mapper接口加载，此种方法要求mapper接口名称和mapper映射文件相同，且放在同一个目录中 

```xml
<mappers>
    <mapper class="com.icpc.shu.dao.StudentMapper"></mapper>
</mappers>
```

批量加载，要求mapper接口名称和mapper映射文件相同，且放在同一个目录中 

```xml
<mappers>
    <package class="com.icpc.shu.dao"></package>
</mappers>
```

### 6. 输入映射

类型可以是简单类型、hashMap、pojo的包装类型

#### pojo的包装对象

需求：完成用户信息的综合查询，需要传入查询条件很复杂（包括用户信息、其他信息）

映射文件：

```xml

```



### 7. 输出类型

#### 7.1 resultType

使用resultType进行映射，只有查询出来的列名和pojo中的属性名一致，该列才可能映射成功。

如果查询出来的列名和pojo中的属性名全部不一致，不会创建pojo对象；

只要有一个一致，就会创建pojo对象。



#### 7.2 resultMap

完成高级输出结果映射。

如果查询出来的列名和pojo的属性名不一致，通过定义一个resultMap对列名和pojo属性名作映射关系

步骤：

1. 定义resultMap

```xml
<!--
type:resultMap最终映射成的java对象, 可以使用别名
id: 唯一标识，如果有多个列组成唯一标识，则配置多个id

column: 查询出来的列名
property: type指定的pojo类型中的属性名
最终resultMap对column和property做映射


如果statement使用其他mapper的resultMap，前面需要加namespace
-->
<resultMap type="User" id="userResultMap">
  	 	<id column="id_" property="id"/>
    	<result column="username_" property="username"/>
</resultMap>
```



2. 使用resultMap作为statement的输出类型



### 8. 动态sql

mybatis核心，对sql语句进行灵活操作，通过表达式进行判断，对sql进行灵活拼接、组装。

#### if判断

1. 对查询条件进行判断，如果输入参数不为空才进行拼接。

```xml
<select id="findUserList" parameterType="User" resultType="User">
    select * from user
    <!--
	where可以自动去掉条件中的第一个and
-->
    <where>
        <if test="userCostum!=null">
        	<if test="userCostum.sex!=null and userCostum.sex!=''">
            	and user.sex = #{userCostum.sex}
            </if>
        </if>
    </where>
</select>
```

#### sql片段

将上边实现的动态sql判断代码块抽取出来，组成一个sql片段。

定义sql片段

```xml
<!-- 
经验：
定义sql片段是基于单表定义
sql片段不要包含where
-->

<sql id="query_user_where">
	<if test="userCostum!=null">
        	<if test="userCostum.sex!=null and userCostum.sex!=''">
            	and user.sex = #{userCostum.sex}
            </if>
     </if>
</sql>
```

使用

```xml
<select id="findUserList" parameterType="User" resultType="User">
    select * from user
    <!--
	若include的sql片段不在本mapper中，加上其namespace
-->
    <where>
        <include refid="query_user_where"></include>
        <!--此处还要引用其他sql片段-->
    </where>
</select>
```

#### sql-foreach

给sql传递数组或list，mybatis使用foreach解析

传入多个id

```xml
<sql id="query_user_where">
	<if test="userCostum!=null">
        	<if test="userCostum.sex!=null and userCostum.sex!=''">
            	and user.sex = #{userCostum.sex}
            </if>
     </if>
    <!--
	使用foreach遍历传入ids
	collection: 指定输入对象集合属性
	item: 每个遍历生成对象中
	open: 开始遍历时拼接串
	close: 结束遍历时拼接串
	seperator: 遍历的两个对象中间需要拼接的串
	-->
    
    <if test="ids!=null">
        
        <!--方法1.实现sql拼接:	AND (id = 1 or id = 10 or id = 16)  -->
        <foreach collection="ids" item="user_id" open="AND (" close=")" seperator="or">
        	<!-- 每次遍历需要拼接的串-->
            id = #{user_id}
        </foreach>
        
        <!--方法2.实现sql拼接:	AND id in (1,10,16) -->
        <foreach collection="ids" item="user_id" open="AND id in (" close=")" seperator=",">
            #{user_id}
        </foreach>
    </if>
</sql>
```



### 9. 一对一查询

#### resultType

创建pojo：原始的类不能包含所有字段，因此需创建新的pojo；创建一个pojo继承包括查询字段较多的po类。

```java
public class OrderCustom extends Order{
    private String username;
    private String sex;
    
    /*
    getter and setter
    */
}
```



#### resultMap

在order类中添加User属性，将关联出来的用户信息映射到order对象的user属性中。

```java
public class Order{
    private Integer id;
    private Integer userId;
    private String number;
    private String note;
    private Date createTime;
    
    private User user;
    /*
    getter and setter
    */
}

public class User{
    private Integer id;
    private String username;
    private String sex;
    private String address;
}
```

定义resultMap

```xml
<resultMap id="ordersResult" type="Order">
    <id column="id" preperty="id"></id>
    <result column="note" property="note"></result>
    <result column="number" property="number"></result>
    <result column="creat_time" property="creatTime"></result>
    
    
    <!--assocication可以映射关联查询单个对象的信息 
        property:要将关联查询的用户信息映射到Orders中的哪个属性
        javaType:指定这个属性对象的类型
    -->
    <association prepterty="user" type="User">
    	<id column="user_id" property="id"></id>
        <result column="user_name" property="username"></result>
        <result column="sex" property="sex"></result>
        <result column="address" property="address"></result>
    </association>
</resultMap>
```

在statement中使用



resultMap可以实现**延迟加载**

### 9. 一对多查询

查询订单以及订单明细，一条订单又多条明细，要求对order不能出现重复信息

在目标类中添加List<OrderDetail> orderDetails属性

```java
//订单明细
public class OrderDetail{
    private Integer id;
    private Integer itemId;
    private Integer itemNum;
    private Integer orderId;		//外键
}

//订单
public class Order{
    private Integer id;
    private Integer userId;
    private String number;
    private String note;
    private Date createTime;
    
    User user;
    
    List<OrderDetail> orderDetails;
}
```



resultMap**的定义**

```xml
<!--可以使用继承-->

<resultMap id="ordersResultMap" type="Order" extends="ordersResult">
    <!--
	订单信息
	用户信息
	订单明细信息
-->
    
    <!--使用collection进行一对多映射
	collection:对关联查询到多条记录映射到集合对象中
	property:要将关联查询到的多条记录映射到Order中的哪个属性
	ofType: 指定映射到list集合属性中的pojo类型
-->
    <collection property="orderDetails" ofType="OrderDetail">
        <id column="item_detail_id" property="id"></id>
    	<result column="item_id" property="itemId"></result>
        <result column="id" property="orderId"></result>
    </collection>
    
</resultMap>
```



### 10. 多对多查询

需求：查询用户及用户购买商品信息

在User类中添加订单l列表属性 List<Order> orderList，将用户创建的订单映射到orderlist；

在Order类中添加订单明细列表属性 List<OrderDetail> orderDetails，将订单的明细映射到orderDetails；

 collection套collection



#### resultMap总结

resultType需要新建一个pojo类型（继承原有对象），将关联数据全部展示在页面；

resultMap使用association和collection完成一对一和一对多的高级映射；需要在原有对象中添加属性（对象或对象list）



### 11. 延迟加载

#### 1. 定义

resultMap可以实现高级映射。association和collection具备延迟加载的功能。

需求：查询订单并且关联查询用户信息。如果先查询订单信息即可满足要求，当我们需要查询用户信息时再查询用户信息。把对用户信息的按需去查询就是延迟加载。

延迟加载：先从单表查询、需要时再从关联表去查询，大大提高数据库性能，因为查询单表比关联查询多表速度要快。

#### 2. 使用

定义resultMap

```xml
<resultMap id="orderUserLazyLoadingMap" type="Order">
    <id column="id" preperty="id"></id>
    <result column="note" property="note"></result>
    <result column="number" property="number"></result>
    <result column="creat_time" property="creatTime"></result>
    
    
    <!--assocication可以映射关联查询单个对象的信息 
        select: 指定延迟加载需要执行的statement的id （是根据user_id查询用户信息的statement）
	    column: 订单信息中关联用户信息查询的列， user_id
-->
    <association prepterty="user" type="User" select="findUserById" column="user_id">
        
    </association>
</resultMap>

<select id="findUserById" parameterType="int" resultType="User">
	select * from user where id = #{value}
</select>

<select id="findOrderUserLazyLoading" resultMap = "orderUserLazyLoadingMap">
    select * from orders
</select>
```



sqlMapConfig中打开延迟加载开关

```xml
<settings>
    <setting name="LazyLoadingEnabled" value="true"></setting>
    <setting name="agressiveLazyLoading" value="true"></setting>
</settings>
```

总结：使用延迟加载的方式，先去查询简单的sql（最好是单表），再去按需加载关联查询的其他信息

### 12. 查询缓存

mybatis提供的查询缓存，用于减轻数据库压力，提高数据库性能

提供一级缓存和二级缓存

![](D:\技术\学习笔记\Spring\pic-mybatis\缓存.PNG)

在操作数据库时需要构造sqlSession对象，在对象中有一个数据结构（HashMap）用于存储缓存数据。

一级缓存：不用的sqlSession之间的缓存数据区域是互相不影响的

二级缓存：mapper级别的缓存，多个sqlSession去操作同一个Mapper的sql语句，多个sqlSession可以公用二级缓存，跨sqlSession。

mybatis默认开启一级缓存，不需要在配置文件中配置。



#### 一级缓存

执行commit会清空一级缓存



**一级缓存应用**

一个service方法中包括很多mapper方法调用。

service{

​	//开始执行时，开启事务，创建sqlSession对象

​	//第一次调用findUserById(1)

​	//第二次调用findUserById(1)	从一级缓存中获取

//AOP，方法结束时sqlSession关闭

}

如果是执行两次service方法，不走一级缓存，因为之前的sqlSession已经被关闭



#### 二级缓存

开启二级缓存

sqlSession1去查询用户id为1的用户信息，查询到的用户信息会将查询数据存储到二级缓存中。

sqlSession2去。。。，直接从二级缓存中取出数据。

多个sqlSession可以共享一个UserMapper（namespace）的二级缓存区域。

sqlSession3去执行相同mapper下sql，执行commit提交，清空该mapper的二级缓存区域的数据。



##### 开启二级缓存

除了在sqlMapConfig.xml设置二级缓存的总开关，还要在具体的mapper.xml中开启二级缓存。

```xml
<settings>
    <setting name="cacheEnabled" value="true"></setting>
</settings>
```

在userMapper.xml中开启二级缓存

````xml
<mapper namespace="xxx">
    <cache/>
</mapper>
````

pojo对象实现序列化接口

```java
public class User implements Serializable{
    
}
```

为了将对象反序列化，因为其可能存储在文件、硬盘上。



可以在statement中设置useCache=false，可以禁用二级缓存，即每次查询都会发sql去查询，默认情况是true，即该sql使用二级缓存。



##### 应用场景

访问多的查询请求且用户对查询结**实时性要求不高**，此时可采用二级缓存降低数据库访问量。



局限性：对一个列表进行缓存，但是若修改其中一个数据则会清楚整个缓存，所以细粒度的实现不好。



#### 整合分布式缓存框架

redis, memcache, ehcahce等

mybatis提供了一个cache接口，如果要实现自己的缓存逻辑，实现cache接口开发即可。





### maven逆向工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.mybatis.reverse</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator</artifactId>
            <version>1.3.7</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.7</version>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>


    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

        </plugins>
    </build>

</project>

```

```java
package com.mybatis.reverse.demo.plugin;



import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    public void generator() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        /**指向逆向工程配置文件*/
        File configFile = new File("src/generationConfig.xml");
        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration config = parser.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }
    public static void main(String[] args) throws Exception {
        try {
            Generator generatorSqlmap = new Generator();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


```



```xml
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="mybatisGenerator" targetRuntime="MyBatis3">
        <commentGenerator>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://47.100.89.70:3306/icpc?useUnicode=true;characterEncoding=UTF-8"
                        userId="root"
                        password="123456">
        </jdbcConnection>

        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
            NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!-- targetProject:生成PO类的位置 -->
        <javaModelGenerator targetPackage="main\java\com\mybatis\reverse\demo\pojo"
                            targetProject=".\src">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- targetProject:mapper映射文件生成的位置 -->
        <sqlMapGenerator targetPackage="main\java\com\mybatis\reverse\demo\mapper"
                         targetProject=".\src">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
        <!-- targetPackage：mapper接口生成的位置 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="main\java\com\mybatis\reverse\demo\mapper"
                             targetProject=".\src">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
        <!-- 指定数据库表 -->
        <table tableName="student"></table>
        <table tableName="coach"></table>
        <table tableName="school"></table>
        <table tableName="admin"></table>
        <table tableName="contest"></table>
        <table tableName="team"></table>
        <!-- 有些表的字段需要指定java类型
         <table schema="" tableName="">
            <columnOverride column="" javaType="" />
        </table> -->
    </context>
</generatorConfiguration>s
```

