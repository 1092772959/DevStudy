# Spring学习笔记

传统方法遇到的问题：

- 需要引入其他类来支持运行
- 缺少依赖会导致编译时报错



耦合：程序间的依赖关系

- 类之间的依赖
- 方法间的依赖

解耦：降低程序间的依赖关系

实际开发中：编译期不依赖，运行时才依赖

解耦思路：

1. 使用反射来创建对象，避免使用new关键类
2. 通过读取配置文件来获取要创建的对象全限定类名



BeanFactory

Bean：可重用组件

JavaBean：（不等同于实体类，远大于实体类），用java语言编写的可重用组件。创建service和dao对象

1. 需要配置文件来配置service和dao。配置内容：唯一标识 -> 全限定类名(key : value)
2. 通过读取配置文件中的配置内容，反射创建对象
3. 配置文件可以是xml也可以是properties

工厂模式创建bean对象：

1. 定义properties对象
2. 使用静态代码块为properties对象赋值
3. 定义getBean()函数，通过properties和beanName获取bean的路径，并通过该路径和Class.forName()方法返回Object对象（由于不确定需要返回哪个bean，service或者dao）。
4. 多例模式



解耦升级：

使用HashMap存储所有properties里记录的beans，需要获取时直接按名读取。

此时是单例模式



## 一、控制反转IOC

控制反转：应用把创建对象的权利交给框架

作用：削减计算机程序的耦合

依赖注入：在当前类需要用到其他类的对象，由 Spring 提供，而在配置文件中说明依赖关系的维护

Spring中配置文件xml放在resource目录下

可通过ApplicationContext，之后通过xml中定义的id获取bean对象。

```xml
<?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd">
 
     <bean id = "accountService" class = "com.itheima.service.impl.AccountServiceImpl">
          <property name="name" value ="taylor"></property>
          <property name="age" value="21"></property>
          <property name="birthday" ref="now"></property>
      </bean>
 </beans>
```



```java
@Test
    public void test7(){
        //1.获取核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        //.根据id获取Bean对象
        ac.getBean("accountService", AccountServiceImpl.class);
    }
```



## *核心容器的两种接口

1. ApplicationContext：在构建核心容器时，创建对象采取的策略是采用立即加载的方式。一读取完配置文件就创建文件中配置的对象（单例对象适用）
2. BeanFactory：策略是延迟加载方式，什么时候根据id获取对象了，什么时候才真正创建对象（多例对象适用）

BeanFactory是一个顶层接口，很多功能未实现，一般在开发中实用ApplicationContext。



### 2. Bean

#### 1. 创建Bean的三种方式：

##### 1. 默认构造函数构造

在spring的配置文件中使用bean标签，配以id和class属性之后，且没有其他属性和标签时，采用默认构造函数创建，此时如果类中没有默认构造函数，则对象无法创建。

```xml
<?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd>
                            
  <bean id ="accountService" class="com.itheima.service.impl.AccountServiceImpl"></bean>
 </beans>
```

id：需要的bean

class：映射反转的对象（实际创建的对象）

##### 2. 普通工厂

使用某个类（jar包中的类）的方法创建对象，并存入Spring容器

```xml
<bean id ="accountService" factory-bean ="instanceFactory" factory-method ="getAccountService"></bean>
```

id：需要的bean

factory-bean：创建bean的工厂

factory-method：工厂中创建bean的方法

##### 3. 静态工厂中的静态方法

使用某个类（jar包中的类）中的静态方法创建对象，并存入Spring容器

```xml
<bean id ="accountService" class ="com.itheima.factory.StaticFactory" factory-method ="getAccountService"></bean>
```



#### 作用范围

1. bean 标签的 scope 属性

   （添加在xml配置中）

   作用：用于指定 bean 的作用范围

   取值：常用的就是单例和多例

   - singleton : 单例的（default）
   - prototype : 多例的
   - request : 作用于 web 应用的请求范围
   - session : 作用于 web 应用的**会话**范围
   - global-session : 作用于集群的会话范围（全局会话范围），当不是集群范围时，它就是 session

   *常用前两个

2. bean对象的声明周期

   单例对象：

   - 出生：当容器创建时发生（解析完配置文件）
   - 活着：只要容器还在对象就一直活着
   - 死亡：容器销毁，对象消亡

   总结：单例对象的声明周期和容器相同

   多例对象：

   - 出生：当我们使用对象时 Spring 框架为我们创建
   - 活着：对象只要是在使用过程中就活着
   - 死亡：当对象长时间不用，且没有别的对象引用时，由 Java 的GC回收

XML文件中：

```xml
 <bean id = "accountService" class = "com.itheima.service.impl.AccountServiceImpl" scope="singleton" init-method="init" destroy-method="destroy">
     </bean>
```

AccountService对象中的方法：

```java
public void init(){
    //初始化的操作
}
public void destroy(){
    //销毁的操作
}
```

测试类：

```java
public static void main(String[] args) {
          //1.获取核心容器对象
          ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
          //2.根据id获取Bean对象
          IAccountService as  = (IAccountService)ac.getBean("accountService");
  
          as.saveAccount();
    	  ac.close();
      }
  }
```

若ac是ApplicationContext接口类型，则Bean对象不会执行销毁操作。因为容器并不会销毁，而是在main函数结束时直接在内存中被释放。

若Bean对象采用**多例**模式，则该类对象的销毁不由Spring负责，而是由java的垃圾回收机制销毁。在长时间没有访问且没有被其他对象引用时，该类对象会被垃圾回收机制销毁。



### 3. 依赖注入

在IOC降低了耦合之后，在当前类需要用到其他类的对象，由 Spring 为我们提供。而在配置文件中说明依赖关系的维护，这种方式就称为依赖注入。

能注入的数据：

- 基本类型和 String
- 其他 bean 类型（在配置文件中或者注解中配置过的bean）
- 复杂类型/集合类型

注入的方法：

1. 使用构造函数
2. 使用set方法
3. 使用注解提供



#### 1. 使用构造函数提供

使用的标签：constructor-arg

标签所在位置：bean 标签的内部

标签中的属性：

- type : 用于指定要注入的数据类型，该类型也是构造函数中某个或某些参数的类型
- index : 用于指定要注入的数据给构造函数中指定索引位置的参数赋值，索引的位置时从0开始
- name(**常用**) : 用于指定给构造函数中指定名称的参数赋值
- value : 用于提供基本类型和String类型的数据
- ref : 用于指定其他的bean类型数据。它指的就是在spring的IOC核心容器出现过的bean对象

*前三个用于指定给构造函数中哪个参数赋值

特点：在获取 bean 对象时，注入数据是必须的操作，否则无法操作成功

弊端：改变了 bean 对象的实例化方式，使我们在用不到这些数据的情况下也必须提供带参构造函数，**因此开发中较少使用此方法**，除非避无可避。



```java
 public class AccountServiceImpl implements IAccountService {
     // 如果时经常变化的数据不适用于依赖注入，此处仅为演示
     private String name;
     private Integer age;
     private Date birthday;
 
     public AccountServiceImpl(String name, Integer age, Date birthday){
         this.name = name;
         this.age = age;
         this.birthday = birthday;
     }
 
     public void  saveAccount() {
         System.out.println("service中的saveaccount()执行了");
     }
 
 }
```

配置文件

```xml
 <bean id = "accountService" class = "com.itheima.service.impl.AccountServiceImpl">
         <constructor-arg name = "name" value="taylor"></constructor-arg>
         <constructor-arg name = "age" value = "23"></constructor-arg>
         <constructor-arg name = "birthday" ref = "now"></constructor-arg>
     </bean>
	
	<!--此处相当于配置了一个日期类  读取全限定类名，反射创建一个bean，存入Spring核心容器中-->
     <bean id = "now" class = "java.util.Date"></bean>
```

#### 2. set方法

需要在bean对象的定义中给出set方法

使用的标签：property

出现的位置：bean 标签的内部

标签的属性：

- name : 用于指定注入时所使用的 set 方法
- value : 用于提供基本类型和 String 类型的数据
- ref : 用于指定其他的bean类型数据，它指的就是在 Spring 容器中出现过的bean对象

优势：创建对象时没有明确的限制，可以直接使用默认构造函数

弊端：如果有某个成员必须有值，是有可能 set 方法没有执行



基本类型和字符串类型：

配置文件：

```xml
	<bean id = "accountService" class = "com.itheima.service.impl.AccountServiceImpl">
          <property name="name" value ="taylor"></property>
          <property name="age" value="21"></property>
          <property name="birthday" ref="now"></property>
      </bean>
  
      <bean id = "now" class = "java.util.Date"></bean>
```

bean类：

```java
  public class AccountServiceImpl implements IAccountService {
      // 如果时经常变化的数据不适用于依赖注入，此处仅为演示
      private String name;
      private Integer age;
      private Date birthday;
  
      public void setName(String name) {
          this.name = name;
      }
  
      public void setAge(Integer age) {
          this.age = age;
      }
  
      public void setBirthday(Date birthday) {
          this.birthday = birthday;
      }
  
      public void  saveAccount() {
          System.out.println("service中的saveaccount()执行了" + name + "," + age + "," +birthday);
      }
  
  }
```



2.复杂集合类型：

- 用于给 List 结构集合注入的标签
  - list
  - array
  - set
- 用于给map结构集合注入的标签
  - map
  - properties

**结构相同，标签可以互换，因此开发中只要记住两组标签即可**

```xml
<bean id = "accountService" class = "com.itheima.service.impl.AccountServiceImpl">
         <!--以下三个标签是等价的，set未列出-->
         <property name="myList">
             <list>
                 <value>aaa</value>
                 <value>bbb</value>
             </list>
         </property>
 
         <property name="myStrs">
             <array>
                 <value>aaa</value>
                 <value>bbbb</value>
             </array>
         </property>
 
         <property name="mySet">
             <array>
                 <value>aaa</value>
                 <value>bbbb</value>
             </array>
         </property>
 
         <!--以下两种方式等价-->
         <property name="myMap">
             <map>
                 <!--以下两种配置方式都可以-->
                 <entry key="testA" value="aaa"></entry>
                 <entry key="testA">
                     <value>bbb</value>
                 </entry>
             </map>
         </property>
 
         <property name="myProps">
             <props>
                 <prop key="testB">bbb</prop>
             </props>
         </property>
     </bean>
```

```java
//bean类内
private String[] myStrs;
     private List<String> myList;
     private Set<String> mySet;
     private Map<String, String> myMap;
     private Properties myProps;
```



#### 3. 注解注入

*此时set方法不再是必须的了

第一步：在类或方法的前面加上注解关键字

第二步：引入约束,注意此处约束多了xmlns:context...

第三步：添加配置文件，告知 Spring 在创建容器时要扫描的包，配置所需的标签不是在 bean 约束中，而是一个名称为context 的名称空间和约束中,完整配置如下：

```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd">
 
     <context:component-scan base-package="com.itheima"></context:component-scan>
 </beans>
```



接口：

```java
 public interface IAccountDao {
  
      void saveAccount();
  }

  public interface IAccountService {
  
      /**
       * 模拟保存账户
       */
      void saveAccount();
  }
```



实现类：

```java
  @Service("accountService")
  public class AccountServiceImpl implements IAccountService {
  
      @Autowired
      @Qualifier("accountDao2")
      private IAccountDao accountDao = null;
  
  
      public void  saveAccount() {
          accountDao.saveAccount();
      }
  
  }

  @Repository("accountDao1")
  public class AccountDaoImpl implements IAccountDao {
  
      public void  saveAccount() {
          System.out.println("对象创建了111");
      }
  
  }

  @Repository("accountDao2")
  public class IAccountDaoImpl2 implements IAccountDao{
  
      public void  saveAccount() {
          System.out.println("对象创建了222");
      }
  }
```



注解类型

##### 1.用于创建对象

作用：等同于 xml 配置文件中编写一个bean标签，将当前类对象存入spring容器中

```java
@Component(value="accountDao1")			//value绑定bean的id
```

属性：

- value：用于指定bean的id，不写时默认是当前类名，首字母小写。当通过容器获取时，call这个value。只包括一个类时，value= 可不写

由Component衍生的注解：

- @Controller 用于表现层
- @Service 用于业务层
- @Repository 用于持久层

以上三个注解的作用与 @Component 完全一样，它们是 Spring 提供的更明确的划分，使三层对象更加清晰。



##### 2.用于注入数据的

1）匹配类型唯一

```java
@AutoWired
private IAccountDao accountDao = null;
```

- 作用：自动按照类型注入，只要容器中有唯一的一个 bean 对象类型和要注入的变量类型匹配， 就可以注入成功
- 如果IOC容器中没有任何 bean 的类型和要注入的变量类型匹配，则报错。

- 如果容器中有多个bean类型匹配，首先按照类型圈定匹配的对象，其次选用变量名称id匹配。此时如果多个名称都与其不匹配则报错，若有变量名称匹配则将该bean赋给变量。
- 出现位置：可以是变量上，也可以是方法上。

*在使用注解输入时，set方法就不是必须的了

2）匹配类型冲突——写法一

```java
@Autowired
@Qualifier(value="accountDao1")
private IAccountDao acountDao = null;
```

- 作用：在按照类型注入的基础上再按照名称注入，它在给类成员注入时不能单独使用，但是在给方法参数注入 时可以。
- 属性：value : 用于指定注入的 bean 的 id

3）匹配类型——写法二

```java
@Resource(name="accountDao2")
private IAccountDao acountDao = null;
```

@Resource

- 作用：直接按照 bean 的 id 注入，可以直接使用
- 属性：name : 用于指定 bean 的 id
- 等同于@Autowired+@Qualifier

*以上三个注入都只能注入其他 bean 类型的数据，而基本类型和 String 类型的数据无法使用上述注解实现。另外，集合类型的注入只能通过 xml 配置文件实现*

4）注入基本类型和String类型

```java
@Value
```

- 作用：用于注入基本类型和 String 类型的数据

- 属性：

  value : 用于指定数据的值，它可以使用 Spring 中 Spel (即spring的el表达式)

  Spel 的写法：${表达式}



##### 3.用于改变改变作用范围

```java
@Scope(value="prototype")
private IAccountDao acountDao = null;
```

- 作用：用于指定 bean 的作用范围，等用于xml中bean标签的scope属性

- 属性：

  value : 指定范围的取值，同 xml 中值，常用为 singleton , prototype。默认为singleton

##### 4.生命周期相关（了解即可）

```java
@PostConstruct
public void init(){
   //初始化的操作
}

@PreDestroy
public void delete(){
    //销毁的操作
}
```

- @PreDestroy

  作用：用于指定销毁方法

- @Postcontruct

  作用：用于指定初始化方法

测试类：

```java
 public static void main(String[] args) {
          //1.获取核心容器对象，如果此处用接口类型则不能执行对象销毁的具体操作
          ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
          //2.根据id获取Bean对象
          IAccountService as  = (IAccountService)ac.getBean("accountService");
          as.saveAccount();
      }
  }
```



### 4. 注解

但如果使用注解实现IOC，XML文件中依然存在一些加载bean的配置，因为无法对jar包中的类（数据库驱动等）加上注解。

解决方法：创建配置类

```java
@Configuration
@ComponentScan( basePackages={"com.itheima"})
public class SpringConfiguration{
    
    /*
    用于创建一个QueryRunner对象
    */
    @Bean(name="runner")
    @Scope("prototype")				//使之称为多例模式
    public QueryRunner createQueryRunner(DateSource dataSource){
        return new QueryRunner(dataSrouce);
    }
    
    /*
    创建数据源对象
    */
    @Bean(name="dataSrouce")
    public DateSource createDateSrouce(){
        try{
        	ComboPooledDataSource ds = new CombaPooledDataSource();    
        	ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
            ds.setUser("xwl");
            ds.setPassword("123456");
        }catch(Exception e){
            throw new RuntimeException(e);
        } 
    }
}
```

#### @Configuration

- 作用：指定当前类是一个配置类，等同于xml配置
- 细节：当配置类作为AnnotationConfigApplicationContext对象创建的参数时并且所有的配置信息都写在这个传入的配置类中时，该注解可以不写。

#### @ComponentScan

- 作用：用于通过注解指定 Spring 在创建容器时要扫描的包
- 属性：
  - value：它和 basepackages 的作用是一样的，都是用于指定创建容器时要扫描的包。使用类路径

使用此注解等同于XML中配置了：

```xml
<context:component-scan base-package="com.itheima"></context:component-scan>
```



问题：但此时对queryRunner的创建还不能与xml配置等价，因为配置类中的方法不能将创建了的对象存入Spring容器中。

解决方法：在创建对象的方法上使用@Bean注解

#### @Bean

- 作用：用于把当前方法的返回值作为 bean 对象放入 Spring 的IOC容器中
- 属性：
  - name：**用于指定bean的id**。当不写时，默认值是当前方法的名称。

此时与xml配置创建bean等价

细节：当使用注解配置方法时，如果方法有参数，Spring 框架会去容器中查找有没有可用的 bean 对象，查找的方式和 Autowired 注解的作用是一样的。



注意：此时虽然bean对象的注入都已经通过注解实现，但是bean.xml文件还是不能删除。因为容器的创建还是依赖于xml文件中的约束（就是从网上导入的一堆url）。

解决方法：创建AnnotationConfigApplicationContext。

测试类：

```java
public class AccountServiceTest {
  
      @Test
      public void testFindAll() {
          /* 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          现在使用AnnotationConfigApplicationContext创建容器，传入参数为具体的配置类
          */
          ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
          // 2.得到业务层对象
          IAccountService iAccountService = ac.getBean("accountService",IAccountService.class);
          // 3.执行方法
          List<Account> accounts = iAccountService.findAllAccount();
          for (Account account : accounts) {
              System.out.println(account);
          }
      }
}
```

#### @Import

- 作用：用于导入其他的配置类
- 属性：
  - value：用于指定其他配置类的字节码

使用 Import 的注解之后，有 Import 注解的类就是父配置类，而导入的都是子配置类（父子关系的配置关系更为合理）

```java
@ComponentScan("com.itheima")
@Import(JdbcConfig.class)
public class SpringConfiguration{
    //配置信息
}
```

仍存在的问题：数据库连接的信息又硬编码到java文件中

解决方法：创建.properties文件，并将连接的配置信息写在properties文件中，并使用@Value注解将信息加载到配置类的对应属性上。接下来，在父配置类上加注解@PropertySource。

#### @PropertySrouce

- 作用：用于指定 properties 文件的位置
- 属性：
  - value：指定文件的路径和名称
  - value中的关键字：classpath（表示为类路径下）

properties中：

```properties
jdbc.driver=com.mysql.jdbc.driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=xwl
jdbc.password=123456
```

配置类中：

```java
public class JdbcConfig{
    @Value(${jdbc.dirver})
    private String driver;
    
    @Value(${jdbc.url})
    private String url;
    
    @Value(${jdbc.username})
    private String username;
    
    @Value(${jdbc.password})
    private String password;
    
     /*
    用于创建一个QueryRunner对象
    */
    @Bean(name="runner")
    @Scope("prototype")				//使之称为多例模式
    public QueryRunner createQueryRunner(DateSource dataSource){
        return new QueryRunner(dataSrouce);
    }
    
    /*
    创建数据源对象
    */
    @Bean(name="dataSrouce")
    public DateSource createDateSrouce(){
        try{
            //使用注入的数据进行配置
        	ComboPooledDataSource ds = new CombaPooledDataSource();    
        	ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
        }catch(Exception e){
            throw new RuntimeException(e);
        } 
    }
}
```

父配置类：

```java
@CompenentScan("com.itheima")
@Import(JdbcConfig.class)
@PropertySource("classpath:jdbcConfig.properties")
public class SpringConfiguration{
    //父类注解
}
```

问题：其实纯注解的实现IOC容器，反而会更麻烦。

总结：

- 在jar包中的类，使用xml进行配置
- 自己实现的java类，使用注解进行配置



### 5. 单元测试Junit

问题：在测试类中仍然需要编写很多和测试无关的代码。比如创建容器，创建bean等。而测试工程师不一定了解Spring框架，其工作重点应放在测试上。

#### 1. 相关知识

1.应用程序的入口：main方法

2.Junit单元测试中，没有main方法也能执行。Junit集成了一个main方法，该方法会判断当前测试类中哪些方法有@Test注解。Junit就会让有@Test注解的方法执行。

3.Junit不管我们是否会使用Spring框架。在执行测试方法时，Junit根本不知道我们是否使用了Spring框架，所以也就不会为我们读取配置文件/配置类来创建Spring核心容器。

4.由上述可知，当测试方法执行时，没有IOC容器，就算写了@Autowired注解，也无法实现注入。因此会出现空指针异常问题。

#### 2. Spring整合Junit

##### 1. 导入spring整合Junit的jar包

*注意Spring5要求Junit的版本在4.12以上

```xml
<dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2RELEASE</version>
 </dependency>
 
 <dependency>
     <gourpId>junit</gourpId>
     <artifactId>junit</artifactId>
     <version>4.12</version>
</dependency>
```

##### 2. 使用Junit提供的注解替换原有的main方法

@RunWith

作用：替换测试类的main方法

##### 3. 告知Spring运行器

@ContextConfiguration

- 作用：告知Spring的运行器，Spring的IOC容器创建是基于xml还是注解的，并且说明位置
- 属性：
  - locations：指定xml文件的位置，加上classpath关键字，表示在类路径下
  - classes：指定注解类所在的位置

##### 4. @Autowired注入

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class AccountServiceTest{
    
    @Autowired
    public IAccountService as;
    
    @Test
    public void test(){
        //测试内容
    }
}
```

*如果使用Springboot，可以少去第三部的操作



## 二、面向切面编程AOP

### 1. 事务

![](D:\技术\学习笔记\Spring\pic\事务1.PNG)

查看此段代码，在两个更新操作之间抛出了异常，导致了数据库中数据的不一致。而该方法有四个对数据库的连接的对象，他们应该一起成功或一起失败。

![](D:\技术\学习笔记\Spring\pic\事务控制.png)

需要使用ThreadLocal对象把Connection和当前线程绑定，从而使一个线程中只有一个能控制事务的对象。

**事务的控制应该都在服务层（service）**

1.使用工具类将线程和连接绑定

2.使用事务管理类管理事务

为了保证Dao类不会创建多个连接，使用工具类中的与线程绑定的连接方法作为query的第一个参数

最终的结果：工具类使用数据源；事务管理类和Dao类使用工具类；Service类使用事务管理类和Dao类。

3.在xml中配置工具类和事务管理类

问题：但是在每个Service方法中，都会有一些重复的步骤，例如建立连接、提交事务、回滚处理、释放连接等。

```java
@Override
    public List<Account> findAllAccount() {
        try {
            //1.开启事务
            txManager.beginTransaction();
            //2.执行操作
            List<Account> accounts = accountDao.findAllAccount();
            //3.提交事务
            txManager.commit();
            //4.返回结果
            return accounts;
        }catch (Exception e){
            //5.回滚操作
            txManager.rollback();
            throw new RuntimeException(e);
        }finally {
            //6.释放连接
            txManager.release();
        }
    }
```

### 2. 动态代理

![](D:\技术\学习笔记\Spring\pic\代理.png)

简单理解，本来厂商可以自产自销，但是由于各种开销，最后厂商选择只生产产品，销售则交由各级经销商完成。

- 特点：字节码随用随创建，随用随加载

- 作用：不修改源码的基础上对方法增强

- 分类：

  基于接口的动态代理

  基于子类的动态代理

#### 2.1 基于接口的动态代理

1. 基于接口的动态代理：

   涉及的类：Proxy

   提供者：JDK官方

2. 如何创建代理对象：

   使用Proxy类中的newProxyInstance方法

3. 创建代理对象的要求：

   被代理类最少实现的一个接口，如果没有则不能使用

4. newProxyInstance方法的参数：

   ClassLoader : 用于加载代理对象字节码，和被代理对象使用相同的类加载器，固定写法

   Class [ ] : 用于让代理对象和被代理对象有相同的方法，**固定写法**

   InvocationHandler : 用于提供增强的代码

   它是让我们写如何代理。我们一般是写一个该接口的实现类，通常是匿名内部类，但不是必须的。此接口的实现类都是谁用谁写。

生产厂家接口IProducer：

```java
 //对生产厂家要求的接口
  public interface IProducer {
      /**
       * 销售
       * @param money
       */
      public void saleProduct(float money);
  
      /**
       * 售后
       * @param money
       */
      public void afterService(float money);
  }
```

生产者：

```java
 // 一个生产者
  public class Producer implements IProducer {
  
      /**
       * 销售
       * @param money
       */
      public void saleProduct(float money) {
          System.out.println("销售产品，并拿到钱：" + money);
      }
  
      /**
       * 售后
       * @param money
       */
      public void afterService(float money) {
          System.out.println("提供售后服务，并拿到钱：" + money);
      }
  }
```

消费者：

```java
 /**
   * 模拟一个消费者
   */
  public class Client {
      public static void main(String[] args) {
          final Producer producer = new Producer();
  
          IProducer proxyProducer = (IProducer) Proxy.newProxyInstance(producer.getClass().getClassLoader(),
                  producer.getClass().getInterfaces(), new InvocationHandler() {
                      /**
                       * 作用：执行被代理对象的任何接口方法都会经过该方法
                       * 方法参数的含义：
                       * @param proxy         代理对象的含义
                       * @param method        当前执行的方法
                       * @param args          当前执行方法的参数
                       * @return              和被代理对象方法有相同的返回值
                       * @throws Throwable
                       */
                      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                          // 提供增强的代码
                          Object returnValue = null;
                          // 1.获取方法执行的参数
                          Float money = (Float)args[0];
                          // 2.判断当前方法是不是销售
                          if ("saleProduct".equals(method.getName())) {
                              returnValue = method.invoke(producer,money * 0.8f);
                          }
                          return returnValue;
                      }
                  });
          proxyProducer.saleProduct(10000f);		//实际会返回8000
      }
  }
```



#### 2.2 基于子类的动态代理

需要依赖：

```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>2.1_3</version>
</dependency>
```

1. 基于子类的动态代理：

   涉及的类：Enhancer

   提供者：第三方 cglib 库

2. 如何创建代理对象：

   使用 Enhancer 类中的 create 方法

3. 创建代理对象的要求：

   被代理类不能是最终类

4. create 方法的参数：

   Class : 它是用于被指定代理对象的字节码，固定写法

   callback : 用于提供增强的代码

   它是让我们写如何代理。我们一般是写一个该接口的实现类，通常是匿名内部类，但不是必须的。此接口的实现类都是谁用谁写。我们一般写的都是该接口的子实现类：MethodInterCeptor

生产者：

```java
 public class Producer {
      /**
       * 销售
       * @param money
       */
      public void saleProduct(float money) {
          System.out.println("销售产品，并拿到钱：" + money);
      }
  
      /**
       * 售后
       * @param money
       */
      public void afterService(float money) {
          System.out.println("提供售后服务，并拿到钱：" + money);
      }
  }
```

消费者：

```java
  /**
   * 模拟一个消费者
   */
  public class Client {
      public static void main(String[] args) {
          final Producer producer = new Producer();
          Producer cglibProducer = (Producer) Enhancer.create(producer.getClass(), new MethodInterceptor() {
              public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                  // 提供增强的代码
                  Object returnValue = null;
                  // 1.获取方法执行的参数
                  Float money = (Float)args[0];
                  // 2.判断当前方法是不是销售
                  if ("saleProduct".equals(method.getName())) {
                      returnValue = method.invoke(producer,money * 0.8f);
                  }
                  return returnValue;
              }
          });
          cglibProducer.saleProduct(12000f);
      }
  }
```

#### 2.3 动态代理提升开发Service效率

通过动态代理，在service的类中已经不再需要写大量代码，而是使用Dao提供的方法即可完成事务。

所有方法的增强放在实现动态代理的BeanFactory中实现。

```java
//用于创建Service的代理对象的工厂
public class BeanFactory {
    
    private IAccountService accountService;

    private TransactionManager txManager;

    public void setTxManager(TransactionManager txManager) {
        this.txManager = txManager;
    }


    public final void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取Service代理对象
     * @return
     */
    public IAccountService getAccountService() {
        return (IAccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(),
                new InvocationHandler() {
                    /**
                     * 添加事务的支持
                     *
                     * @param proxy
                     * @param method
                     * @param args
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        if("test".equals(method.getName())){
                            return method.invoke(accountService,args);
                        }

                        Object rtValue = null;
                        try {
                            //1.开启事务
                            txManager.beginTransaction();
                            //2.执行操作
                            rtValue = method.invoke(accountService, args);
                            //3.提交事务
                            txManager.commit();
                            //4.返回结果
                            return rtValue;
                        } catch (Exception e) {
                            //5.回滚操作
                            txManager.rollback();
                            throw new RuntimeException(e);
                        } finally {
                            //6.释放连接
                            txManager.release();
                        }
                    }
                });
    }
}
```

*当然，其中的bean对象需要在xml中配置.



### 3. AOP的概念

在单体架构下的软件开发中，一个大型项目通常是依照功能拆分成各个模块。但是如日志、安全和事务管理此类重要且繁琐的开发却没有必要参与到各个模块中，将这些功能与业务逻辑相关的模块分离就是面向切面编程所要解决的问题。AOP就是**把程序中重复的代码抽取出来**，在需要执行的时候，使用动态代理技术，在不修改源码的基础上，对已有方法进行增强。

作用：在程序运行期间，不修改源码对已有方法进行增强

优势：

- 减少重复代码
- 提升开发效率
- 维护方便

#### 3.1 相关术语

- Advice (通知/增强)：所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知。 通知的类型：前置通知，后置通知，异常通知，最终通知，环绕通知（整个invoke方法，在环绕通知中有明确的切入点方法调用）。（例：TransactionManager中的方法，即公共代码）。
- Joinpoint (连接点)：所谓连接点是指那些被拦截到的点。在 Spring 中,这些点指的是方法,因为 Spring 只支持方法类型的连接点。（所有方法）
- Pointcut (切入点)：所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义（被增强的方法）。
- Introduction (引介): 引介是一种特殊的通知在不修改类代码的前提下，Introduction 可以在运行期为类动态地添加一些方法或字段。 
- Target(目标对象): 代理的目标对象（例：AccountService对象）。
- Weaving (织入): 是指把增强应用到目标对象来创建新的代理对象的过程。 Spring 采用动态代理织入，而 AspectJ 采用编译期织入和类装载期织入。
- Proxy（代理）: 一个类被 AOP 织入增强后，就产生一个结果代理类。 
- Aspect(切面)：是切入点和通知（引介）的结合（用配置说明通知和切入点如何组织，先后次序或者异常和最终等）。

![](D:\技术\学习笔记\Spring\pic\通知的类型.jpg)

#### 3.2 基于XML的AOP

##### 常用属性

- 使用aop:config标签表明开始AOP的配置
- 使用aop:aspect标签表明配置切面
  - id属性：是给切面提供一个唯一标识
  - ref属性：是指定通知类bean的Id
- 在aop:aspect标签的内部使用对应标签来配置通知的类型
  - aop:before：表示配置前置通知
  - aop:after-returning：表示后置通知，在切入点方法正常执行之后值，它和异常通知永远只能执行一个
  - aop:after-throwing：表示异常通知
  - aop:after：表示最终通知，无论正常执行与否，都会在最后执行
  - method属性：用于指定Logger类中哪个方法是前置通知
  - pointcut属性：用于指定切入点表达式，该表达式的含义指的是对业务层中哪些方法增强
- 切入点表达式的写法：
  - 关键字：execution(表达式)
  - 表达式：访问修饰符  返回值  包名.包名.包名...类名.方法名(参数列表)
  - 例：public void com.itheima.service.impl.AccountServiceImpl.saveAccount()
  - 访问修饰符可以省略： 
  - 返回值可使用通配符，表任意返回值： 
  - 包名可以使用通配符，表示任意包。但是有几级包，就需要写几个*.
  - 包名可以使用..表示当前包及其子包
  - 类名和方法名都可以使用*来实现通配
  -  参数列表：
    - 可以直接写数据类型：
    - 基本类型直接写名称           int
    - 引用类型写包名.类名的方式   java.lang.String
    - 可以使用通配符表示任意类型，但是必须有参数
    - 可以使用..表示有无参数均可，有参数可以是任意类型

实际开发中切入点表达式的通常写法：切到业务层实现类下的所有方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 配置srping的Ioc,把service对象配置进来-->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl"></bean>

    <!--spring中基于XML的AOP配置步骤
        1、把通知Bean也交给spring来管理
        2、使用aop:config标签表明开始AOP的配置
        3、使用aop:aspect标签表明配置切面
                id属性：是给切面提供一个唯一标识
                ref属性：是指定通知类bean的Id。
        4、在aop:aspect标签的内部使用对应标签来配置通知的类型
               我们现在示例是让printLog方法在切入点方法执行之前之前：所以是前置通知
               aop:before：表示配置前置通知
                    method属性：用于指定Logger类中哪个方法是前置通知
                    pointcut属性：用于指定切入点表达式，该表达式的含义指的是对业务层中哪些方法增强

            切入点表达式的写法：
                关键字：execution(表达式)
                表达式：
                    访问修饰符  返回值  包名.包名.包名...类名.方法名(参数列表)
                标准的表达式写法：
                    public void com.itheima.service.impl.AccountServiceImpl.saveAccount()
                访问修饰符可以省略
                    void com.itheima.service.impl.AccountServiceImpl.saveAccount()
                返回值可以使用通配符，表示任意返回值
                    * com.itheima.service.impl.AccountServiceImpl.saveAccount()
                包名可以使用通配符，表示任意包。但是有几级包，就需要写几个*.
                    * *.*.*.*.AccountServiceImpl.saveAccount())
                包名可以使用..表示当前包及其子包
                    * *..AccountServiceImpl.saveAccount()
                类名和方法名都可以使用*来实现通配
                    * *..*.*()
                参数列表：
                    可以直接写数据类型：
                        基本类型直接写名称           int
                        引用类型写包名.类名的方式   java.lang.String
                    可以使用通配符表示任意类型，但是必须有参数
                    可以使用..表示有无参数均可，有参数可以是任意类型
                全通配写法：
                    * *..*.*(..)

                实际开发中切入点表达式的通常写法：
                    切到业务层实现类下的所有方法
                        * com.itheima.service.impl.*.*(..)
    -->

    <!-- 配置Logger类 -->
    <bean id="logger" class="com.itheima.utils.Logger"></bean>

    <!--配置AOP-->
    <aop:config>
        <!--配置切面 -->
        <aop:aspect id="logAdvice" ref="logger">
            <!-- 配置通知的类型，并且建立通知方法和切入点方法的关联-->
            <aop:before method="printLog" pointcut="execution(* com.itheima.service.impl.*.*(..))"></aop:before>
        </aop:aspect>
    </aop:config>

</beans
```

##### 配置切入点表达式：

简化配置中的复杂性

```xml
<aop:pointcut id="pt1" expression="execution(* com.itheima.service.impl.*.*(..))"></aop:pointcut>
```

之后便可以在配置通知中使用这个切入点表达式的id，但是只能当前切面使用

若写在aop:aspect外部且必须放在其之前，则所有切面都可以使用。

```xml
<aop:config>
        <!-- 配置切入点表达式 id属性用于指定表达式的唯一标识。expression属性用于指定表达式内容
              此标签写在aop:aspect标签内部只能当前切面使用。
              它还可以写在aop:aspect外面，此时就变成了所有切面可用
          -->
        <aop:pointcut id="pt1" expression="execution(* com.itheima.service.impl.*.*(..))"></aop:pointcut>
        <!--配置切面 -->
        <aop:aspect id="logAdvice" ref="logger">
            <!-- 配置前置通知：在切入点方法执行之前执行
            <aop:before method="beforePrintLog" pointcut-ref="pt1" ></aop:before>-->

            <!-- 配置后置通知：在切入点方法正常执行之后值。它和异常通知永远只能执行一个
            <aop:after-returning method="afterReturningPrintLog" pointcut-ref="pt1"></aop:after-returning>-->

            <!-- 配置异常通知：在切入点方法执行产生异常之后执行。它和后置通知永远只能执行一个
            <aop:after-throwing method="afterThrowingPrintLog" pointcut-ref="pt1"></aop:after-throwing>-->

            <!-- 配置最终通知：无论切入点方法是否正常执行它都会在其后面执行
            <aop:after method="afterPrintLog" pointcut-ref="pt1"></aop:after>-->

            <!-- 配置环绕通知 详细的注释请看Logger类中-->
            <aop:around method="aroundPringLog" pointcut-ref="pt1"></aop:around>
        </aop:aspect>
    </aop:config>
```



##### 环绕通知

标签：aop:around

问题：当配置了环绕通知之后，切入点方法没有执行，而通知方法执行了。

分析：通过对比动态代理中的环绕通知代码，发现动态代理的环绕通知有明确的切入点方法调用，而现在的环绕通知中没有。

解决：Spring框架提供了一个接口（ProceedingJoinPoint）。该接口有一个方法proceed()，此方法就相当于调用切入点方法。该接口可作为环绕通知的方法参数。

Spring环绕通知：Spring框架为我们提供的一种可以在代码中手动控制增强方法何时执行的方式，自己编码实现而不用xml配置。

```java
public Object aroundPringLog(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try{
            Object[] args = pjp.getArgs();//得到方法执行所需的参数

            System.out.println("Logger类中的aroundPringLog方法开始记录日志了。。。前置");

            rtValue = pjp.proceed(args);//明确调用业务层方法（切入点方法）

            System.out.println("Logger类中的aroundPringLog方法开始记录日志了。。。后置");

            return rtValue;
        }catch (Throwable t){
            System.out.println("Logger类中的aroundPringLog方法开始记录日志了。。。异常");
            throw new RuntimeException(t);
        }finally {
            System.out.println("Logger类中的aroundPringLog方法开始记录日志了。。。最终");
        }
    }
```

问题：注解方法若不使用自己编写的环绕通知，则会产生最终通知和异常/后置通知次序颠倒的错误，这是无法避免的，所以推荐使用环绕通知。



#### 3.3 基于注解的AOP

```jAVA

```







## 三、事务控制

### 1. 声明式事务控制

#### 1. 基于XML的事务控制

```
1、配置事务管理器
2、配置事务的通知
        此时需要导入事务的约束 tx名称空间和约束，同时也需要aop的
        使用tx:advice标签配置事务通知
            属性：
                id：给事务通知起一个唯一标识
                transaction-manager：给事务通知提供一个事务管理器引用
3、配置AOP中的通用切入点表达式
4、建立事务通知和切入点表达式的对应关系
5、配置事务的属性
       是在事务的通知tx:advice标签的内部
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 配置业务层-->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!-- 配置账户的持久层-->
    <bean id="accountDao" class="com.itheima.dao.impl.AccountDaoImpl">
        <property name="dataSource" ref="dataSource"></property>
    </bean>


    <!-- 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/eesy"></property>
        <property name="username" value="root"></property>
        <property name="password" value="1234"></property>
    </bean>

     -->
    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 配置事务的通知-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <!-- 配置事务的属性
                isolation：用于指定事务的隔离级别。默认值是DEFAULT，表示使用数据库的默认隔离级别。
                propagation：用于指定事务的传播行为。默认值是REQUIRED，表示一定会有事务，增删改的选择。查询方法可以选择SUPPORTS。
                read-only：用于指定事务是否只读。只有查询方法才能设置为true。默认值是false，表示读写。
                timeout：用于指定事务的超时时间，默认值是-1，表示永不超时。如果指定了数值，以秒为单位。
                rollback-for：用于指定一个异常，当产生该异常时，事务回滚，产生其他异常时，事务不回滚。没有默认值。表示任何异常都回滚。
                no-rollback-for：用于指定一个异常，当产生该异常时，事务不回滚，产生其他异常时事务回滚。没有默认值。表示任何异常都回滚。
        -->
        <tx:attributes>
            <tx:method name="*" propagation="REQUIRED" read-only="false"/>
            <tx:method name="find*" propagation="SUPPORTS" read-only="true"></tx:method>
        </tx:attributes>
    </tx:advice>

    <!-- 配置aop-->
    <aop:config>
        <!-- 配置切入点表达式-->
        <aop:pointcut id="pt1" expression="execution(* com.itheima.service.impl.*.*(..))"></aop:pointcut>
        <!--建立切入点表达式和事务通知的对应关系 -->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
    </aop:config>
</beans>
```



#### 2. 基于注解的事务控制

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 配置spring创建容器时要扫描的包-->
    <context:component-scan base-package="com.itheima"></context:component-scan>

    <!-- 配置JdbcTemplate-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>



    <!-- 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/eesy"></property>
        <property name="username" value="root"></property>
        <property name="password" value="1234"></property>
    </bean>

    <!-- spring中基于注解 的声明式事务控制配置步骤
        1、配置事务管理器
        2、开启spring对注解事务的支持
        3、在需要事务支持的地方使用@Transactional注解-->
    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    
    <!-- 开启spring对注解事务的支持-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>

</beans>
```

```java
/**
 * 账户的业务层实现类
 *
 * 事务控制应该都是在业务层
 */
@Service("accountService")
@Transactional(propagation= Propagation.SUPPORTS,readOnly=true)	//只读型事务的配置
public class AccountServiceImpl implements IAccountService{

    @Autowired
    private IAccountDao accountDao;

    @Override
    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);

    }

    //需要的是读写型事务配置
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    @Override
    public void transfer(String sourceName, String targetName, Float money) {
        System.out.println("transfer....");
            //2.1根据名称查询转出账户
            Account source = accountDao.findAccountByName(sourceName);
            //2.2根据名称查询转入账户
            Account target = accountDao.findAccountByName(targetName);
            //2.3转出账户减钱
            source.setMoney(source.getMoney()-money);
            //2.4转入账户加钱
            target.setMoney(target.getMoney()+money);
            //2.5更新转出账户
            accountDao.updateAccount(source);

            int i=1/0;				//需要控制的异常

            //2.6更新转入账户
            accountDao.updateAccount(target);
    }
}
```



#### 3. 传播行为

事务的第一个方面就是传播行为。传播行为定义了客户端与被调用方法之间的事务边界。Spring定义了7中不同的传播行为，传播规则规定了何时要创建一个事务或何时使用已有的事务：

1. PROPAGATION_MANDATORY 表示该方法必须在事务中运行。如果当前事务不存在，则会抛出一个异常
2. PROPAGATION_NESTED 表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立与当前事务进行单独的提交或回滚
3. PROPAGATION_NEVER 表示当前方法不应该运行在事务上下文中，如果当前正在有一个事务运行，则会抛出异常
4. PROPAGATION_NOT_SUPPORTED 表示该方法不应该运行在事务中。
5. PROPAGATION_REQUIRED 表示当前方法必须运行在事务中。如果当前事务存在，方法将会在该事务中运行。否者，会启动一个新的事务
6. PROPAGATION_REQUIRES_NEW 表示当前方法必须运行在它自己的事务中。一个新的事务将被启动
7. PROPAGATION_SUPPORTS 表示当前方法不需要事务上下文，但是如果存在当前事务的话，那么该方法会在这个事务中运行

*方法A调用同一个类中的方法B，即使B的事务传播为REQUIREDS_NEW，Spring也不会为它创建新的事务。

#### 4. 隔离级别

声明式事务的第二个维度就是隔离级别。隔离级别定义了一个事务可能受其他并发事务影响的程度。多个事务并发运行，经常会操作相同的数据来完成各自的任务，但是可以回导致以下问题：

1. 脏读：事务A读取了事务B已经修改但为提交的数据。若事务B回滚数据，事务A的数据存在不一致的问题。
2. 不可重复读：书屋A第一次读取最初数据，第二次读取事务B已经提交的修改或删除的数据。导致两次数据读取不一致。不符合事务的隔离性。
3. 幻读：事务A根据相同条件第二次查询到的事务B提交的新增数据，两次数据结果不一致，不符合事务的隔离性。

理想情况下，事务之间是完全隔离的，从而可以防止这些问题的发生。但是完全的隔离会导致性能问题，因为它通常会涉及锁定数据库中的记录。侵占性的锁定会阻碍并发性，要求事务互相等待以完成各自的工作。因此为了实现在事务隔离上有一定的灵活性。因此，就会有多重隔离级别：

1. ISOLATION_DEFAULT 使用后端数据库默认的隔离级别
2. SIOLATION_READ_UNCOMMITTED 允许读取尚未提交的数据变更。可能会导致脏读、幻读或不可重复读
3. ISOLATION_READ_COMMITTED 允许读取并发事务提交的数据。可以阻止脏读，但是幻读或不可重复读仍可能发生
4. ISOLATION_REPEATABLE_READ 对同一字段的多次读取结果是一致的，除非数据是被本事务自己所修改，可以阻止脏读和不可重复读，但幻读仍可能发生
5. ISOLATION_SERIALIZABLE 完全服从ACID的事务隔离级别，确保阻止脏读、不可重复读、幻读。这是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关的数据库来实现的



##### @Transactional 事务实现机制

在应用系统调用声明了 @Transactional 的目标方法时，Spring Framework 默认使用 AOP 代理，在代码运行时生成一个代理对象，根据 @Transactional 的属性配置信息，这个代理对象决定该声明 @Transactional 的目标方法是否由拦截器 TransactionInterceptor 来使用拦截，在 TransactionInterceptor 拦截时，会在目标方法开始执行之前创建并加入事务，并执行目标方法的逻辑, 最后根据执行情况是否出现异常，利用抽象事务管理器 AbstractPlatformTransactionManager 操作数据源 DataSource 提交或回滚事务。

Spring AOP 代理有 CglibAopProxy 和 JdkDynamicAopProxy 两种，以 CglibAopProxy 为例，对于 CglibAopProxy，需要调用其内部类的 DynamicAdvisedInterceptor 的 intercept 方法。对于 JdkDynamicAopProxy，需要调用其 invoke 方法。

![Spring-transaction-mechanis](http://img.nextyu.com/2017-12-06-Spring-transaction-mechanism.png)

事务管理的框架是由抽象事务管理器 AbstractPlatformTransactionManager 来提供的，而具体的底层事务处理实现，由 PlatformTransactionManager 的具体实现类来实现，如事务管理器 DataSourceTransactionManager。不同的事务管理器管理不同的数据资源 DataSource，比如 DataSourceTransactionManager 管理 JDBC 的 Connection。

![Spring-TransactionManager-hierarchy-subtypes](http://img.nextyu.com/2017-12-06-Spring-TransactionManager-hierarchy-subtypes.png)