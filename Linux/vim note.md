简明教程

https://coolshell.cn/articles/5426.html



## 配置环境

1. 安装Vundle

通过git下载Vundle安装包

```bash
git clone https://github.com/gmarik/Vundle.vim.git  ~/.vim/bundle/Vundle.vim
```

2. 配置.vimrc文件

在~/.vimrc中加入自己的配置

```bash
Last login: Thu Dec  5 10:25:18 on ttys000
(base) localhost:~ lixiuwen$ ssh lixiuwen.xwl@devbox
Linux n227-009-011 4.14.81.bm.15-amd64 #1 SMP Debian 4.14.81.bm.15 Sun Sep 8 05:02:31 UTC 2019 x86_64

The programs included with the Debian GNU/Linux system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent
permitted by applicable law.
Last login: Thu Dec  5 10:45:17 2019 from 10.0.243.107
lixiuwen.xwl@n227-009-011:~$ ls -l
total 28
drwxr-xr-x 7 lixiuwen.xwl lixiuwen.xwl  4096 Dec  1 15:36 app_package
drwxr-xr-x 9 lixiuwen.xwl lixiuwen.xwl  4096 Dec  2 17:08 code
-rwxr-xr-x 1 lixiuwen.xwl lixiuwen.xwl 13109 Nov 21 18:02 go
drwxr-xr-x 6 lixiuwen.xwl lixiuwen.xwl  4096 Dec  1 15:10 list
lixiuwen.xwl@n227-009-011:~$ cd .vim/
lixiuwen.xwl@n227-009-011:~/.vim$ ls
bundle  vim.conf
lixiuwen.xwl@n227-009-011:~/.vim$ cd bundle/
lixiuwen.xwl@n227-009-011:~/.vim/bundle$ ls
indentpython.vim  Vundle.vim  YouCompleteMe
lixiuwen.xwl@n227-009-011:~/.vim/bundle$ cd ..
lixiuwen.xwl@n227-009-011:~/.vim$ cd ..
lixiuwen.xwl@n227-009-011:~$ vim .vimrc 

set nocompatible              " required
filetype off                  " required

" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()

" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
Plugin 'vim-scripts/indentpython.vim'
Plugin 'Valloric/YouCompleteMe'

" Add all your plugins here (note older versions of Vundle used Bundle instead of Plugin)
" All of your Plugins must be added before the following line
call vundle#end()            " required
filetype plugin indent on    " required
" au BufNewFile,BufRead *.py
set tabstop=4
set softtabstop=4
set shiftwidth=4
set textwidth=200
set expandtab
set autoindent
set fileformat=unix
set encoding=utf-8

syntax on

" let g:ycm_global_ycm_extra_conf='~/.vim/bundle/YouCompleteMe/third_party/ycmd/.ycm_extra_conf.py'
let g:ycm_global_ycm_extra_conf = '~/.vim/bundle/YouCompleteMe/cpp/ycm/.ycm_extra_conf.py'

let g:ycm_confirm_extra_conf = 0
" 禁用syntastic来对python检查
let g:syntastic_ignore_files=[".*\.py$"]
" " 使用ctags生成的tags文件
let g:ycm_collect_identifiers_from_tag_files = 1
" " 开启语义补全
" " 修改对C语言的补全快捷键，默认是CTRL+space，修改为ALT+;未测出效果
let g:ycm_key_invoke_completion = '<M-;>'
" " 设置转到定义处的快捷键为ALT+G，未测出效果
" nmap <M-g> :YcmCompleter GoToDefinitionElseDeclaration <C-R>=expand("<cword>")<CR><CR> 
" "关键字补全
let g:ycm_seed_identifiers_with_syntax = 1
                                                 
```



修改

Conf.py

```python
# This file is NOT licensed under the GPLv3, which is the license for the rest
# of YouCompleteMe.
#
# Here's the license text for this file:
#
# This is free and unencumbered software released into the public domain.
#
# Anyone is free to copy, modify, publish, use, compile, sell, or
# distribute this software, either in source code form or as a compiled
# binary, for any purpose, commercial or non-commercial, and by any
# means.
#
# In jurisdictions that recognize copyright laws, the author or authors
# of this software dedicate any and all copyright interest in the
# software to the public domain. We make this dedication for the benefit
# of the public at large and to the detriment of our heirs and
# successors. We intend this dedication to be an overt act of
# relinquishment in perpetuity of all present and future rights to this
# software under copyright law.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
# OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
# ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.
#
# For more information, please refer to <http://unlicense.org/>

from distutils.sysconfig import get_python_inc
import platform
import os.path as p
import subprocess
import ycm_core

DIR_OF_THIS_SCRIPT = p.abspath( p.dirname( __file__ ) )
DIR_OF_THIRD_PARTY = p.join( DIR_OF_THIS_SCRIPT, 'third_party' )
SOURCE_EXTENSIONS = [ '.cpp', '.cxx', '.cc', '.c', '.m', '.mm' ]

# These are the compilation flags that will be used in case there's no
# compilation database set (by default, one is not set).
# CHANGE THIS LIST OF FLAGS. YES, THIS IS THE DROID YOU HAVE BEEN LOOKING FOR.
flags = [
'-isystem',
'/usr/include/c++/6',
'-isystem',
'/usr/local/include',
'-isystem',
'/usr/include/c++/4.8.4',
'-isystem',
'/usr/include/c++/4.9.2',
'-isystem',
'/usr/include',
'/usr/include/x86_64-linux-gnu/c++',
'-Wall',
'-Wextra',
'-Werror',
'-Wno-long-long',
'-Wno-variadic-macros',
'-fexceptions',
'-DNDEBUG',
# You 100% do NOT need -DUSE_CLANG_COMPLETER and/or -DYCM_EXPORT in your flags;
# only the YCM source code needs it.
'-DUSE_CLANG_COMPLETER',
'-DYCM_EXPORT=',
# THIS IS IMPORTANT! Without the '-x' flag, Clang won't know which language to
# use when compiling headers. So it will guess. Badly. So C++ headers will be
# compiled as C headers. You don't want that so ALWAYS specify the '-x' flag.
# For a C project, you would set this to 'c' instead of 'c++'.
'-x',
'c++',
'-isystem',
'cpp/pybind11',
'-isystem',
'cpp/whereami',
'-isystem',
'cpp/BoostParts',
'-isystem',
get_python_inc(),
'-isystem',
'cpp/llvm/include',
'-isystem',
'cpp/llvm/tools/clang/include',
'-I',
'cpp/ycm',
'-I',
'cpp/ycm/ClangCompleter',
'-isystem',
'cpp/ycm/tests/gmock/gtest',
'-isystem',
'cpp/ycm/tests/gmock/gtest/include',
'-isystem',
'cpp/ycm/tests/gmock',
'-isystem',
'cpp/ycm/tests/gmock/include',
'-isystem',
'cpp/ycm/benchmarks/benchmark/include',
]

# Clang automatically sets the '-std=' flag to 'c++14' for MSVC 2015 or later,
# which is required for compiling the standard library, and to 'c++11' for older
# versions.
if platform.system() != 'Windows':
  flags.append( '-std=c++11' )


# Set this to the absolute path to the folder (NOT the file!) containing the
# compile_commands.json file to use that instead of 'flags'. See here for
# more details: http://clang.llvm.org/docs/JSONCompilationDatabase.html
#
# You can get CMake to generate this file for you by adding:
#   set( CMAKE_EXPORT_COMPILE_COMMANDS 1 )
# to your CMakeLists.txt file.
#
# Most projects will NOT need to set this to anything; you can just change the
# 'flags' list of compilation flags. Notice that YCM itself uses that approach.
compilation_database_folder = ''

if p.exists( compilation_database_folder ):
  database = ycm_core.CompilationDatabase( compilation_database_folder )
else:
  database = None


def IsHeaderFile( filename ):
  extension = p.splitext( filename )[ 1 ]
  return extension in [ '.h', '.hxx', '.hpp', '.hh' ]


def FindCorrespondingSourceFile( filename ):
  if IsHeaderFile( filename ):
    basename = p.splitext( filename )[ 0 ]
    for extension in SOURCE_EXTENSIONS:
      replacement_file = basename + extension
      if p.exists( replacement_file ):
        return replacement_file
  return filename


def PathToPythonUsedDuringBuild():
  try:
    filepath = p.join( DIR_OF_THIS_SCRIPT, 'PYTHON_USED_DURING_BUILDING' )
    with open( filepath ) as f:
      return f.read().strip()
  # We need to check for IOError for Python 2 and OSError for Python 3.
  except ( IOError, OSError ):
    return None


def Settings( **kwargs ):
  language = kwargs[ 'language' ]

  if language == 'cfamily':
    # If the file is a header, try to find the corresponding source file and
    # retrieve its flags from the compilation database if using one. This is
    # necessary since compilation databases don't have entries for header files.
    # In addition, use this source file as the translation unit. This makes it
    # possible to jump from a declaration in the header file to its definition
    # in the corresponding source file.
    filename = FindCorrespondingSourceFile( kwargs[ 'filename' ] )

    if not database:
      return {
        'flags': flags,
        'include_paths_relative_to_dir': DIR_OF_THIS_SCRIPT,
        'override_filename': filename
      }

    compilation_info = database.GetCompilationInfoForFile( filename )
    if not compilation_info.compiler_flags_:
      return {}

    # Bear in mind that compilation_info.compiler_flags_ does NOT return a
    # python list, but a "list-like" StringVec object.
    final_flags = list( compilation_info.compiler_flags_ )

    # NOTE: This is just for YouCompleteMe; it's highly likely that your project
    # does NOT need to remove the stdlib flag. DO NOT USE THIS IN YOUR
    # ycm_extra_conf IF YOU'RE NOT 100% SURE YOU NEED IT.
    try:
      final_flags.remove( '-stdlib=libc++' )
    except ValueError:
      pass

    return {
      'flags': final_flags,
      'include_paths_relative_to_dir': compilation_info.compiler_working_dir_,
      'override_filename': filename
    }

  if language == 'python':
    return {
      'interpreter_path': PathToPythonUsedDuringBuild()
    }

  return {}


def GetStandardLibraryIndexInSysPath( sys_path ):
  for index, path in enumerate( sys_path ):
    if p.isfile( p.join( path, 'os.py' ) ):
      return index
  raise RuntimeError( 'Could not find standard library path in Python path.' )


def PythonSysPath( **kwargs ):
  sys_path = kwargs[ 'sys_path' ]

  interpreter_path = kwargs[ 'interpreter_path' ]
  major_version = subprocess.check_output( [
    interpreter_path, '-c', 'import sys; print( sys.version_info[ 0 ] )' ]
  ).rstrip().decode( 'utf8' )

  sys_path.insert( GetStandardLibraryIndexInSysPath( sys_path ) + 1,
                   p.join( DIR_OF_THIRD_PARTY, 'python-future', 'src' ) )
  sys_path[ 0:0 ] = [ p.join( DIR_OF_THIS_SCRIPT ),
                      p.join( DIR_OF_THIRD_PARTY, 'bottle' ),
                      p.join( DIR_OF_THIRD_PARTY, 'cregex',
                              'regex_{}'.format( major_version ) ),
                      p.join( DIR_OF_THIRD_PARTY, 'frozendict' ),
                      p.join( DIR_OF_THIRD_PARTY, 'jedi_deps', 'jedi' ),
                      p.join( DIR_OF_THIRD_PARTY, 'jedi_deps', 'numpydoc' ),
                      p.join( DIR_OF_THIRD_PARTY, 'jedi_deps', 'parso' ),
                      p.join( DIR_OF_THIRD_PARTY, 'requests_deps', 'requests' ),
                      p.join( DIR_OF_THIRD_PARTY, 'requests_deps',
                                                  'urllib3',
                                                  'src' ),
                      p.join( DIR_OF_THIRD_PARTY, 'requests_deps',
                                                  'chardet' ),
                      p.join( DIR_OF_THIRD_PARTY, 'requests_deps',
                                                  'certifi' ),
                      p.join( DIR_OF_THIRD_PARTY, 'requests_deps',
                                                  'idna' ),
                      p.join( DIR_OF_THIRD_PARTY, 'waitress' ) ]

  return sys_path
```





## 操作

### 移动



- `hjkl` (强例推荐使用其移动光标，但不必需) →你也可以使用光标键 (←↓↑→). 注: `j` 就像下箭头。
- `:help ` → 显示相关命令的帮助。你也可以就输入 `:help` 而不跟命令。（陈皓注：退出帮助需要输入:q）



### 查找







查找字符串

```bash
/${words}
```

替换

```shell
 1.  替换当前行中的内容：    :s/from/to/    （s即substitude）
        :s/from/to/     ：  将当前行中的第一个from，替换成to。如果当前行含有多个
                            from，则只会替换其中的第一个。
        :s/from/to/g    ：  将当前行中的所有from都替换成to。
        :s/from/to/gc   ：  将当前行中的所有from都替换成to，但是每一次替换之前都
                            会询问请求用户确认此操作。
 
        注意：这里的from和to都可以是任何字符串，其中from还可以是正则表达式。
 
    2.  替换某一行的内容：      :33s/from/to/g
        :.s/from/to/g   ：  在当前行进行替换操作。
        :33s/from/to/g  ：  在第33行进行替换操作。
        :$s/from/to/g   ：  在最后一行进行替换操作。
 
    3.  替换某些行的内容：      :10,20s/from/to/g
        :10,20s/from/to/g   ：  对第10行到第20行的内容进行替换。
        :1,$s/from/to/g     ：  对第一行到最后一行的内容进行替换（即全部文本）。
        :1,.s/from/to/g     ：  对第一行到当前行的内容进行替换。
        :.,$s/from/to/g     ：  对当前行到最后一行的内容进行替换。
        :'a,'bs/from/to/g   ：  对标记a和b之间的行（含a和b所在的行）进行替换。
                                其中a和b是之前用m命令所做的标记。
 
    4.  替换所有行的内容：      :%s/from/to/g
        :%s/from/to/g   ：  对所有行的内容进行替换。
```





### 复制

命令模式中按v进入visual模式；

选中文本；

按y复制；

按p粘贴



跨文本复制：

- Vim new .py
- 在new.py中 :sp old.py
- Ctrl+w+w切换至old.py
- 复制文本
- 切换回new.py
- 按p复制





### 分屏

#### 分屏启动Vim

1. 使用大写的O参数来垂直分屏。

   ```
   vim -On file1 file2 ...
   ```

2. 使用小写的o参数来水平分屏。

   ```
   vim -on file1 file2 ...
   ```

**注释:** n是数字，表示分成几个屏。

注：以下的使用方式是，先同时按 ctrl和w（即ctrl+*，没有“+”），然后松手，然后再按后面的命令

#### 关闭分屏 

1. 关闭当前窗口。

   ```
   Ctrl+w c
   ```

2. 关闭当前窗口，如果只剩最后一个了，则退出Vim。

   ```
   Ctrl+w q
   ```

### 分屏

1. 上下分割当前打开的文件。

   ```
   Ctrl+w s
   ```

2. 上下分割，并打开一个新的文件。

   ```
   :sp filename
   ```

3. 左右分割当前打开的文件。 

   ```
   Ctrl+w v
   ```

4. 左右分割，并打开一个新的文件。

   ```
   :vsp filename
   ```

#### 移动光标

Vi中的光标键是h, j, k, l，要在各个屏间切换，只需要先按一下Ctrl+W

1. 把光标移到右边的屏。

   ```
   Ctrl+w l
   ```

2. 把光标移到左边的屏中。

   ```
   Ctrl+w h
   ```

3. 把光标移到上边的屏中。

   ```
   Ctrl+w k
   ```

4. 把光标移到下边的屏中。

   ```
   Ctrl+w j
   ```

5. 把光标移到下一个的屏中。

   ```
   Ctrl+w w  注：也可以按住cntl键，同时按下两次w键
   ```

#### 移动分屏

这个功能还是使用了Vim的光标键，参数是大写的。当然了，如果你的分屏很乱很复杂的话，这个功能可能会出现一些非常奇怪的症状。

1. 向右移动。

   ```
   Ctrl+w L
   ```

2. 向左移动 

   ```
   Ctrl+w H
   ```

3. 向上移动 

   ```
   Ctrl+w K
   ```

4. 向下移动 

   ```
   Ctrl+w J   
   ```

#### 屏幕尺寸

下面是改变尺寸的一些操作，主要是高度，对于宽度你可以使用Ctrl+W <或是>，但这可能需要最新的版本才支持。

1. 让所有的屏都有一样的高度。

   ```
   Ctrl+w =
   ```

2. 增加高度。

   ```
   Ctrl+w +
   ```

3. 减少高度。

   ```
   Ctrl+w -
   ```



**回到上一个缓冲区：`ctrl + o`**





### 