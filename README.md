# latmn inc. kangaroo

### 表描述
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

#### 数据库 导入
`gateway.sql`
`user.sql`

#### start nacos
```sh
startup.cmd -m standalone
http://127.0.0.1:8848/nacos/#/login
nacos
nacos
```

#### feign example
```
当前 user 调用 news
```