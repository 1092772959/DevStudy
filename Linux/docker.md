### 基本概念

产生原因：开发到部署，**操作系统、环境和配置**都可能不同。Docker容器技术在任何操作系统上都是一致的，实现跨平台、跨服务器。只要一次配置好环境，换到别的机器上就可以一键部署。

镜像：运行文档、配置环境、运行环境、运行依赖包、操作系统发行版。



传统虚拟机：可以在一种操作系统中运行另一种操作系统。应用程序对此毫无感知；对于底层系统，虚拟机就是一个普通文件，不需要了就删掉，对其他部分毫无影响。使应用程序、操作系统和硬件三者之间的逻辑不变。

虚拟机的缺点：



容器虚拟化技术：



#### docker三要素

一.镜像（Image）

　　Docker镜像（Image）就是一个只读的模板，镜像可以用来创建Docker容器，一个镜像可以创建很多容器。

| Docker | 面向对象    |
| ------ | ----------- |
| 镜像   | 类（class） |
| 容器   | 实例对象    |

二.容器（Container）

　　1.Docker利用容器（Container）独立运行一个或一组应用

　　2.容器使用镜像创建的运行实例

　　3.容器可以被启动、开始、停止、删除，每个容器之间都是相互隔离的，保证平台的安全。

　　4.可以把容器看做是一个简易版的Linux环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。

　　5.容器的定义和镜像几乎一摸一样，也是一堆层的统一视角，唯一区别在于容器的最上面那一层是可读可写的。

三.仓库（Repository）

　　1.仓库（Repository）是集中存放镜像文件的场所。

　　2.仓库（Repository）和仓库注册服务器（Registry）是有区别的，仓库注册服务器上往往存放着多个仓库，每个仓库中又包含了多个镜像，每个镜像有不同的标签（tag）。

　　3.仓库分为公开仓库（Public）和私有仓库（Private）两种形式。

　　4.最大的公开仓库是Docker Hub（https://hub.docker.com/），存放了数量庞大的镜像供用户下载。

　　5.国内的公开仓库包括阿里云、网易云等。



### image

```bash
# 检索image  
$docker search image_name 
 
# 下载image  
$docker pull image_name 
 
# 列出镜像列表; -a, --all=false Show all images; --no-trunc=false Don't truncate output; -q, --quiet=false Only show numeric IDs  
$docker images  

# 删除一个或者多个镜像; -f, --force=false Force; --no-prune=false Do not delete untagged parents  
$docker rmi image_name  
提示Error response from daemon: No such image: zookeeper:latest使用docker rmi ID

# 显示一个镜像的历史; --no-trunc=false Don't truncate output; -q, --quiet=false Only show numeric IDs  
$docker history image_name  
```


