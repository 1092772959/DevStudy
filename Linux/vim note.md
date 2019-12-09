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



查找

```bash
/{words}
```

