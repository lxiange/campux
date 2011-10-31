 
程序名：Lily_Update

程序作用：更新南京大学小百合论坛各subforum的新内容，将新的theme_page的内容格式化后存入数据库，并同时更新表'lily_date'

程序结构：根据功能分成8个包
    1. General_Function    一些常用的辅助函数，独立于本程序
    2. Get_All_Subforum_Name    获取所有子论坛名
    3. Get_Url    获取frame_page和theme_page的url
    4. Store_New_Theme    存储theme_page的内容
    5. Main_Function    main函数
    6. Initial_Table   初始化表'lily_date'
    7. Get_Old_Newest_Theme   获取表'lily_date'中某subforum对应的最新theme_page的信息
    8. Set_New_Newest_Theme   更新'lily_date'中某subforum对应的最新theme_page的信息

处理流程：one subforum url -> get url and date in 'lily_date' corresponding to this subforum-> 
          all new theme_page url of this subforum-> all new theme_page content of this subforum -> 
          store into DB->  refresh url and date in 'lily_date' corresponding to this subforum 

百合论坛结构(4层)：
    classefied_subforum(，或discussion,分类讨论区)，如"http://bbs.nju.edu.cn/bbsboa?sec=5"
    subforum(子论坛)，如"http://bbs.nju.edu.cn/bbsdoc?board=Nirvana"
    frame_page(目录页)，如"http://bbs.nju.edu.cn/bbsdoc?board=D_Computer&start=61&type=doc"  
    theme_page(主题页)，如"http://bbs.nju.edu.cn/bbscon?board=Nirvana&file=M.1210959451.A"

数据库中维持三个表：
    lily_theme(存储主题类页面)、lily_reply(存储回复类页面)、lily_date(存储子论坛最新主题页的url和date)