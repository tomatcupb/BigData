#shell commands
1. Hadoop
```shell
# 本地上传文件
hadoop fs -put 本地路径 hdfs路径
# hdfs文件下载
hadoop fs -get hdfs路径 本地路径


```

1. shell
```shell
# 可读方式显示文件、文件夹大小
du -h 路径

# gzip压缩文件
gzip file路径

# awk命令
awk

# 重定向
>		输出重定向到一个文件或设备 覆盖原来的文件
>!		输出重定向到一个文件或设备 强制覆盖原来的文件
>>      输出重定向到一个文件或设备 追加原来的文件
<       输入重定向到一个程序 

2>      将一个标准错误输出重定向到一个文件或设备 覆盖原来的文件  b-shell
2>>     将一个标准错误输出重定向到一个文件或设备 追加到原来的文件
2>&1    将一个标准错误输出重定向到标准输出
>&      将一个标准错误输出重定向到一个文件或设备 覆盖原来的文件  c-shell
|&      将一个标准错误 管道 输送 到另一个命令作为输入
>/dev/null	重定向到空，即不保存

ls 2>1 					测试一下，不会报没有2文件的错误，但会输出一个空的文件1；
ls xxx 2>1				测试，没有xxx这个文件的错误输出到了文件1中；
ls xxx 2>&1				测试，不会生成1这个文件了，不过错误跑到标准输出了；
ls xxx >out.txt 2>&1 	实际上可换成 ls xxx 1>out.txt 2>&1；重定向符号>默认是1,错误和输出都传到out.txt了。


# 查看端口占用
lsof -i:PORT
```