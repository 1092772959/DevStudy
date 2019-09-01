## 基础部分



### 五、docker

一个应用容器引擎。Docker支持将软件编译成一个镜像；然后在镜像中各种软件做好配置，将镜像发布出去，其他使用者可以直接使用这个镜像；

运行中的这个镜像称为容器，容器启动是非常快速的。



概念：

docker主机(Host)：安装了Docker程序的机器（Docker直接安装在操作系统之上）；

docker客户端(Client)：连接docker主机进行操作；

docker仓库(Registry)：用来保存各种打包好的软件镜像；

docker镜像(Images)：软件打包好的镜像；放在docker仓库中；

docker容器(Container)：镜像启动后的实例称为一个容器；容器是独立运行的一个或一组应用



使用Docker的步骤：

1）、安装Docker

2）、去Docker仓库找到这个软件对应的镜像；

3）、使用Docker运行这个镜像，这个镜像就会生成一个Docker容器；

4）、对容器的启动停止就是对软件的启动停止；

#### 1. 安装docker

系统：centos7，内核版本3.10以上

```bash
1、检查内核版本，必须是3.10及以上
uname -r
2、安装docker
yum install docker
3、输入y确认安装
4、启动docker
[root@localhost ~]# systemctl start docker
[root@localhost ~]# docker -v
Docker version 1.12.6, build 3e8e77d/1.12.6
5、开机启动docker
[root@localhost ~]# systemctl enable docker
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
6、停止docker
systemctl stop docker
```

#### 2. 常用操作

| 操作 | 命令                                            | 说明                                                     |
| ---- | ----------------------------------------------- | -------------------------------------------------------- |
| 检索 | docker  search 关键字  eg：docker  search redis | 我们经常去docker  hub上检索镜像的详细信息，如镜像的TAG。 |
| 拉取 | docker pull 镜像名:tag                          | :tag是可选的，tag表示标签，多为软件的版本，默认是latest  |
| 列表 | docker images                                   | 查看所有本地镜像                                         |
| 删除 | docker rmi image-id                             | 删除指定的本地镜像                                       |

https://hub.docker.com/







## 高级部分

### 一、缓存

#### 1. 概念

Java Caching定义了5个核心接口，分别是CachingProvider, CacheManager, Cache, Entry 和 Expiry

1.  CachingProvider定义了创建、配置、获取、管理和控制多个CacheManager。一个应用可 以在运行期访问多个CachingProvider。 
2. CacheManager定义了创建、配置、获取、管理和控制多个唯一命名的Cache，这些Cache 存在于CacheManager的上下文中。一个CacheManager仅被一个CachingProvider所拥有。 
3. Cache是一个类似Map的数据结构并临时存储以Key为索引的值。一个Cache仅被一个 CacheManager所拥有。 CRUD操作在Cache中。
4. Entry是一个存储在Cache中的key-value对。
5. Expiry 每一个存储在Cache中的条目有一个定义的有效期。一旦超过这个时间，条目为过期 的状态。一旦过期，条目将不可访问、更新和删除。缓存有效期可以通过ExpiryPolicy设置。

![](D:\技术\学习笔记\Spring\pic\缓存.PNG)



Cache接口下Spring提供了各种xxxCache的实现；如RedisCache，EhCacheCache , ConcurrentMapCache等

每次调用需要缓存功能的方法时，Spring会检查检查指定参数的指定的目标方法是否 已经被调用过；如果有就直接从缓存中获取方法调用后的结果，如果没有就调用方法 并缓存结果后返回给用户。下次调用直接从缓存中获取。 

#### 2. 重要注解

| Cache          | 缓存接口，定义缓存操作。实现有：RedisCache、EhCacheCache、ConcurrentMapCache等 |
| -------------- | ------------------------------------------------------------ |
| CacheManager   | 缓存管理器，管理各种缓存（Cache）组件                        |
| @Cacheable     | 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存     |
| @CacheEvict    | 清空缓存                                                     |
| @CachePut      | 保证方法被调用，又希望结果被缓存。                           |
| @EnableCaching | 开启基于注解的缓存，写在应用类上方                           |
| keyGenerator   | 缓存数据时key生成策略                                        |
| serialize      | 缓存数据时value序列化策略                                    |

#### 3. 简单步骤

1、开启基于注解的缓存

```java
@EnableCaching						//此处开启
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

2、标注缓存注解

```java
@Cacheable(cacheNames = {"pswd"})
//根据ID获取密码
public Object getUserById(Integer id){
    String res;
    if(stu.findByXh(id)!=null){
        res=stu.findByXh(id).getPassword();
        return res;
    }
    else return null;
}
```

- Cacheable
  - value/cacheNames：指定缓存的名字（可指定多个缓存名）；将方法的返回值放在哪个缓存中
  - key：缓存数据使用的key；可以用它来指定。如需指定则要使用SpEL表达式； #id表示获取参数id的值，等同于#root.args[0]、#a0、#p0
  - keyGenerator：key的生成器；可以自己指定key的生成器组件id（key和keyGenerator二选一）
  - cacheManager：指定缓存管理器；或cacheResolver，二选一
  - condition：指定符合条件的情况下才缓存，SpEL表达式
  - unless：否定缓存，为true时方法的返回值不会被缓存
  - sync：是否使用异步模式，异步模式时unless不起作用

运行流程：

1、方法运行前，先查询Cache，按照cacheNames指定的名字获取（cacheManager先获取相应的缓存）；第一次获取缓存会自动创建

2、区Cache中查找缓存的内容，使用一个key，默认就是方法的参数；key是按某种策略生成的，默认使用SimpleKeyGenerator生成key。

3、没有查到缓存就调用目标方法

4、将目标方法的返回结果放进缓存中



核心：

1. 使用CacheManager按照名字得到Cache组件，默认使用ConcurrentMap
2. key使用keyGenerator生成
3. 执行流程

自定义keyGenerator：

```java

@Configuration
public class MyKeyGenerator {

    @Bean("myKey")
    public KeyGenerator keyGenerator(){
        //由于是实现接口，所以要用匿名内部类的方式
        return new KeyGenerator(){

            @Override
            public Object generate(Object target, Method method, Object...params){
                return method.getName()+"["+ Arrays.asList()+"]";
            }
        };
    }
}
```

然后在需要缓存的方法上：

```java
	@Cacheable(cacheNames = {"pswd"}, keyGenerator = "myKey")
    //根据ID获取密码
    public Object getUserById(Integer id){
        String res;
        if(stu.findByXh(id)!=null){
            res=stu.findByXh(id).getPassword();
            return res;
        }
        else return null;
    }
```



- CachePut

既调用方法，又更新缓存数据，**默认在方法之后执行**；同步更新缓存

例如：更新1号员工，那么方法的返回值也将放进缓存里。

若保持使用的**Cache和key一致**，那么可以在保持数据一致性的情况下更新数据库中的数据。

```java
//获取用户头像路径
    @Cacheable(value="getIcon", key="#a0")
    public String getIcon(Integer xh){
        System.out.println("执行了icon查询");
        return this.stu.queryIconById(xh);
    }

    @CachePut(value="setIcon",key="#a0")
    public String updateIcon(Integer xh,String iconUrl){
        System.out.println("执行了icon修改");
        this.stu.updateIconUrlByXh(xh,iconUrl);
        return iconUrl;
    }
```

测试方法：

```java
@Test
    public void testCache(){

        System.out.println(server.getIcon(1101));
        String tmp = server.getIcon(1101);
        System.out.println(tmp);
        server.updateIcon(1101,"xiuwenLi");
        server.getIcon(1101);
        System.out.println(tmp);
    }

/**
原本数据库中的值为：lili
打印结果：

执行了icon查询
Hibernate: select icon_url from S where xh= ?
lili
lili
执行了icon修改
Hibernate: update s set icon_url=? where xh=?
xiuwenLi
*/
```



- CacheEvict

清除缓存

key：指定要清除的数据

```java
/*
        删除缓存中的数据
     */
    @CacheEvict(value="Icon", key="#a0")
    public void deleteIcon(Integer xh){
        System.out.println("执行了icon删除");
        //模拟icon删除
    }
```

测试方法

```java
@Test
public void testCache2(){
    String tmp = server.getIcon(1101);
    System.out.println(tmp);
    tmp = server.getIcon(1101);
    server.deleteIcon(1101);
    tmp = server.getIcon(1101);		//此时缓存中已经没有数据，需要执行方法
}
/**
打印结果：

执行了icon查询
Hibernate: select icon_url from S where xh= ?
xiuwenLi
执行了icon删除
执行了icon查询
Hibernate: select icon_url from S where xh= ?
*/
```

- 属性：
  - allEntries：为true时将该Cache中的所有数据都删除
  - beforeInvocation：缓存的清楚是否在方法之前执行，默认false表示在方法执行之后执行



#### 4. 使用Redis作中间件

常见操作数据类型：String、List（列表）、Set（集合）、Hash（散列）、ZSet（有序集合）

stringRedisTemplate.opsForValue()	：操作字符串

stringRedisTemplate.opsForList()：列表 





##### 配置

CacheManager == Cache 缓存组件来实际给缓存中存取数据

1. 引入redis的starter，容器中保存的是 RedisCacheManager
2. RedisCacheManager 帮我们创建RedisCache来作为缓存组件；RedisCache通过缓存数据
3. 默认保存数据 k-v 都是Object；利用序列化保存

![](D:\技术\学习笔记\Spring\pic\redis缓存结果.png)

如何保存为Json？

1. 引入了redis的starter，cacheManager变为RedisCacheManager；
2. 默认创建的 redisCacheManager在操作redis的时候，使用的是 redisTemplate<Object,Object>





pom.xml：

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
 </dependency>
```



配置类：

```java



@Configuration
public class MyRedisConfig {

    private Duration timeToLive = Duration.ZERO;
    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }


    @Bean(name="redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();


        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(redisSerializer);
        //value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }

    /**
   		保证redis作为缓存中间件，不会产生乱码
    */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
}
```

服务层：

```java

@Transactional
@Service
public class StudentServer {

    @Autowired
    StudentRepository stu;


    //使用缓存
    @Cacheable(cacheNames = {"pswd"}, keyGenerator = "myKey")
    //根据ID获取密码
    public Object getUserById(Integer id){
        String res;
        if(stu.findByXh(id)!=null){
            res=stu.findByXh(id).getPassword();
            return res;
        }
        else return null;
    }

    public List<Student> getAll(){
        return stu.findAll();
    }

    //保存(新建或更新)
    public void save(Student stu){
        this.stu.save(stu);
    }



    //获取用户头像路径
    @Cacheable(value="Icon", key="#a0")
    public String getIcon(Integer xh){
        System.out.println("执行了icon查询");
        return this.stu.queryIconById(xh);
    }

    @CachePut(value="Icon",key="#a0")
    public String updateIcon(Integer xh,String iconUrl){
        System.out.println("执行了icon修改");
        this.stu.updateIconUrlByXh(xh,iconUrl);
        return iconUrl;
    }

    /*
        删除缓存中的数据
     */
    @CacheEvict(value="Icon", key="#a0")
    public void deleteIcon(Integer xh){
        System.out.println("执行了icon删除");
        //模拟icon删除
    }
}
```



##### Redis工具类



### 二、消息队列

作用：异步通信、应用解耦、流量削峰（秒杀系统的实现）

重要概念：

1. 消息代理（消息中间件的服务器）
2. 目的地

消息队列主要有两种形式的目的地：

1. 队列：点对点通信
2. 主题：发布/订阅消息通信

点对点式：消息发送者发送消息，消息代理将其放入一个队列中，消息接收者从队列中获取消息内容，之后消息移出队列；消息只有唯一的发送者和**接受者**，但并不只能有一个**接收者**

#### 1. RabbitMQ

Message：消息，不具名。由消息头和消息体组成。消息体不透明，消息头由一系列可选属性组成

- routing-key(路由键)、priority（相对于其他消息的优先权）、delivery-mode（指出该消息可能需要持久性存储）等

publisher：消息的生产者，也是一个向交换器发布消息的客户端应用程序

exchane：交换器，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。

- 四种类型：direct（默认）、fanout、topic和headers，不同类型的exchange转发消息的策略有所区别。

![](D:\技术\学习笔记\Spring\pic\消息代理.PNG)

##### Exchange类型

1、direct（单播）

![](D:\技术\学习笔记\Spring\pic\direct.PNG)

消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配，如果一个队列绑定到交换机要求路由键为 “dog”，则只转发 routing key 标记为“dog”的消息，不会转 发“dog.puppy”，也不会转发“dog.guard”等等。它是完全匹配、单播的模式。

2、fanout（广播）

![](D:\技术\学习笔记\Spring\pic\fanout.PNG)

每个发到 fanout 类型交换器的消息都会分到所 有绑定的队列上去。fanout 交换器不处理路由键， 只是简单的将队列绑定到交换器上，每个发送 到交换器的消息都会被转发到与该交换器绑定的所有队列上。很像子网广播，每台子网内的 主机都获得了一份复制的消息。fanout 类型转发消息是最快的。

3、topic（选择性广播）

![](D:\技术\学习笔记\Spring\pic\tpoic.PNG)

topic 交换器通过模式匹配分配消息的路由键属 性，将路由键和某个模式进行匹配，此时队列需要绑定到一个模式上。它将路由键和绑定键 的字符串切分成单词，这些单词之间用点隔开。 它同样也会识别两个通配符：符号“#”和符号 “* ” 。 # 匹配 0 个或多个单词 ， *匹配一个单词。



#### 2. 自动配置

1. RabbitAutoConfiguration
2. 有自动配置了连接工厂ConnnectionFactory
3. RabbitProperties 封装了 RabbitMQ的配置
4. RabbitTemplate：给rabbitMQ发送和接收消息
5. AmqpAdmin：rabbitMQ系统管理功能组件

管理界面端口：15672

#### 3. 简单步骤

1、pom.xml

```xml
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```

2、rabbit-properties

```properties

#rabbitMQ配置信息
spring.rabbitmq.host = 123.207.25.93
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
#端口默认5672
#virtualHost默认 "/"
```



3、测试类

```java
	//注入依赖
	@Autowired
    private RabbitTemplate rabbitTemplate;

	/**
     * RabbitMQ
     * 1. 单播
     */
    @Test
    public void testRabbit(){
        /**
         *
            Message需要自己构造；定义消息体和消息头
            rabbitTemplate.send(exchange,routeKey,message);

            常用convertAndSend, 帮我们自动序列化Object对象， object为消息体
            rabbitTemplate.convertAndSend(exchange,routeKey,object);
         */
        List<Student> res = server.getAll();
        //此时在消息队列中的消息体为字节码
        rabbitTemplate.convertAndSend("exchange.direct","xwl.news",res);
    }

    /**
     *  选择消息队列,接收数据,并直接转化为Object
     */
    @Test
    public void receive(){
        Object res = rabbitTemplate.receiveAndConvert("xwl.news");
        System.out.println(res.getClass());
        System.out.println(res);
    }

	/**
     * 2、广播
     */
    @Test
    public void sendMsg(){
        List<Student> res = server.getAll();
        rabbitTemplate.convertAndSend("exchange.fanout","",res);
    }
```

4、使序列化结果不为字节码

自定义配置类

```java
@Configuration
public class MyAMQPConfig {

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

```

5、基于注解的消息监听

1) 开启注解

```java
@EnableRabbit       //开启基于注解的RabbitMQ
@EnableCaching      //开启基于注解的cache
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

2) 方法上用@RabbitListener注解，并指明接收的队列名

```java
@Service
public class MessageService {

    //消息监听
    @RabbitListener(queues = "xwl.news")
    public void receive(Student stu){
        System.out.println("收到学生消息："+stu);
    }

    //接收完整Message
    public void reveive2(Message message){
        //消息体的字节数组
        System.out.println(message.getBody());
        //消息体的头信息
        System.out.println(message.getMessageProperties());
    }
}
```

#### 4. AmqpAdmin组件

若需要在程序中创建和删除交换器、绑定规则、消息队列等，可以使用它。

可直接使用AmqpAdmin组件（已在RabbitAutoConfiguration中注入）

```java
	@Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){
        //新建交换器和队列
        amqpAdmin.declareExchange(new DirectExchange("new-xwl"));
        amqpAdmin.declareQueue(new Queue("myQueue",true));
        //绑定
        amqpAdmin.declareBinding(new Binding("myQueue", Binding.DestinationType.QUEUE,"new-xwl",
                "new-xwl",null));
    }

```







### 三、任务

### 1. 定时任务





### 2. 邮件服务

#### 1. 配置

pom.xml

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

application.properties

```properties
spring.mail.host=smtp.163.com
spring.mail.username=13120716616@163.com
spring.mail.password=						#写重置之后的密码
spring.mail.default-encoding=UTF-8

mail.fromMail.addr=13120716616@163.com
```

#### 2. 简单邮件

```java
@Component
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    //将properties中的发送者地址注入
    @Value("${mail.fromMail.addr}")
    private String host;

    /**
    to:接收者的地址
    subject:主题
    content:文本内容
    */
    public void sendSimpleMail(String []to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
        }
    }
}
```

#### 3. 附件发送

```java
//添加附件
    public void sendAttachedMail(String []to, String subject, String content){
        // MimeMessage 本身的 API 有些笨重，我们可以使用 MimeMessageHelper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            // 第二个参数是 true ，表明这个消息是 multipart类型的/
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(host);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);


            //添加附件,第一个参数表示添加到 Email 中附件的名称，第二个参数是图片资源
            mimeMessageHelper.addAttachment("boot.png", new ClassPathResource("static/img/defaultPic.jpg"));
            mailSender.send(mimeMessage);
            logger.info("简单邮件已经发送。");
        }catch(Exception e) {
            e.printStackTrace();
            logger.error("发送简单邮件时发生异常！", e);
        }
    }
```

#### 4. 富文本



