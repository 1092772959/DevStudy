## Git常用指南(Win)

### 理论基础

Git本地有三个工作区域：工作目录（Working Directory）、暂存区(Stage/Index)、资源库(Repository或Git Directory)。如果在加上远程的git仓库(Remote Directory)就可以分为四个工作区域。

![img](https://images2017.cnblogs.com/blog/63651/201709/63651-20170905201017069-171460014.png)



- Workspace：工作区，就是你平时存放项目代码的地方
- Index / Stage：暂存区，用于临时存放你的改动，事实上它只是一个文件，保存即将提交到文件列表信息
- Repository：仓库区（或本地仓库），就是安全存放数据的位置，这里面有你提交到所有版本的数据。其中HEAD指向最新放入仓库的版本
- Remote：远程仓库，托管代码的服务器，可以简单的认为是你项目组中的一台电脑用于远程数据交换





*来自廖雪峰老师的教程

### 1. 安装

找<https://git-scm.com/downloads>下载并安装

### 2. 版本库

创建版本库

```bash
#进入项目目录
$ git init
```

将文件添加至版本库

首先这里再明确一下，所有的版本控制系统，其实只能跟踪文本文件的改动，比如TXT文件，网页，所有的程序代码等等，Git也不例外。版本控制系统可以告诉你每次的改动，比如在第5行加了一个单词“Linux”，在第8行删了一个单词“Windows”。而图片、视频这些二进制文件，虽然也能由版本控制系统管理，但没法跟踪文件的变化，只能把二进制文件每次改动串起来，也就是只知道图片从100KB改成了120KB，但到底改了啥，版本控制系统不知道，也没法知道。

不幸的是，Microsoft的Word格式是二进制格式，因此，版本控制系统是没法跟踪Word文件的改动的。



### 3. 添加文件至仓库

1.添加

```bash
git add .				#使用.表示将该目录下全部文件添加

#git add -u   保存修改和删除，但是不包括新建文件。

#git add -A   保存所有的修改
```

2.用命令`git commit`告诉Git，把文件提交到仓库：

```bash
git commit -m "wrote a readme file"			# -m 后是本次提交的说明
```

3.git rm

当我们需要删除`暂存区`或`分支`上的文件, 同时工作区也不需要这个文件了, 可以使用

```
1 git rm file_path
2 git commit -m 'delete somefile'
3 git push
```

当我们需要删除`暂存区`或`分支`上的文件, 但本地又需要使用, 只是不希望这个文件被版本控制, 可以使用

```
git rm --cached file_path
git commit -m 'delete remote somefile'
git push
```



### 4. 版本控制

这个在实际开发中会有重要作用



### 5. 远程仓库

#### 添加远程仓库

```bash
git remote add origin [url]
```

#### 上传

第一次上传文件：由于远程库是空的，我们第一次推送`master`分支时，加上了`-u`参数，Git不但会把本地的`master`分支内容推送的远程新的`master`分支，还会把本地的`master`分支和远程的`master`分支关联起来，在以后的推送或者拉取时就可以简化命令。

```bash
git push -u origin master		#第一次上传
```

之后上传

```bash
git push origin master			#仓库不为空
```

#### 下拉

`git pull` 命令基本上就是 `git fetch` 和 `git merge origin/master` 命令的组合体，Git 从指定的远程仓库中抓取内容，然后马上尝试将其合并进你所在的分支中。合并可能会遇到冲突，需要手动处理。



#### 克隆

```bash
git clone (url)
```



### 6. 分支管理

- 克隆远程仓库的某一个分支

```bash
git clone -b (分支名) (url)
```

- 查看项目的分支

```bash
git branch -a
```

- 删除本地分支

```bash
git branch -d <BranchName>
```

- 删除远程分支

```bash
git push origin --delete <BranchName>
```

- 新建本地分支

```bash
git branch 本地分支名
```

- 新建本地分支并切换到指定远程分支

```bash
 git checkout -b 本地分支名 origin/远程分支名
```

该命令可以将远程`git`仓库里的指定分支拉取到本地，这样就在本地新建了一个`dev`分支，并和指定的远程分支`release/caigou_v1.0`关联了起来。

- 将本地分支推送到远程

```bash
git push <远程主机名> <本地分支名>:<远程分支名>
```



#### 合并

- 从当前分支合并到指定分支

```bash
git merge <本地分支名>
```





### 7. gitignore

创建.gitignore文件，以springboot为例

```java
HELP.md
target/
!.mvn/wrapper/maven-wrapper.jar
/src/test/**
/src/main/resources/*.properties
/src/main/resources/*.yml

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### VS Code ###
.vscode/
```

若未即时生效则将缓存中的内容清除

```bash
git rm -r --cached .
git add .gitignore
git commit -m "update .gitignore"
```



### 8. submodule

ref: https://www.jianshu.com/p/0107698498af

- 添加子项目

```bash
git submodule add <submodule_url>  # 添加子项目

git submodule init  # 初始化		本地.gitmodules文件
git submodule update  # 同步远端submodule源码
```

添加子项目后会出现.gitmodules的文件，这是一个配置文件，记录mapping between the project's URL and the local subdirectory。且.gitmodules在git版本控制中，这样其他参与项目的人才能知道submodule projects的情况。

- clone主项目的同时获取所有化子项目代码

```bash
git clone --recurse-submodules <main_project_url>  # 获取主项目和所有子项目源码
```

如果获取的项目包含submodules，pull main project的时候不会同时获取submodules的源码，需要执行本地.gitmodules初始化的命令，再同步远端submodule源码。如果希望clone main project的时候包含所有submodules，可以使用下面的命令。

操作submodules源码：先进入submodule的direcotry，再执行下述命令

```shell
git fetch  # 获取submodule远端源码
git merge origin/<branch_name>  # 合并submodule远端源码
git pull  # 获取submodule远端源码合并到当前分支
git checkout <branch_name>  # 切换submodule的branch
git commit -am "change_summary"  # 提交submodule的commit

# or

# 更新submodule源码，默认更新的branch是master，如果要修改branch，在.gitmodule中设置
git submodule update --remote <submodule_name>  
# 更新所有submodule源码，默认更新.gitmodule中设置的跟踪分支，未设置则跟踪master
git submodule update --remote  
# 当submodule commits提交有问题的时候放弃整个push
git push --recurse-submodules=check
# 分开提交submodule和main project
git push --recurse-submodules=on-demand
```

.gitmodule内容大致如下

```bash
[submodule <submodule_name>]
    path = <local_directory>
    url = <remote_url>
    branch = <remote_update_branch_name>
```



### 9. LSF

Git LFS 是 Github 开发的一个 Git 的扩展，用于实现 Git 对大文件的支持

#### 安装

- **Linux**

1.  `curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash`
2. `sudo apt-get install git-lfs`
3. `git lfs install`

- **Mac**

1. 安装HomeBrew `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"` 
2. `brew install git-lfs`
3. `git lfs install`

- **Windows**

1. 下载安装 [windows installer](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Fgithub%2Fgit-lfs%2Freleases) 
2. 运行 windows installer
3. 在命令行执行 `git lfs install`



#### 首次使用

第一次使用前需要运行下 `git lfs install`，只要运行一次，以后都不需要了

#### 日常使用

需要用lfs管理的文件要添加到追踪列表里，一般而言，把某个类型的文件统一用lfs管理会是个好注意，例如我们把dll文件用lfs管理`git lfs track '*.dll'`

此时，仓库的根目录下会自动创建.gitattribute文件，里面就记录了使用lfs的文件后续添加新的类型可以用`git lfs track`命令，也可以直接编辑.gitattribute文件

**注意：.gitattribute文件需要添加到git仓库中进行版本管理**

文件追踪之后，后续的所有操作都是和git的普通操作一致了

#### 辅助命令

`git lfs ls-files`：查看当前有哪些文件是使用lfs管理的







