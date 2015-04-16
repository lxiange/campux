v0.1r46 2011/12/23

# 实现 #

定位服务位于子项目Servers的统一服务器框架下的com.bizdata.campux.server.wifilocator 包目录下。<br />

# 数据 #

初始收集的地理数据较少，需要人工增加数据，并在用户使用过程中，记录用户输入的地点来扩充和更新地理数据。

# 协议 #

所有的通讯都要使用SSL Socket进行TCP连接，以保障通讯安全。<br />
服务的TCP port为2503。<br />
通讯语言使用XML，XML的处理使用org.xml.sax或DOM4J开源项目以保证效率，JAVA自带的DOM效率较低。<br />
Locator按照Servers统一规范开发，XML消息开头可包含XML注释。

**通过wifi信息取得地址
```
		请求：<g s="用户SessionID">
			<i>IP地址</i>
			<w s="型号强度" c="连接的wifi为true">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			</g>
		OK响应：<ok>地理位置名称</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	添加地址数据
```
		请求：<a s="用户SessionID" l="位置名称">
			<i>IP地址</i>
			<w s="型号强度" c="连接的wifi为true">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			</a>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```

# SDK使用 #

`com.bizdata.campux.sdk.Locator`类实封装了定位功能。同时需要`com.bizdata.campux.sdk.User`类的支持。（目前暂时无需用户登录）<br />
需要通过ip地址、扫描周围的wifi信息来获得定位，android中如何获得当前IP、wifi的BSSID和信号强度请参见WifiCollector子项目。

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
User user = new User(); //需要User类支持
Locator locator = new Locator();
```**

**通过wifi状态取得地理位置
```
// 可以添加多个扫描到的wifi信息
locator.addWifi("00:00:00:00:00:00"/*扫描到的一个wifi的BSSID*/, -90/*强度*/, false/*是否已连接上该wifi*/);
locator.addWifi("00:00:00:00:00:00"/*扫描到的一个wifi的BSSID*/, -90/*强度*/, false/*是否已连接上该wifi*/);
// 可以提供目前获得的ip地址
locator.setIP("ip地址");
// 获得地址名称
try{
    String locationname = locator.getLocation(user); //目前user对象无需做任何事
}catch(Exception exc){
    // 暂时无法取得定位
}
```**
