v0.1r49  2012/2/15

# 实现 #

定位服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.bubble包目录下。<br />
服务器读取用户的UserLocation变量确定用户的位置。

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2509。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
Bubble按照Servers统一规范开发，XML消息开头可包含XML注释。


**发布信息
```
		请求：<p s="用户SessionID" b64="true">信息内容</p>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取信息历史
```
		请求：<r s="用户SessionID" d="起始时间（秒）"></r> (无起始时间表示读取所有内容）
		OK响应：<ok>
			<m d="时间（秒）" b64="true" u="发布用户">信息内容</m>
			<m d="时间（秒）" b64="true" u="发布用户">信息内容</m>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```



# SDK使用 #

需要使用`com.bizdata.campux.sdk.User`和`com.bizdata.campux.sdk.Bubble`类。<br />

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
//获取Bubble类实例
Bubble bubble = new Bubble(usr);
//建议将该对象保持长生存期
```**

**发布消息
```
Boolean success = bubble.publish("消息内容");
// success==true表示发布成功
```**

**读取消息方法1
```
// 消息返回对象为BubbleMessage类数组
// 参数为返回该时间之后的消息，时间为0返回所有消息。时间的格式与{{{System.currentTimeMillis();}}}一致。
BubbleMessage[] msgs = bubble.bubbleRead(long 时间); 
// 打印得到的消息，包含用户名称、时间、和消息内容三部分
for(BubbleMessage msg : msgs){
    System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
}
```**
