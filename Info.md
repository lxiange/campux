v0.1r49  2012/2/20

# 实现 #

咨询服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.info包目录下。<br />

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2506。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
`Info`按照Servers统一规范开发，XML消息开头可包含XML注释。


**发布信息
```
		请求：<si s="用户SessionID" b64="true" r="room name">信息内容</si>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取信息
```
		请求：<gs s="用户SessionID" d="起始时间（秒）"></gs> (无起始时间表示读取所有内容）
		OK响应：<ok>
			<s d="时间" b64="true" u="发布用户名">信息内容</s>
			<s d="时间" b64="true" u="发布用户名">信息内容</s>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**注册PUBLISHER：PUBLISHER为system组的用户，无需注册**	读取PUBLISHER的icon：直接读取发布用户的photo


# SDK使用 #


需要使用`com.bizdata.campux.sdk.User`和`com.bizdata.campux.sdk.Info`类。<br />

**类初始化
```
//包名称
import com.bizdata.campux.sdk.*;
//首先初始化配置，提供配置文件输入流
if( Config.needInit() ){
    InputStream input=new FileInputStream("sdk.config");
    Config.init(input);
}
//获取User类实例。
User user = new User();
//在用户登录后，才能进行后续的访问
boolean success = user.login("用户名","密码");
//获取Info类实例
Info f = new Info(user);
```**

**获得用户的咨询更新
```
// 获得更新的消息
InfoMessage[] msgs = f.infoUpdate(); 
//或者使用 f.infoRead(long starttime)，其中starttime参数为起始时间 

// 打印消息
for(InfoMessage msg:msgs){
    // 打印  消息发布人的用户名  消息发布的时间  消息内容
    System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
}
```**

！！！！！！！！！！！！！！！！！！ <br />
约定：消息内容的第一行为URL，第二行为<br />
主题，第三行为内容的前40个字。<br />
！！！！！！！！！！！！！！！！！！<br />

！！！！！！！！！！！！！！！！！！<br />
用户关注的咨询在用户注册时服务器端<br />
加入，暂时不需要再客户端做工作<br />
！！！！！！！！！！！！！！！！！！<br />

**发布咨询
```
boolean suc = f.__infoPublish(咨询分类, 消息内容(主题+"\n"+内容));
// suc为true表示发布成功。
```**

