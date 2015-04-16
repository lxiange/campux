v0.1r49  2012/2/11

# 实现 #

定位服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.userstatus 包目录下。<br />

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2505。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
UserStatus按照Servers统一规范开发，XML消息开头可包含XML注释。

**系统用户变量列表
```
		请求：<usl></usl>
		OK响应：<ok><v>变量名1</v><v>变量名2</v></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取系统用户变量
```
		请求：<usr s="用户会话编号" u="用户名">变量名称</usr> (用户名省略时为读取自己的变量)
		OK响应：<ok b64="true">值</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**设置系统用户变量
```
		请求：<usw s="用户会话编号" n="变量名称" b64="true" u="用户名">值</usw> (用户名省略时为设置自己的变量)
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	获取MessageBox更新
```
		请求：<mg s="用户会话编号">起始ID<mg>
		OK响应（有更新）：<ok>
		    <b u="发布人" t="发布时间" d="消息id" b64="true">消息1</b>
		    <b u="发布人" t="发布时间" d="消息id" b64="true">消息2</b>
		    </ok>
		OK响应（无更新）：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**发布MessageBox消息
```
		请求：<ms s="用户会话编号"><u>发布对象用户</u><g>发布对象组</g><m b64="true">消息</m><ms>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	删除Message
```
		请求：<md s="用户会话编号">消息ID<md>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```


# 系统预定义的变量 #

约定以下变量名称及其用途：<br />
用户基本信息：
  * "`UserName`": 用户的姓名
  * "`UserSchool`": 所在的学院
  * "`UserDepartment`": 所在的系
  * "`UserGrade`": 年级
  * "`UserAge`": 年龄
  * "`UserGender`": 性别
  * "`UserPhoto`": 头像照片
  * "`UserPhotoSnippet`": 头像照片缩略图
用户动态信息：
  * "`UserLocation`": 用户位置信息
  * "`UserStatus`": 用户发布的状态
  * "`Friends`": 好友信息
用户配置信息：
  * "`AllowLocationAutoUpdate`": ("true" or "false") 允许自动更新位置信息
  * "`OnlyFollowedByFriends`": ("true" or "false") 只允许我关注的朋友获得我的信息更新

**变量的自动关联**
  * 在改变`UserLocation`和`UserStatus`后，服务器端自动发送改变消息给好友服务
  * 在改变`UserPhoto`后，服务器自动生成`UserPhotoSnippet`的缩略图。

# SDK使用 #

`com.bizdata.campux.sdk.User`类实封装了全部功能。<br />

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
User user = new User();
//在用户登录后，才能进行后续的访问
boolean success = user.login("用户名","密码");
```**

**获得用户变量列表
```
//获得已被设置的变量名称列表
String[] vars = user.getUserVariables();
```**

**读取变量
```
//读取变量名称var的值. 变量名称var为任意非空字符串
String var = "UserName"; //读取用户姓名
String val = user.getUserVariable(var)
```**

**读取其他用户变量
```
//读取其他用户的变量名称var的值. 变量名称var为任意非空字符串
String targetuser = "001221154"; //目标用户名
String var = "UserName"; //读取用户姓名
String val = user.getUserVariable(targetuser, var)
```**

**写入变量
```
//给变量赋值
// 1. 变量名称var为任意非空字符串
// 2. 值val为任意非空字符串
boolean success = user.setUserVariable(var, val);
```**

**获取MessageBox更新
```
//获得指定消息id之后的消息。消息编号从0开始。
int lastmsgid = -1; //获得所有消息
Message[] messages = user.messageGet(lastmsgid);
```**

**删除MessageBox消息
```
//删除指定id的消息
int id = 0;
boolean success = user.messageDelete(int id);
```**

