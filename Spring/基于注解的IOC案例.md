### 1. 接口

```java
  public interface IAccountDao {
  
      /**
       * 查询所有
       * @return
       */
      List<Account> findAllAccount();
  
      /**
       * 查询一个
       * @return
       */
      Account findAccountById(Integer accountId);
  
      /**
       * 保存
       * @param account
       */
      void saveAccount(Account account);
  
      /**
       * 更新
       * @param account
       */
      void updateAccount(Account account);
  
      /**
       * 删除
       * @param accountId
       */
      void deleteAccount(Integer accountId);
  }
```



```java
  public interface IAccountService {
  
      /**
       * 查询所有
       * @return
       */
      List<Account> findAllAccount();
  
      /**
       * 查询一个
       * @return
       */
      Account findAccountById(Integer accountId);
  
      /**
       * 保存
       * @param account
       */
      void saveAccount(Account account);
  
      /**
       * 更新
       * @param account
       */
      void updateAccount(Account account);
  
      /**
       * 删除
       * @param accountId
       */
      void deleteAccount(Integer accountId);
  }
```

### 2. 实现类

```java
@Repository("accountDao") 
public class AccountDaoImpl implements IAccountDao {
  
     @Autowired 
     private QueryRunner runner;
  	
      public List<Account> findAllAccount() {
          try{
              return runner.query("select * from account", new BeanListHandler<Account>(Account.class));
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  
      public Account findAccountById(Integer accountId) {
          try{
              return runner.query("select * from account where id = ?", new BeanHandler<Account>(Account.class),accountId);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  
      public void saveAccount(Account account) {
          try{
              runner.update("insert into account(name, money) values(?,?)", account.getName(),account.getMoney());
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  
      public void updateAccount(Account account) {
          try{
              runner.update("update account set name = ?, money = ? where id = ?", account.getName(),account.getMoney(),account.getId());
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  
      public void deleteAccount(Integer accountId) {
          try{
              runner.update("delete from account where id = ?", accountId);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
```



```java
@Service("accountService")  
public class AccountServiceImpl implements IAccountService {
  		
    @Autowired
      private IAccountDao accountDao;
  
      public List<Account> findAllAccount() {
          return accountDao.findAllAccount();
      }
  
      public Account findAccountById(Integer accountId) {
          return accountDao.findAccountById(accountId);
      }
  
      public void saveAccount(Account account) {
          accountDao.saveAccount(account);
      }
  
      public void updateAccount(Account account) {
          accountDao.updateAccount(account);
      }
  
      public void deleteAccount(Integer accountId) {
          accountDao.deleteAccount(accountId);
      }
  }
```



### 3. 实体类

```java
 public class Account implements Serializable {
  
      private Integer id;
      private String name;
      private Float money;
  
      public void setId(Integer id) {
          this.id = id;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public void setMoney(Float money) {
          this.money = money;
      }
  
      public Integer getId() {
          return id;
      }
  
      public String getName() {
          return name;
      }
  
      public Float getMoney() {
          return money;
      }
  
      @Override
      public String toString() {
          return "Account{" +
                  "id=" + id +
                  ", name='" + name + '\'' +
                  ", money=" + money +
                  '}';
      }
  }
```

### 4. XML配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd">
                            
 <!-- 告诉Spring在创建容器时要自动扫描的包-->                           
<context:component-scan base-package="com.itheima"></context:component-scan>
                                                  
 <!--配置runner-->
 <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
     <!--注入数据源-->
     <constructor-arg name="ds" ref="dataSource"></constructor-arg>
 </bean>

 <!--配置数据源-->
 <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
     <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
     <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/eesy"></property>
     <property name="user" value="root"></property>
     <property name="password" value="HotteMYSQL"></property>
 </bean>
```

### 5. 测试类

```java
 public class AccountServiceTest {
  
      @Test
      public void testFindAll() {
          // 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          // 2.得到业务层对象
          IAccountService iAccountService = applicationContext.getBean("accountService",IAccountService.class);
          // 3.执行方法
          List<Account> accounts = iAccountService.findAllAccount();
          for (Account account : accounts) {
              System.out.println(account);
          }
      }
  
      @Test
      public void testFindOne() {
          // 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          // 2.得到业务层对象
          IAccountService iAccountService = applicationContext.getBean("accountService",IAccountService.class);
          // 3.执行方法
          Account account = iAccountService.findAccountById(1);
          System.out.println(account);
      }
  
      @Test
      public void testSave() {
          Account account = new Account();
          account.setName("test");
          account.setMoney(12345f);
          // 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          // 2.得到业务层对象
          IAccountService iAccountService = applicationContext.getBean("accountService",IAccountService.class);
          // 3.执行方法
          iAccountService.saveAccount(account);
      }
  
      @Test
      public void testUpdate() {
          // 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          // 2.得到业务层对象
          IAccountService iAccountService = applicationContext.getBean("accountService",IAccountService.class);
          // 3.执行方法
          Account account = iAccountService.findAccountById(4);
          account.setMoney(23456f);
          iAccountService.updateAccount(account);
      }
  
      @Test
      public void testDelete() {
          // 1.获取容器
          ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
          // 2.得到业务层对象
          IAccountService iAccountService = applicationContext.getBean("accountService",IAccountService.class);
          // 3.执行方法
          iAccountService.deleteAccount(4);
      }
  }
```

