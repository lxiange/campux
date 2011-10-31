
程序名：Crawler_Lily

程序作用：爬取南京大学小百合论坛所有主题页的内容，格式化后，存入数据库

程序结构：根据功能分成5个包
    1. General_Function    一些常用的辅助函数，独立于本程序
    2. Get_All_Subforum_Name    获取所有子论坛名
    3. Get_Url    获取frame_page和theme_page的url
    4. Store_New_Theme    存储theme_page的内容
    5. Main_Function    main函数

处理流程：one subforum url -> all frame_page url of this subforum-> all theme_page url of this subforum-> all theme_page content of this subforum-> store into DB

百合论坛结构(4层)：
    classefied_subforum(，或discussion,分类讨论区)，如"http://bbs.nju.edu.cn/bbsboa?sec=5"
    subforum(子论坛)，如"http://bbs.nju.edu.cn/bbsdoc?board=Nirvana"
    frame_page(目录页)，如"http://bbs.nju.edu.cn/bbsdoc?board=D_Computer&start=61&type=doc"  
    theme_page(主题页)，如"http://bbs.nju.edu.cn/bbscon?board=Nirvana&file=M.1210959451.A"

数据库中维持两个表：
    lily_theme(存储主题类页面)、lily_reply(存储回复类页面)