# 实体类简介

[TOC]

简易聊天系统`ChatApp`

## 登录凭证

`LoginBo`

用户登录凭证

- 用户登录时需要将帐号和密码发送至服务器
- 服务器验证成功后，返回一个登录凭证给用户

## 用户

用户实体类
`User`

- 属性
	- `uid`用户帐号
	- `name`用户名
	- `pwd`密码


## 群组

`Group`

聊天室抽象为群组
群组本身不记录用户状态
两人单独聊天的群组：用户列表只有两人
多人聊天的群组，用户列表包含多人

- 属性
	- `gid`群组uid
	- `owdnerId`群主uid
	- `groupName`群组名
	- `establishTime`建群时间

### 群组成员

`GroupMember`

该实体类只是为了从数据库中取出数据而存在

## 客户端和服务器端的交互对象

### 消息类型

`MessageType`

- 属性
	- `info`类型简介
	- `type`类型的`char`表示

```
- STATUS_TYPE：用户状态信息：服务器端发送登录信息给客户端，或客户端注销时向服务器端发送，子类为StatusMessage
- INFO_TYPE：用户查询信息：客户端查询信息，服务器端返回查询结果，子类为InfoMessage
- UPDATE_TYPE：用户信息更新：客户端第一次登录时，登录后修改用户信息，子类为UpdateMessage
- CHAT_TYPE：用户聊天：客户端发送给服务器端，或者服务器端发送给客户端[新的聊天记录]，子类为ChatMessage
```

### 消息

`Message`

属性包括

- 用户登录凭证`LoginBo`
- 信息类型`MessageType`
- 消息发送时间`sendTime`

根据类型不同转换为以下子类

### 聊天

`ChatMessage`

在客户端和服务器端之间传递的聊天信息
客户端发送给服务器
服务器再发送给其他的客户端

### 状态

`StatusMessage`

- 用户登录时，第一次发送UpdateMessage，服务器端返回新的LoginBo的StatusMessage
- 用户注销时，发送StatusMessage，服务器端关闭用户对应serverThread的进程，并发送注销通知
- 用户端接收到服务器端发送的注销通知(LoginBo中[uid=0000]的StatusMessage)时，关闭全部线程

### 查询信息

`InfoMessage`

- 查询过程
1. 客户端发送给服务器需要查询的对象
2. 服务器返回客户端查询的结果信息
3. 客户端接收到查询信息只需要打印即可

- 属性包括
- 客户端向服务器查询的信息类型`u:用户信息，g:群组信息，c:聊天信息`
- 查询的具体参数
	- 若infoType是u，specType内存放uid(可多个)，代表查询用户信息
	- 若infoType是g，specTYpe内存放gid(可多个)，代表查询群组信息
	- 若infoType是c，specType内存放群组gid(唯一)，代表查询聊天信息
- 服务器端向客户端响应的信息列表（用户信息 或 群组信息 或 聊天信息）

### 更新

`updateMessage`

客户端登录时发送一个只包含帐号和密码的用户信息
登录后，客户端发送给服务器端需要更新的用户信息

## 关注

`Attention`

朋友实体类转换为关注

不同的uid之间可以相互关注

## 返回信息

`ReturnInfo`

属性包括`状态码`和`简要信息（String）`

### 状态码

`StatusCode`

enum类

```
- SUCCESS_CODE：成功运行结束状态
- ERROR_CODE：运行过程中异常终止
- NOT_FOUND_CODE：没有找到对应信息
- NO_PERMISSION_CODE：权限不足

```

## 服务器端

主进程为ServerSocket

### 服务器端对单个客户端的处理线程

`ServerCallable`

服务器端每接收到一个socket连接，就会开启一个对应的处理线程

### 处理线程工厂类

`ServerThreadFactory`

## 客户端

### 用户命令

enum：用于对用户输入的命令进行限定
`ClientCommand`

- 属性
	- `command`命令的字符串形式
	- `info`命令的简单介绍

```
- SEND_CHAT_MSG：发送聊天信息
- ALTER_USER_INFO：修改用户信息
- QUERY_INFO：查询用户，群组，聊天信息
- SIGN_OUT：客户端注销命令

```

### 用户输入处理器

`ClientInputHandler`

1. 将用户输入字符串通过正则表达式匹配分割为命令序列
2. 通过匹配命令将输入的信息封装为Message
3. 返回封装的Message

### 输入线程

`InputCallable`

获取服务器端传送的Message交互对象，交给MsgService处理

### 输出线程

`OutputCallabel`

- 获取客户端输入的命令并通过`用户输入处理器`封装为Message
- 将Message传送给服务器端

## 工具类包

`utils/`

### 随机数生成工具类

`RandomUtils`

### 存储工具类

`storageUtils`

- 主要为第一个版本的txt文件提供存取服务
- 目前数据采用数据库形式存储
- 因此该工具类已弃用，后续版本可能会重新开放

### mybatisUtils

使用mybatis的通用静态方法，将其与业务代码分离，实现代码复用

# 程序运行过程介绍

