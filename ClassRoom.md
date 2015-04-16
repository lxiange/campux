v0.1r49  2012/2/20

# 实现 #

自习教室服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.classroom包目录下。<br />

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2510。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
`ClassRoom`按照Servers统一规范开发，XML消息开头可包含XML注释。


**获得教室列表 （地理位置=建筑**教室名称）
```
		请求：<lc></lc>
		OK响应：<ok>
			<c b64="true" m="推荐值">地理位置</c> 
			<c b64="true" m="推荐值">地理位置</c>
		</ok>
```**	发布教室评价
```
		请求：<pr s="用户SessionID" c="0/1" b64="true">地理位置</pr> （1为教室有空，0为教室无空）
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
_读取教室评价
```
		请求：<rr s="用户SessionID"  b64="true">地理位置</rr> 
		OK响应：<ok>0~100</ok> （值表示教室空闲程度）
		失败响应：<err c="错误代码">错误原因</err>
```_**


# SDK使用 #



需要使用`com.bizdata.campux.sdk.User`和`com.bizdata.campux.sdk.ClassRoom`类。<br />

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
//获取ClassRoom类实例
ClassRoom f = new ClassRoom(user);
```**

**获得教室列表
```
// 取得教室列表，结果为HashMap对象
HashMap<String, List<String>> roomlist = f.listClassRooms();
// 打印教室列表
// 对每一个建筑名称
for(String building : roomlist.keySet()){
    System.out.println(building+":"); // 打印建筑名称
    // 获得建筑中的教室名称
    List<String> rooms = roomlist.get(key);
    // 打印建筑中的教室列表
    if( rooms!=null ){
        for(String room : rooms)
            System.out.println("    "+room);
    }
}
```**

**发布教室评价
```
// 第一个参数为建筑名称，第二个参数为教室编号，第三个参数为是否好评
boolean suc = f.publishClassRoomComment("逸夫楼", "I120", true); 
// suc为true表示发布成功。

// 或者使用第二种发布方法：
// 第一个参数为“建筑名称_教室编号”，第二个参数为是否好评
boolean suc = f.publishClassRoomComment("逸夫楼_I120", true); 
// suc为true表示发布成功。
```**

