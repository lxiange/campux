v0.1r46  2011/12/23

# 实现 #
用户认证服务位于`User_Authenticate`子项目中，启动类为`UA_Server_MultiThread.MainFunction`。由SQLServer做后台。

[TODO：在下一个版本中用户认证将与其他服务统一在Servers子项目中，使用统一构架，规范包和函数命名，取消SQLServer的使用。]

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2501。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
XML消息开头必须不包含XML注释，必须以"\n"结束。<br />
[TODO：在下一个版本中将取消不能包含XML注释和必须换行结束的要求，与其他Servers子项目中的服务使用统一规范。]

用户认证服务提供用户的登录和管理请求：<br />
**用户认证
```
		请求：<v><u>username</u><p>password</p></v>
		OK相应：<ok>随机会话编号</ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户注销
```
		请求：<o><s>会话编号</s></o>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户会话认证
```
		请求：<c><s>会话编号</s></c>
		OK相应：<ok>用户名</ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户添加
```
		请求：<a><u>username</u><p>password</p></a>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户删除
```
		请求：<d><si>会话编号</si><u>username</u></d>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户密码修改
```
		请求：<m><si>会话编号</si><p>new password</p></m>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户组添加
```
		请求：<ga><si>会话编号</si><g>group name</g></ga>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户组删除
```
		请求：<gd><si>会话编号</si><g>group name</g></gd>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户组枚举
```
		请求：<gl><si>会话编号</si></gl>
		OK相应：<ok><g>group1</g><g>group2</g>…</ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户组中的用户枚举
```
		请求：<gul><si>会话编号</si><g>group name</g></gul>
		OK相应：<ok><u>user1</u><u>user2</u>…</ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户关联到用户组(此处不能关联到app组)
```
		请求：<gua><si>会话编号</si><u>user</u><g>group </g></gua>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```** 用户取消用户组关联(此处不能与app组取消关联)
```
		请求：<gur><si>会话编号</si><u>user</u><g>group name</g></gur>
		OK相应：<ok></ok>
		失败相应：<err c="错误代码">错误原因</err>
```
**用户的组属性枚举
```
		请求：<ug><si>会话编号</si><u>user</u></ug>
		OK相应：<ok><g>group1</g><g>group2</g>…</ok>
		失败相应：<err c="错误代码">错误原因</err>
```**


# 默认值 #
**内置管理员组：admin（管理员组），app（应用组），user（用户组）**<br />
**内置管理员账户：admin，属于admin组。**<br />
**密码枚举保护：连续密码错误三次则暂停对该用户的认证5分钟。**<br />
**用户会话失效：会话建立60分钟后失效，会话失效后需要重新认证。**<br />


# SDK使用 #

用户认证功能全部实现于com.bizdata.campux.sdk.User类中。<br />

**类初始化
```
//包名称
import com.bizdata.campux.sdk.*;
//首先初始化配置，提供配置文件输入流
if( Config.needInit() ){
    InputStream input=new FileInputStream("sdk.config");
    Config.init(input);
}
//获取类实例。
User instance = new User();
```**

**用户注册
```
boolean success = instance.register("学号", "密码", "姓名", "系", "院", "年级", "年龄", "性别(M/F)");
```
其中前三个参数为必填参数，其他参数可以为null。**<br />
注册成功后该User类自动进入登录状态。

**用户登录
```
boolean success = instance.login("用户名","密码");
```
登录后要维持该对象的生命周期，以通过该用户身份进行访问。**

**注销登录
```
boolean success = instance.logout();
```**

**获得用户会话的session ID
```
String id = instance.getSessionID();
```
该id用于证实用户的登录。**

**通过session ID认证用户
```
String user = instance.lookupUsername(String sessionID);
```
查出用户名称意味着该用户为真实的登录用户。**

