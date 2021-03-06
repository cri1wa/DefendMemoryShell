# DefendMemoryShell

## 介绍

这是一款内存马清除工具，支持以下功能

- 白名单功能
- 黑名单功能
- 发现后自动清除内存马

## 如何使用

maven打包成jar后，命令行直接启动

```shell
java -jar .\DefendMemoryShell-1.0-SNAPSHOT.jar com.crilwa.SpringdemoApplication D:\\blog\\github\\DefendMemoryShell\\target\\DefendMemoryShell-1.0-SNAPSHOT.jar
```

- args[0]，目标项目，springboot之类的
- args[1]，打包好的jar路径

### 自定义配置

#### 黑名单

此处黑名单还有待添加......

| 危险类路径                                      | 注释       |
| ----------------------------------------------- | ---------- |
| org.apache.catalina.core.ApplicationFilterChain | 常见注入类 |
| javax.servlet.http.HttpServlet                  | 冰蝎马     |

#### 白名单

| 保护类路径   | 注释         |
| ------------ | ------------ |
| 根据需求设定 | 根据需求设定 |
|              |              |

## 工具演示

先注入常见的javaAgent内存马

![2](images/2.png)

然后执行我们的工具进行清除

![3](images/3.png)

![4](images/4.png)

再次访问页面，无法再执行命令了，清除成功

![5](images/5.png)

## 目前尚未解决的Bug

### tools.jar路径问题（尚未解决）

Java自带jar包tools.jar路径问题，问题路径`LoadAgent#loadAgent2JVM`

![1](images/1.png)

尽管可以动态获取系统变量中tools.jar路径，虽然路径和图中红框一致，但是会无法获取java自带的`VirtualMachine`类，此bug尚待解决。

本人是java安全入门学习者，如果有更好的查杀思路，或者是修复工具的BUG，欢迎找俺讨论。

## 免责声明
此工具仅仅作为学习交流使用，切勿使用在非法途径上。若使用此工具造成任何损失，与本人无关。
## References
[Agent内存马的自动分析与查杀 - 先知社区 (aliyun.com)](https://xz.aliyun.com/t/10910)

[JAVA内存马的“一生” - 先知社区 (aliyun.com)](https://xz.aliyun.com/t/11003)


