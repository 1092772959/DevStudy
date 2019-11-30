## 常用配置

### ssh免密登陆

- 在本地生成rsa公钥

```bash
ssh-keygen

```

- 远程服务器创建ssh目录(if not exsits)

mkdir -p ~/.ssh *//-p选项表示遇到不存在的目录自动创建*

- 将本地创建的ssh公钥放到服务器上

```bash
scp ~/.ssh/id_rsa.pub lixiuwen.xwl@10.227.9.11:~/.ssh/

```

- 将服务器上的公钥id_rsa.pub的内容复制到服务器~/.ssh/authorized_keys中

```bash
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

```

- 修改`authorized_keys`文件权限

```bash
chmod 600 ~/.ssh/authorized_keys
```

- 在本地创建登陆配置文件

```bash
#编辑/etc/hosts文件
10.227.9.11 	devbox
```

- 通过别名登陆

```bash
ssh lixiuwen.xwl@devbox
```





## 安装应用

### pip

#### Linux

- 通过脚本安装

```bash
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
python get-pip.py
```



- 通过Linux软件包安装

```bash
#Debian/Ubuntu
#Python 2:
sudo apt install python-pip
#Python 3:
sudo apt install python3-venv python3-pip
```



- 更新pip

```bash
sudo pip3 install -U pip

```



- 卸载pip

```bash
sudo python -m pip uninstall pip
```



#### Mac

- 命令行输入

```bash
sudo easy_install pip
```


pip --version查看版本信息



- 卸载

```bash
sudo pip uninstall pip 
```







## 常用指令

### 权限

- chmod 

```bash
chmod [ugoa...][[+-=][rwxX]...][,...]
```

- u 表示该文件的拥有者，g 表示与该文件的拥有者属于同一个群体(group)者，o 表示其他以外的人，a 表示这三者皆是。
- \+ 表示增加权限、- 表示取消权限、= 表示唯一设定权限。
- r 表示可读取，w 表示可写入，x 表示可执行，X 表示只有当该文件是个子目录或者该文件已经被设定过为可执行。

例：

1. 将文件 file1.txt 设为所有人皆可读取 :

```bash
chmod ugo+r file1.txt
```

2. 将文件 file1.txt 设为所有人皆可读取 :

```bash
chmod a+r file1.txt
```

3. 将文件 file1.txt 与 file2.txt 设为该文件拥有者，与其所属同一个群体者可写入，但其他以外的人则不可写入 :

```bash
chmod ug+w,o-w file1.txt file2.txt
```



此外也可以用数字表示权限

语法为：

```
chmod abc file
```

其中a,b,c各为一个数字，分别表示User、Group、及Other的权限。

r=4，w=2，x=1

- 若要rwx属性则4+2+1=7；
- 若要rw-属性则4+2=6；
- 若要r-x属性则4+1=5。

```bash
chmod 777 file		# equels chmod a=rwx file
```

