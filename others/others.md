# 补充
1. log4j的运用
    - 
```
//info：日志级别，console：是否控制台打印，HFILE：是否保存日志到文件HFILE（文件名可自定义）
log4j.rootLogger=info,console,HFILE

#文件大小到达指定尺寸的时候产生一个新的文件
log4j.appender.HFILE=org.apache.log4j.RollingFileAppender
log4j.appender.HFILE.File=./src/main/java/com/cheng/log/test.log//生成日志的位置
log4j.appender.HFILE.MaxFileSize=30mb
log4j.appender.HFILE.MaxBackupIndex=20
log4j.appender.HFILE.layout=org.apache.log4j.PatternLayout
log4j.appender.HFILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %p %l %t %r  %c: %m%n


log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.target=System.out
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c %M(): %m%n
```
    - 
```
static Logger logger = Logger.getLogger("WordCountDriver.class");
logger.info("delete the outdir!");
```
2. IDEA 打jar包
    - mvn package: 不在 POM 中配置任何插件，直接使用 mvn package 进行项目打包，这对于没有使用外部依赖包的项目是可行的。