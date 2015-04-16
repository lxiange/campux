v0.1r49  2012/2/15

# 实现 #

好友服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.friends包目录下。<br />
使用用户的Friends变量存储用户的好友。<br />
用户对`UserStatus`、`UserLocation`变量的更改将自动发布好友更新信息。

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2508。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
Friends按照Servers统一规范开发，XML消息开头可包含XML注释。


**发布信息
```
		请求：<ps s="System用户SessionID" u="发布用户名" b64="true">信息内容</ps>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取信息历史
```
		请求：<rs s="用户SessionID" d="起始时间（秒）"></rs> (无起始时间表示读取所有内容）
		OK响应：<ok>
			<s d="时间" b64="true" u="发布用户名">信息内容</s>
			<s d="时间" b64="true" u="发布用户名">信息内容</s>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```



# SDK使用 #

注意：<br>
1. 得到的好友更新可能不是按照时间顺序排列的，客户端需要再根据时间排序。<br>
2. 在获取的好友更新中，默认会包含自己的更新。<br>
<br>
需要使用<code>com.bizdata.campux.sdk.User</code>和<code>com.bizdata.campux.sdk.Friends</code>类。<br />

<b>用户对<code>UserStatus</code>、<code>UserLocation</code>变量的更改将自动发布好友更新信息。</b>

<b>类初始化<br>
<pre><code>//包名称<br>
import com.bizdata.campux.sdk.*;<br>
//首先初始化配置，提供配置文件输入流<br>
if( Config.needInit() ){<br>
    InputStream input=new FileInputStream("sdk.config");<br>
    Config.init(input);<br>
}<br>
//获取User类实例。<br>
User user = new User();<br>
//在用户登录后，才能进行后续的访问<br>
boolean success = user.login("用户名","密码");<br>
//获取Friend类实例<br>
Friend finstance = new Friend(usr);<br>
//建议将该对象保持长生存期<br>
</code></pre></b>

<b>发布消息<br>
<pre><code>Boolean success = bubble.publish("消息内容");<br>
// success==true表示发布成功<br>
</code></pre></b>

<b>读取消息方法1<br>
<pre><code>// 消息返回对象为FriendMessage类数组<br>
// 参数为返回该时间之后的消息，时间为0返回所有消息。时间的格式与{{{System.currentTimeMillis();}}}一致。<br>
FriendMessage[] msgs = finstance.friendStatusRead(long 时间); <br>
// 打印得到的消息，包含用户名称、时间、类型和消息内容四个部分<br>
for(FriendMessage msg : msgs){<br>
    System.out.println(msg.publisher + " " <br>
        + msg.time + " " + msg.type==0?"状态更新":"位置更新" + " " <br>
        + msg.message);<br>
}<br>
</code></pre></b>

<b>读取消息方法2（推荐方法）<br>
<pre><code>// 消息返回对象为FriendMessage类数组。该方法返回自上一次读取消息后的新消息。<br>
FriendMessage[] msgs = finstance.friendStatusUpdate(); <br>
// 打印得到的消息，包含用户名称、时间、类型和消息内容四个部分<br>
for(FriendMessage msg : msgs){<br>
    System.out.println(msg.publisher + " " <br>
        + msg.time + " " + msg.type==0?"状态更新":"位置更新" + " " <br>
        + msg.message);<br>
}<br>
</code></pre></b>

<b>获得当前用户的好友列表<br>
<pre><code>// 得到好友的学号<br>
List&lt;String&gt; myfriends = finstance.friendRead();<br>
</code></pre></b>

<b>添加好友<br>
<pre><code>// 按学号添加用户<br>
boolean success = friendAdd(String friendname);<br>
// success为true表示添加成功<br>
</code></pre></b>

