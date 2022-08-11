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

![2](D:\blog\github\DefendMemoryShell\images\2.png)

然后执行我们的工具进行清除

![3](D:\blog\github\DefendMemoryShell\images\3.png)

![4](D:\blog\github\DefendMemoryShell\images\4.png)

再次访问页面，无法再执行命令了，清除成功

![5](D:\blog\github\DefendMemoryShell\images\5.png)

## 目前尚未解决的Bug

### tools.jar路径问题（尚未解决）

Java自带jar包tools.jar路径问题，问题路径`LoadAgent#loadAgent2JVM`

![1](D:\blog\github\DefendMemoryShell\images\1.png)

尽管可以动态获取系统变量中tools.jar路径，虽然路径和图中红框一致，但是会无法获取java自带的`VirtualMachine`类，此bug尚待解决。

