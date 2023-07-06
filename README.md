# latmn inc. kangaroo

### 描述
基于 `SpringCloud` `AlibabaCloud` `jdk17` `redis` `mysql` 

采用 Spring Gateway Uri鉴权

业余时间 正在不断完善中

### framework 
#### convention
    web转换定义
#### core
    core
#### test
    测试framework
### platform
#### api
    统一api编写
#### user 
    用户中心
#### mq  
    消息服务
#### news 
    新闻中心


<hr />

### swagger 
路径: `/doc.html`

例如: http://127.0.0.1:8088/user/doc.html

### 运行

#### feign example
```
当前 user 调用 news
```

#### start nacos
```sh
startup.cmd -m standalone
http://127.0.0.1:8848/nacos/#/login
nacos
nacos
```
#### 启动数据库
导入
`gateway.sql`
`user.sql`

#### 启动redis
    127.0.0.1 6379
#### 启动 sentinel
下载:
```shell
https://github.com/alibaba/Sentinel/releases/download/2.0.0-alpha/sentinel-dashboard-2.0.0-alpha-preview.jar
```
启动:
```
java -Dserver.port=9999 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-2.0.0-alpha-preview.jar
```

#### 启动程序
```shell
sentinel  9999    
gateway   80      9993(sentinel transport)
user      8088    9992(sentinel transport)
news      8089    9991(sentinel transport)
```
`gateway负责路径鉴权`