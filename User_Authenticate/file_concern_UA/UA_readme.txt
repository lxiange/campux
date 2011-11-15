程序名称：User_Authenticate
程序作用：参照《用户认证与存储服务》
输入：某段来自客户端的xml
输出：根据输入产生的结果xml，并根据输入内容对数据库和物理空间中的文件进行相应修改



程序结构：
本程序包括6个包：
General_Function     为通用函数，独立于本程序
Auxiliary_Function   为辅助函数，用于连接数据库，建立动态表等
User_Authenticate    当输入xml为普通用户发送时，使用此包中代码，主要实现了《用户认证与存储服务》第二部分的功能
Store_Admin          当输入xml为admin发送时，使用此包中代码，主要实现了《用户认证与存储服务》第三部分前半的功能
Store_Use            当输入xml为app发送时，使用此包中代码，主要实现了《用户认证与存储服务》第三部分后半的功能 
Main_Function        为主程序。作为持续运行的服务器程序时，直接运行"UA_Server_MultiThread.java"即可。
                     "UA_Client.java"为客户端程序示例。端口号可以在此两个文件中修改，保持一致即可。


相关DB table：
DB中维持5个表(前4个表的约束关系参照"UA_DB.jpg")：
user_psw      表项(user_name,  user_psw,  user_status)，存储用户名、密码、状态。所有用户(admin/app/nomal user)都必须在此表中有相应项。此处不区分用户组别。
              user_status项用于密码保护动态表，取"0"、"1"，"0"表示可以访问，"1"表示拒绝访问。一般置"0"，当连续密码输入错误3次后置"1"，5min后恢复为"0"。

workgourp     表项(group_no, group_name)，存储用户组号、用户组名。组号从"1"开始，可以自己设置，也可以自动生成。有关用户组名的操作一般都以组号代替。

user_group    表项(user_group_no, user_name, group_no)，存储 (用户，组)号，用户名，组号。当某用户u加入到某组g时，即在此表中添加一项(u_g_no, u, g)，其中u_g_no可以
              自己设置，也可以自动生成。
              当group_no是"app"时，需要额外操作：在表app_store中添加(u_g_no, u, space_allocated, 0)项(含义见下面介绍表app_store处)，并在物理空间根目录下(如"D:\all_app\")
              中建立名为"u"的空文件夹，作为此app的生存空间。
              当从表中删除某项时，删除操作同上。

app_store     表项(user_group_no, app_name, space_allocated, space_used)，表示app号、app名称、预分配的空间(Byte)、已用空间(Byte)。每个app对应一个表项，同时对应
              一个物理空间根目录下(如"D:\all_app\")中名为app_name的文件夹，此app用户可以在此文件夹内进行相关操作。

wrong_set     表项(wrong_no, wrong_reason)，表示错误代码、错误原因，用于出错时输出相应数据。

(注意：假设workgourp中有(1，app)项，则user_group表中的项(8, zhao, 1)、app_store表中的项(8,zhao,100000, 0)、文件夹"D:\all_app\zhao"是同时生成、同时删除的。)



程序运行后，内存中维持两个动态表：
ran_con      表项(ran_con_num, user_name, active_time)，表示已通过会话认证的用户，其active_time会随着计时器递减，60min后减为0，自动从表中删除此项，用户需要重新登录。
psw_pro      表项(user_name, wrong_psw_time, active_time)，表示连续密码输入错误的用户，wrong_psw_time表示连续输入次数，当其为"3"时，置user_psw中user_name相关项的user_staus为"1"。
             其active_time会随着计时器递减，5min后减为0，自动从表中删除此项，并置user_psw中user_name相关项的user_staus为"0"，用户可继续尝试登录。