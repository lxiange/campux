v0.1r49  2012/2/12

中间层服务

# 一、服务范例 #
> 服务均通过TCP协议进行通讯，以便能够灵活部署到网络节点中。
> 服务端口范围设定在2500~2599之间。
> 通讯语言使用XML。XML的处理使用org.xml.sax和DOM4J开源项目以保证效率。JAVA自带DOM效率较低。
> 当出现属性b64时，无论b64属性设置为何，表示值使用base64编码

# 二、用户配置服务 2505 #

**系统用户变量列表
```
		请求：<usl></usl>
		OK响应：<ok><v>变量名1</v><v>变量名2</v></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取系统用户变量
```
		请求：<usr s="用户会话编号">变量名称</usr>
		OK响应：<ok b64="true">值</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**设置系统用户变量
```
		请求：<usw s="用户会话编号" n="变量名称" b64="true">值</usw> 
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

# 三、用户信息服务UserInfo 2506 #

**读取PUBLISHER列表
```
		请求：<lp></lp>
		OK响应：<ok>
		<a n="PUBLISHER ID">
			<ip b64="true">base64编码的icon</ip>
			<dp>PUBLISHER显示名称</dp>
			<cp>PUBLISHER信息类别1</cp>
			<cp>PUBLISHER信息类别2</cp> ...
		</a>
		<a>…</a>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	注册PUBLISHER（仅admin和system组用户可以注册）
```
		请求：<rp s="用户SessionID">
				<ip b64="true">base64编码的icon</ip>
				<dp>PUBLISHER显示名称</dp>
				<cp>PUBLISHER信息类别1</cp>
				<cp>PUBLISHER信息类别2</cp> ...
			</rp>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**账户初始化
```
		请求：<ui s="用户会话编号"></ui>   
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	检查更新
```
		请求：<ci s="用户会话编号"></ci>  (无会话编号视为guest用户)
		OK响应：<ok>最新信息ID</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**获取信息
```
		请求：<gs s="用户会话编号">起始信息ID</gs>  (无会话编号视为guest)
		OK响应：<ok>
			<i xp="publisher名称" b64="true" xc="类别1;类别2" xd="信息ID">预览信息base64编码</i> （预览信息最多三行）
			<i>…</i>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	获取详细信息
```
		请求：<gi s="用户会话编号">信息ID</gi>
		OK响应：<ok>
			  <t b64="true">题目</t>
			  <d>信息日期</d>
			  <l>链接</l>
			  <co b64="true">内容</co>
			  <e>(包含事件)
			  	<et b64="true">事件名称</et>
			  	<ed>事件日期和时间</ed>
			  	<ea b64="true">事件地点</ea>
			  </e>
			  <e>...</e> （可包含多个事件）
		  </ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**发布信息
```
		请求：<si s="用户会话编号">（每次写入一条）
		  <t b64="true">题目</t>
		  <d>信息日期</d>
		  <l>链接</l>
		  <co b64="true">内容</co>
		  <p>publisher名称</p>
		  <c>publisher信息类别</c>
		  <es>包含事件的数量</es>(无事件则为空或0)
		  <e>(包含事件)
		    <et b64="true">事件名称</et>
		    <ed>事件日期</ed>
		    <ea b64="true">事件地点</ea>
		  </e>
		  ...
		  <e></e>
		  <u>发布对象用户</u>		  
		  ...
		  <u>发布对象用户</u>
		  <g>发布对象组</g>
		  ...
		  <g>发布对象组</g>
		</si>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	删除信息
```
		请求：<di s="用户会话编号">信息ID</di>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
# 三、闹钟服务 2507  ( 到时发送消息给用户的MessageBox ) #

**设置提醒(单次提醒)
```
		请求：<sas s="APP会话编号">
		  <s u="用户" t="提醒日期和时间">提醒消息</s> 
		<sas>
		OK响应：<ok><d>提醒ID</d></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	设置提醒(多次提醒)
```
		请求：<sap s="APP会话编号">
		  <p u="用户" t="提醒时间" s="起始日期和时间" i="时间间隔">提醒消息</p>
		<sap>
		OK响应：<ok><d>提醒ID</d></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**枚举提醒
```
		请求：<al s="APP会话编号"></al>
		OK响应：<ok>
			<s d="提醒ID" u="用户" t="提醒时间">提醒消息</s>
			<p d="提醒ID" u="用户" t="提醒时间" s="起始日期和时间" i="时间间隔">提醒消息</p>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	取消请求
```
		请求：<sa s="APP会话编号">提醒ID<sa>
		OK响应：<ok><d>提醒ID</d></ok>
		失败响应：<err c="错误代码">错误原因</err>
```

# 四、朋友信息服务 2508 #

**读取好友列表
```
		请求：<lf s="用户SessionID"></lf>
		OK响应：<ok>
			<u>用户名</u><u>...</u>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	添加好友
```
		请求：<af s="用户SessionID">好友id</af>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**移除好友
```
		请求：<rf s="用户SessionID">好友id</rf>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	发布信息更改
```
		请求：<ps s="用户SessionID" b64="true">信息内容</ps>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**读取信息历史
```
		请求：<rs s="用户SessionID" d="起始时间">用户名</rs> (无起始时间表示读取所有内容）
		OK响应：<ok>
			<s d="时间" b64="true">信息内容</s>
			<s d="时间" b64="true">信息内容</s>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**

# 五、冒泡服务 2509 #

**发布信息
```
		请求：<p s="用户SessionID" b64="true" l="地理位置">信息内容</p>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	读取信息历史
```
		请求：<r s="用户SessionID" d="起始时间">地理位置"</r> (无起始时间表示读取所有内容）
		OK响应：<ok>
			<m d="时间" b64="true" u="发布用户">信息内容</m>
			<m d="时间" b64="true" u="发布用户">信息内容</m>
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```

# 六、课表与自习教室服务 2510 #

**获得教室列表
```
		请求：<lc></lc>
		OK响应：<ok>
			<c b="分类">地理位置</c>
			<c b="分类">地理位置</c>
		</ok>
```**	发布信息
```
		请求：<pr s="用户SessionID" l="地理位置">1/0</pr> （1为教室有空，0为教室无空）
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**读取信息历史
```
		请求：<rr s="用户SessionID" l="地理位置"</rr> 
		OK响应：<ok>0~100</ok> （值表示教室空闲程度）
		失败响应：<err c="错误代码">错误原因</err>
```**	读取课表
```
		请求：<rs s="用户SessionID"></rs> 
		OK响应：<ok>
			<c d="星期" t="节" r="教室">课表内容</c>
			<c d="星期" t="节" r="教室">课表内容</c>
			...
		</ok> 
		失败响应：<err c="错误代码">错误原因</err>
```
**设置课表
```
		请求：<ws s="用户SessionID" d="星期" t="节" r="教室">课表内容</ws> 
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**

# 八、音乐服务 2512 #

**获得专辑列表
```
		请求：<la s="用户SessionID"></la>
		OK响应：<ok>
			<a><n>名称</n><i b64="true">封面</i></a>
			<a><n>名称</n><i b64="true">封面</i></a>
			...
		</ok>
```**	获得专辑内容
```
		请求：<ra s="用户SessionID">名称</ra>
		OK响应：<ok>
			<f b64="true">封面</f>
			<i b64="true">介绍</i>
			<s b4="true" d="歌曲id">歌名</s>
			<s b4="true" d="歌曲id">歌名</s>
			...
		</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**读取歌曲
```
		请求：<rs s="用户SessionID">歌曲id</rs> 
		OK响应：<ok b64="true">音乐文件</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**

# 九、Q&A服务 2513 （暂时不做） #

# 十、wifi定位服务 2503 #

**取得地址
```
		请求：<g s="用户SessionID">
			<i>IP地址</i>
			<w s="型号强度" c="连接的wifi为true">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			<w s="型号强度">wifi的BSSID</w>
			</g>
		OK响应：<ok>地理位置名称</ok>
		失败响应：<err c="错误代码">错误原因</err>
```**	添加地址
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

# 十一、文档分析服务 2504 #

**注册/覆盖/添加分析任务
```
		请求：<r s="用户SessionID" n="任务名称" append="true" b64="true">训练数据</r> （训练数据格式见后）
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```**  分析
```
		请求：<b s="用户SessionID" n="任务名称"></b>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
```
**预测
```
		请求：<p s="用户SessionID" n="任务名称" b64="true">预测数据</p>
		OK响应：<ok b64="true">预测结果</ok>
		失败响应：<err c="错误代码">错误原因</err>
```
【训练数据的表达】**<br>
N //定义类别为0,1,...,N<br>
N //定义特征数量（不包括doc和class）为N<br>
class,att1,att2,...,attN,doc //一行为一条数据，class为类别，att1, att2为特征的值，doc为base64编码的文本<br>
class,att1,att2,...,attN,doc<br>
...<br>
【测试数据的表达】<br>
-1,att1,att2,doc //att1, att2为特征的值，doc为base64编码的文本<br>