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

1.添加至stage

```bash
git add .				#使用.表示将该目录下全部文件添加，包括修改和新建，不包括删除的修改

#git add -u   保存修改和删除，但是不包括新建文件。

#git add -A   保存所有的修改	
```

2.用命令`git commit`告诉Git，把文件从stage提交到仓库：

```bash
git commit -m "wrote a readme file"			# -m 后是本次提交的说明
```

3.用`git log`查看提交记录

```bash
git log

#仅仅输出commit hash 前7个字符串和commit message.
git log --oneline

#在git log 的基础上输出文件增删改的统计数据。
git log --stat

#输出每个commit具体修改的内容，输出的形式以diff的形式给出。
git log -p 

#图形化
git log --graph --pretty=oneline --abbrev-commit
```



### 4. 版本控制

#### reset

**git reset --soft**

将HEAD引用指向给定提交。索引（暂存区）和工作目录的内容是不变的，在三个命令中对现有版本库状态改动最小。

**git reset --mixed（git reset默认的模式）**

HEAD引用指向给定提交，并且索引（暂存区）内容也跟着改变，工作目录内容不变。这个命令会将索引（暂存区）变成你刚刚暂存该提交全部变化是的状态，会显示工作目录中有什么修改。

**git reset --hard**

HEAD引用指向给定提交，索引（暂存区）内容和工作目录内容都会变给定提交时的状态。也就是在给定提交后所修改的内容都会丢失(新文件会被删除，不在工作目录中的文件恢复，未清除回收站的前提)。



#### commit --amend

可以对之前的提交进行修改而不改变commit记录

```bash
git commit --amend --no-edit

#之后在push的之后需要 -f
```



#### checkout

未使用git add将文件提交到stage时：

```shell
// 放弃单个文件修改,注意不要忘记中间的"--",不写就成了检出分支了!
git checkout -- filepathname
// 放弃所有的文件修改
git checkout .  
```

此命令用来放弃掉所有还没有加入到缓存区（就是 git add 命令）的修改：内容修改与整个文件删除。但是此命令不会删除掉刚新建的文件，因为刚新建的文件还没已有加入到 git 的管理系统中。



#### rm

将文件从stage中删除

```bash
git rm <file_name>
```



#### cherry-pick

将过去

```bash
git cherry-pick 4c805e2
```





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

如果提交到远程服务器，但之后想要进一步code review再合入，则使用：

```shell
git push origin HEAD:refs/for/master

# 不需要cr
git push origin HEAD:refs/heads
```



#### 下拉

`git pull` 命令基本上就是 `git fetch` 和 `git merge origin/master` 命令的组合体，Git 从指定的远程仓库中抓取内容，然后马上尝试将其合并进你所在的分支中。合并可能会遇到冲突，需要手动处理。



#### 克隆

```bash
git clone (url)
```



### 6. 分支管理

- 新建并切换分支

```bash
git switch -c dev

git switch master #仅切换分支
```



- 克隆远程仓库的某一个分支

```bash
git clone -b ${branch_name} ${url}
```

- 查看项目的分支

```bash
git branch -a

git branch -vv	#包含远程库分支
```

- 删除本地分支

```bash
git branch -d ${branch_name}
```

- 删除远程分支

```bash
git push origin --delete ${branch_name}
```

- 新建本地分支

```bash
git branch 本地分支名
```

- 新建本地分支并切换到指定远程分支

```bash
 git checkout -b 本地分支名 origin/${branch_name}
```

该命令可以将远程`git`仓库里的指定分支拉取到本地，这样就在本地新建了一个`dev`分支，并和指定的远程分支`release/caigou_v1.0`关联了起来。

- 已有本地分支，没有远程分支，创建并关联远程分支

```bash
git push --set-upstream origin ${branch_name}
```



- 将本地分支推送到远程

```bash
git push <远程主机名> <本地分支名>:<远程分支名>
```

#### 建立追踪关系

```shell
#手动建立追踪关系
git branch --set-upstream-to=<远程主机名>/<远程分支名> <本地分支名>
```

```shell
#push时建立追踪关系
git push -u <远程主机名> <本地分支名>
```

```shell
#新建分支时建立跟踪关系
git checkout -b <本地分支名> <远程主机名>/<远程分支名>
```



#### 合并

- 从当前分支合并到指定分支

```bash
git merge <本地分支名>		#把<本地分之名>中的代码合并到当前分支中
```

默认在合并时使用`Fast forward`模式，这样在记录中看不到合并的信息；如果要强制禁用`Fast forward`模式，Git就会在merge时生成一个新的commit，这样，从分支历史上就可以看出分支信息，可使用`--no-ff`方式commit

```bash
git merge --no-ff -m "merge with no-ff" dev
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



#### rm

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



### 9. LFS

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

后续添加新的类型可以用`git lfs track`命令，也可以直接编辑.gitattribute文件

**注意：.gitattribute文件需要添加到git仓库中进行版本管理**

文件追踪之后，后续的所有操作都是和git的普通操作一致了

#### 辅助命令

`git lfs ls-files`：查看当前有哪些文件是使用lfs管理的



###  10. stash

常用git stash命令：

（1）**git stash** save "save message" : 执行存储时，添加备注，方便查找，只有git stash 也要可以的，但查找时不方便识别。

（2）**git stash list** ：查看stash了哪些存储

（3）**git stash show** ：显示做了哪些改动，默认show第一个存储,如果要显示其他存贮，后面加stash@{$num}，比如第二个 git stash show stash@{1}

（4）**git stash show -p** : 显示第一个存储的改动，如果想显示其他存存储，命令：git stash show stash@{$num} -p ，比如第二个：git stash show stash@{1} -p

（5）**git stash apply** :应用某个存储,但不会把存储从存储列表中删除，默认使用第一个存储,即stash@{0}，如果要使用其他个，git stash apply stash@{$num} ， 比如第二个：git stash apply stash@{1} 

（6）**git stash pop** ：命令恢复之前缓存的工作目录，将缓存堆栈中的对应stash删除，并将对应修改应用到当前的工作目录下,默认为第一个stash,即stash@{0}，如果要应用并删除其他stash，命令：git stash pop stash@{$num} ，比如应用并删除第二个：git stash pop stash@{1}

（7）**git stash drop** stash@{$num} ：丢弃stash@{$num}存储，从列表中删除这个存储

（8）`**git stash clear** ：`删除所有缓存的stash







### others

- **git status** 查看对 repo 中哪些文件进行了修改。如果不在 repo 里但修改了的文件是不会出现在 **git status** 列表里的

- git add -u

  添加已经在 repo 中，但进行了修改的文件。比以下两个命令好用且安全得多：

  - 添加所有文件 **git add .** --> 容易误加文件
  - 手动添加一堆文件 **git add ** --> 效率太低

- **git show ** 显示某条 commit 的修改，不加 commit id, 则默认显示最近一条 commit

- **git diff ** 在还没 commit 前，查看修改的内容，filename 不加则默认显示所有文件的 diff

- **git stash** 缓存已经在 repo 里但做过修改的文件，经常用于项目修改到一半，需要 **git pull** 最新代码或是 hotfix 一些小的 bug。 使用 **git stash pop** 恢复这些修改。

- **git blame ** 可以看到某个文件每一行的最后修改者，方便追朔问题。比如看到某一行代码不太懂，可以 **git blame**，然后直接去询问最后一个修改者就行

- **git commit --amend** 可以修改最后一条 commit

- **git reset ** 用于去掉那些 git add 但还没 git commit 的文件 https://www.jianshu.com/p/c2ec5f06cf1a

- **git cherry-pick** 一般用于 merge 单条的经过 rebase 的 commit



#### ~ 和 ^

^符号表示某个commit父commit所在层中的第几个parent

```bash
git rebase -i HEAD~n	#rebase到HEAD的前n个commit

git rebase -i HEAD~1^1	#rebase到HEAD的前1个commit的第一个parent
```











