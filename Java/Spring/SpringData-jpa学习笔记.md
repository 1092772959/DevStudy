### Respository接口

Respository是Springdata JPA中的顶层接口，提供了两种查询方法：

1）基于方法名称命名规则

2）基于@Qeury注解查询



#### 1. 方法名称命名规则查询

规则：findBy（关键字）+属性名称（属性名称首字母大写）+查询条件（首字母大写）



模糊查询：

```java
@Test
public void test(){
    
    List<StudentEntity> res = this.stu.findByXmLike("刘%");
    for(StudentEntity tmp : res){
        System.out.println(tmp);
    }
}
```



方法命名规则：

- And --- 等价于 SQL 中的 and 关键字，比如 findByUsernameAndPassword(String user, Striang pwd)；
- Or --- 等价于 SQL 中的 or 关键字，比如 findByUsernameOrAddress(String user, String addr)；
- Between --- 等价于 SQL 中的 between 关键字，比如 findBySalaryBetween(int max, int min)；
- LessThan --- 等价于 SQL 中的 "<"，比如 findBySalaryLessThan(int max)；
- GreaterThan --- 等价于 SQL 中的">"，比如 findBySalaryGreaterThan(int min)；
- GreaterThanEqual
- IsNull --- 等价于 SQL 中的 "is null"，比如 findByUsernameIsNull()；
- IsNotNull --- 等价于 SQL 中的 "is not null"，比如 findByUsernameIsNotNull()；
- NotNull --- 与 IsNotNull 等价；
- Like --- 等价于 SQL 中的 "like"，比如 findByUsernameLike(String user)；
- NotLike --- 等价于 SQL 中的 "not like"，比如 findByUsernameNotLike(String user)；
- OrderBy --- 等价于 SQL 中的 "order by"，比如 findByUsernameOrderBySalaryAsc(String user)；
- Not --- 等价于 SQL 中的 "！ ="，比如 findByUsernameNot(String user)；
- In --- 等价于 SQL 中的 "in"，比如 findByUsernameIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数；
- NotIn --- 等价于 SQL 中的 "not in"，比如 findByUsernameNotIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数；



缺点：查询条件过于复杂时，方法名会很长



#### 2. 基于@Query注解的查询

##### 2.1 通过JPQL语句查询

JPQL：通过Hibernate的HQL演变过来的。和HQL语法极其相似。

不需要实现方法；

JPQL中语句的变量使用类名和类中成员。

```java
//使用@Query注解查询, 抽象方法, 参数列表的顺序就是sql语句中的顺序
    @Query(value="from StudentEntity where xm like ?1")
    List<StudentEntity> queryByXm(String name);

//多值查询
	@Query(value="from StudentEntity where xm is ?1 and yxh is ?2")
	List<StduentEntity> queryByXmAndYxh(String name, Integer yxh);

//或是这样的Like查询写法
@Query(value="from StudentEntity where xm like %:name%")
    List<StudentEntity> queryByXm(@Param("name") String name);
```



@Param("name")   给参数在JPQL语句中赋值



##### 2.2 通过SQL语句查询

@Query语句中使用数据库中的表名和表中数据

nativeQuery = true 使用SQL语句

```java

//可标可不标参数顺序, 多值查询时不标明顺序按次序填充
@Query(value = "select * from S where xm = ?1", nativeQuery = true)
    List<StudentEntity> queryBySQL(String name);
```



####  3. 分页

PagingAndSortingRepository

##### 3.1. 分页处理

```java
@Test
    public void testPaging(){
        int page, size;
        //page 为当前页的索引，起始为0
        //size 为页面大小
        page = 1;
        size = 5;
        Pageable p = new PageRequest(page,size);
        Page<StudentEntity> res =  this.stu.findAll(p);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }
```



##### 3.2 排序处理

```java
/*
    对单列做排序
     */
    @Test
    public void testSort(){
        // Sort: 该对象封装了排序规则以及指定的排序字段（对象的属性来表示）
        // direction: 排序规则
        // properties: 指定做排序的属性, 给表对应类的属性
        Sort sort = new Sort(Sort.Direction.DESC, "xh");
        List<StudentEntity> res = this.stu.findAll(sort);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }

/*
多列排序
*/
@Test
    public void testSort2(){
        //Sort sort = new Sort();
        Sort.Order o1 = new Sort.Order(Sort.Direction.DESC, "yxh");
        Sort.Order o2 = new Sort.Order(Sort.Direction.ASC, "csrq");
        Sort sort = new Sort(o1,o2);
        List<StudentEntity>  res = this.stu.findAll(sort);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }
```



#### 4. JpaSpecificationExecutor接口

不能单独使用，必须和JpaRepository同时继承，否则无法生成代理对象。

完成**复杂的多条件查询**，并且支持分页与排序

```java
//1.创建接口
public interface StudentRepository extends JpaRepository<StudentEntity,Integer>, JpaSpecificationExecutor<StudentEntity>
```

##### 5.1 单条件查询

根据criteriaBuilder的方法调用确定查询方法，如：相等判断，模糊查询等

root.get()方法返回的值可以用 .as(String.class)转成 字符串类 （或其他指定类）

```java

/*
    JpaSpecificationExecutor
    单条件查询
     */
    @Test
    public void testSpecification(){
        Specification<StudentEntity> spec= new Specification<StudentEntity>() {
            /*
            @return Predicate:定义了查询条件
            @param Root<StduentEntity> root:根对象，封装了查询条件的对象
            @param criteriaQuery :定义了基本的查询，一般不常用
            @param criteriaBuilder : 创建一个查询条件
             */
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate pre = criteriaBuilder.equal(root.get("xm"), "刘%");
                return pre;
            }
        };

        List<StudentEntity> res = this.stu.findAll(spec);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }
```

##### 5.2 多条件查询

多条件查询有多种方式

```java
/*
    JpaSpecificationExecutor
    需求：使用姓名和学院查询数据
    多条件查询方式一
     */
    @Test
    public void testSpecification2(){
        Specification<StudentEntity> spec= new Specification<StudentEntity>() {
            /*
            @return Predicate:定义了查询条件
            @param Root<StduentEntity> root:根对象，封装了查询条件的对象
            @param criteriaQuery :定义了基本的查询，一般不常用
            @param criteriaBuilder : 创建一个查询条件
             */
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> plist = new ArrayList<>();
                plist.add(criteriaBuilder.like(root.get("xm"), "刘%"));
                plist.add(criteriaBuilder.equal(root.get("yxh"),2));
                //此时条件之间没有关联
                Predicate [] arr = new Predicate[plist.size()];
                return criteriaBuilder.or(plist.toArray(arr));     //规定关系之间的关系并返回查询规则
                //如果再想或关系，就将cb返回的Predicate对象再放入cb.or方法中
            }
        };

        List<StudentEntity> res = this.stu.findAll(spec);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }
```



```java
/*
    JpaSpecificationExecutor
    需求：使用姓名和学院查询数据
    多条件查询方式二
     */
    @Test
    public void testSpecification2(){
        Specification<StudentEntity> spec= new Specification<StudentEntity>() {
            /*
            @return Predicate:定义了查询条件
            @param Root<StduentEntity> root:根对象，封装了查询条件的对象
            @param criteriaQuery :定义了基本的查询，一般不常用
            @param criteriaBuilder : 创建一个查询条件
             */
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.or(cb.like(root.get("xm"),"刘%"), cb.equal(root.get("yxh"),2));
            }
        };

        List<StudentEntity> res = this.stu.findAll(spec);
        for(StudentEntity s: res){
            System.out.println(s);
        }
    }

```



##### 5.3 多条件查询+分页

```java
	/*
    查询院系号为1或性别为女的同学 结果分页
     */
    @Test
    public void test4(){
        Specification<StudentEntity> spec = new Specification<StudentEntity>() {
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("yxh"),1),cb.equal(root.get("xb"),0));
            }
        };

        Pageable pg = new PageRequest(0,2);
        Page<StudentEntity> res = this.stu.findAll(spec, pg);
        System.out.println(res.getTotalElements());
        System.out.println(res.getTotalPages());
        for(StudentEntity s :res){
            System.out.println(s);
        }
    }
```



##### 5.4 条件查询+排序

```java
/*
    查询院系号为1或性别为女的同学，结果按学号做倒序排序
     */
    @Test
    public void test5(){
        Specification<StudentEntity> spec = new Specification<StudentEntity>() {
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("yxh"),1),cb.equal(root.get("xb"),0));
            }
        };

        Sort sort = new Sort(Sort.Direction.DESC,"xh");
        List<StudentEntity> res = this.stu.findAll(sort);
        for(StudentEntity s :res){
            System.out.println(s);
        }
    }
```



##### 5.5 条件查询+分页+排序

将排序整合到分页对象中

```java
new PageRequest(0,3,sort);
```



```java
/*
    查询院系号为1或性别为女的同学，做分页处理，结果按学号做倒序排序
     */
    @Test
    public void test6(){
        //排序定义
        Sort sort = new Sort(Sort.Direction.DESC,"xh");
        //分页定义,Pageable对象中存在
        Pageable pg = new PageRequest(0,3,sort);

        Specification<StudentEntity> spec = new Specification<StudentEntity>() {
            @Override
            public Predicate toPredicate(Root<StudentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("yxh"),1),cb.equal(root.get("xb"),0));
            }
        };
        Page<StudentEntity> res = this.stu.findAll(pg);
        System.out.println("总条目数: "+res.getTotalElements());
        System.out.println("页数: "+res.getTotalPages());
        for(StudentEntity s : res){
            System.out.println(s);
        }
    }
```







#### 5. 实体类

##### 5.1 主键定义

```java
//自增方式创建主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="xh")
    private int xh;
```



@Temporal方法

```java
//JPA将时间映射到日期
    @Temporal(TemporalType.DATE)
    @Column(name = "csrq")
    private Date csrq;
```





#### 6. 关联关系

##### 6.1 一对一关系

例：角色与用户一对一

```java
//测试一对一关系 学生类
    @OneToOne(cascade = CascadeType.PERSIST)		//表示级联关系，增加学生时，对应用户也会加入数据库
    @JoinColumn(name="id")				//表示外键，使用对应表的主键(数据库内的名称)
    private Account account;
```

```java
	//用户类
	@OneToOne(mappedBy = "account")			//看谁引用了这个account
	private StudentEntity student;
```



操作一对一关系

```java
//增添数据
@Test
    public void test7(){
        //创建用户对象
        Account acc = new Account();
        acc.setUsername("玩家");

        //创建学生对象
        StudentEntity student = new StudentEntity();
        student.setXm("李修文");
        student.setYxh(3);
        student.setSjhm("13120716616");
        student.setPassword("0");
        //student.setCsrq(new Date("1998-7-24"));
        student.setJg("上海");
        student.setXb(1);

        //关联
        student.setAccount(acc);
        acc.setStudent(student);

        //保存学生，相应地acc中也会出现“玩家”条目
        this.stu.save(student);
    }
```

```java
	/*
    根据学号查询学生，并查询其账户
     */
    @Test
    public void test8(){
        StudentEntity student = this.stu.findByXh(1112);
        System.out.println("学生信息："+student);
        Account acc = student.getAccount();
        System.out.println(acc);
    }
```



##### 6.2 一对多关系

 ```java
//用户类
@Id
    @Column(name="user_id")
    private int userId;

    @Column(name="user_name")
    private String name;

    @OneToMany
    @JoinColumn(name="role_id")
    private Role role;
 ```



```java
//角色类
	@Id
    @Column(name="role_id")
    private Integer roleId;

    @Column(name="role_name")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<User>();

```



##### 6.3 多对多关系

将会生成一个中间表

```java
//学生类

/*
@JoinTable: 配置中间表信息
joinColumns: 建立当前表在中间表的外键字段
*/
@ManyToMany(cascade = CasadeType.PERSIST, fetch = FetchType.EAGER)		//级联操作，添加不存在的数据时同时导入课程类,  放弃延迟加载改为立即加载
@JoinTable(name="t_stu_class",joinColumns=@JoinColumn(name="stu_xh"), inverseJoinColumns = @JoinColumn(name="class_id"))
private Set<Class> classes = new HashSet<Class>();

```

```java
//课程类
@ManyToMany(mappedBy = "classes")		//映射引用课程类的对象
private Set<Student> students = new HashSet<Student>();
```



***无论是一对多还是多对多，级联操作开启在具体使用的那个类的注解上**

在查询时，如果没有用立即加载的话，输出学生对应的所有课程会报错（因为延迟加载，session在检索出学生后就关闭了）



#### 7. 更新操作

使用repository继承的save接口，它会根据



#### 8. 多表查询

根据不同的查询的对象，将该方法写在对应的Dao接口中

```java
/**
     * 多表查询：根据用户id查询其收藏的文章
     * @param id
     * @return
     */
    @Query(value="select * from article as X " +
            "where X.id in (" +
                "select article_id from favorite " +
                "where userid = ?1)",
            nativeQuery = true)
    List<Article> findArticleByUserId(Integer id);
```

