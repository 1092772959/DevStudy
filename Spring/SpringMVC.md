## 第一章：三层架构和MVC 

### 1. 三层架构 

开发服务器端程序，一般都基于两种形式，一种C/S架构程序，一种B/S架构程序 

使用Java语言基本上都是开发B/S架构的程序，B/S架构又分成了三层架构 

三层架构：

1.  表现层：WEB层，用来和客户端进行数据交互的。表现层一般会采用MVC的设计模型 
2. 业务层：处理公司具体的业务逻辑
3. 持久层：用来操作数据库

### 2. MVC模型 

MVC全名是Model View Controller 模型视图控制器，每个部分各司其职。 

1. Model：数据模型，JavaBean的类，用来进行数据封装。
2. View：指JSP、HTML用来展示数据给用户 
3. Controller：用来接收用户的请求，整个流程的控制器。用来进行数据校验等。 







### * Servlet

#### 四大作用域

![img](https://images2015.cnblogs.com/blog/955396/201607/955396-20160728213716091-1874759388.jpg)

##### 1. ServletContext域

1. 生命周期：当Web应用被加载进容器时创建代表整个web应用的ServletContext对象，当服务器关闭或Web应用被移除时，ServletContext对象跟着销毁。

2. 作用范围：整个Web应用。

3. 作用

   1. 在不同Servlet 之间转发：

      ```java
      this.getServletContext().getRequestDispatcher("/servlet/Demo10Servlet").forward(request, response);
      ```

      方法执行结束，service就会返回到服务器，再有服务器去调用目标servlet，其中request会重新创建，并将之前的request的数据拷贝进去。

   2. 读取静态资源文件：

      1. 由于相对路径默认相对的是java虚拟机启动的目录，所以我们直接写相对路径将会是相对于tomcat/bin目录，所以是拿不到资源的。

      ```java
      this.getServletContext().getRealPath(“/1.properties”)
          //给进一个资源的虚拟路径，将会返回该资源在当前环境下的真实路径。
      
      this.getServletContext().getResourceAsStream(“/1.properties”)
          //给一个资源的虚拟路径返回到该资源真实路径的流。
      ```

      2. 当在非servlet下获取资源文件时，就没有ServletContext对象用了，此时只能用类加载器   

      ```java
      classLoader.getResourceAsStream("../../1.properties")
          //此方法利用类加载器直接将资源加载到内存中，有更新延迟的问题，以及如果文件太大，占用内存过大。   
      classLoader.getResource("../1.properties").getPath()
          //直接返回资源的真实路径，没有更新延迟的问题。
      ```



##### 2. Request域

1. 生命周期：在service 方法调用前由服务器创建，传入service方法。整个请求结束，request生命结束。
2. 作用范围：整个请求链（请求转发也存在）。
3. 作用：  在整个请求链中共享数据。最常用到：在Servlet 中处理好的数据交给Jsp显示，此时参数就可以放置在Request域中带过去。

##### 3. Session域

1. 生命周期：在第一次调用 request.getSession() 方法时，服务器会检查是否已经有对应的session，如果有就在内存  中创建一个session并返回。当一段时间内session没有被使用（默认30分钟），则服务器会销毁该session。如果服务器非正常关闭（强行关闭），没有到期的session也会跟着销毁。如果调用session提供的invalidate()，可以立即销毁session。注意：服务器正常关闭，再启动，Session对象会进行钝化和活化操作。同时如果服务器钝化的时间在session 默认销毁时间之内，则活化后session还是存在的。否则Session不存在。如果JavaBean数据在session钝化时，没有实现Serializable 则当Session活化时，会消失。

1. 作用范围：一次会话。
2. 作用：为浏览器创建独一无二的内存空间，在其中保存会话相关的信息。

##### 4. PageContext域

1. 生命周期：当对JSP的请求时开始，当响应结束时销毁。
2. 作用范围：整个JSP页面，是四大作用域中最小的一个。
3. 作用：
   1. 获取其它八大隐式对象，可以认为是一个入口对象。
   2. 获取其所有域中的数据
   3. 跳转到其他资源











