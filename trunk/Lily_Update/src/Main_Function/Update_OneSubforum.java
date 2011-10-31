package Main_Function;
/*
 * 主程序1: 更新一个subforum
 * 
 * 
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import General_Function.*;
import Get_All_Subforum_Name.Lily_Get_AllSubForumName;
import Get_New_Url.Lily_GetThemePageUrl_OfNewFramePage;
import Get_Old_Newest_Theme.*;
import Set_New_Newest_Theme.Lily_RefreshNewestUrl;
import Store_New_Theme.Lily_Get_ContentOfOneThemePage;
import Store_New_Theme.Lily_Get_ContentOfSomeThemePage_InLatestDate;

public class Update_OneSubforum 
{
	public static void update_OneSubforum(String subforum_name)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		//取得lily_date表中的subforum_name对应的theme_url
		ArrayList<String> old_arr=Lily_FetchOldNewestUrl.lily_Fetch_Old_Newest_Url(subforum_name);
		String old_url=old_arr.get(1);
		//lily_date表中没有subforum_name的情况
		if(old_url.equals("NotFound"))
		{
			String temp_str=Fragment_Searcher.fragment_Eraser(subforum_name, "", "http://bbs.nju.edu.cn/bbsdoc?board=");
			System.out.println("There is no subforum with name: "+temp_str);
			return;
		}
/*		for(int i=0;i<old_arr.size();i++)
			System.out.println(old_arr.get(i));
*/		String old_date=old_arr.get(2);
		int new_page=Integer.valueOf(old_arr.get(3)).intValue();
		int page_increment=Integer.valueOf(old_arr.get(4)).intValue();
		
		//取得new_page个目录页中含有的主题页的所有url
		ArrayList<String> url_arr=Lily_GetThemePageUrl_OfNewFramePage.lily_GetThemePageUrl_OfNewFramePage(subforum_name,new_page,page_increment);
		int location=Search_In_Arr.search_In_Arr(old_url,url_arr);
		//new_page个目录页中的主题页不包含old_url的情况
		int time=0;
		while(location<0)
		{
			Thread.sleep(1000);
			new_page+=page_increment;
			time++;
			//如果较多页里面都没有old_newest_url，可能是那一页已经被网站删除，采用date比较法，暂定只存储new_page+5*page_increment目录页内含有的主题页
			if(time>=5)
			{
				Date begin_date=Lily_Get_ContentOfSomeThemePage_InLatestDate.date_Analyzer(old_date);
				
				Calendar new_calendar=Calendar.getInstance();
				new_calendar.set(2020,1,1,0,0,0);
				Date end_date=new_calendar.getTime();
				Lily_Get_ContentOfSomeThemePage_InLatestDate.lily_Get_ContentOfSomeThemePage_InLatestDate(subforum_name,new_page,begin_date,end_date);
			    break;
			}
				
			url_arr=Lily_GetThemePageUrl_OfNewFramePage.lily_GetThemePageUrl_OfNewFramePage(subforum_name,new_page,page_increment);
			location=Search_In_Arr.search_In_Arr(old_url,url_arr);
		}
		
		//把取得的新主题页处理后存入数据库
		for(int i=0;i<location;i++)
		{
			Lily_Get_ContentOfOneThemePage.lily_Get_ContentOfOneThemePage(url_arr.get(i), i+1);
			Thread.sleep(1000);
		}
		
		//更新数据库中的lily_date表中对应项的数据
		if(location!=0)
		{
			Lily_RefreshNewestUrl.lily_Refresh_Newest_Url(subforum_name,url_arr.get(0));
		}
		else
		{
			String temp_str=Fragment_Searcher.fragment_Eraser(subforum_name, "", "http://bbs.nju.edu.cn/bbsdoc?board=");
			System.out.println("No new theme in subforum with name: "+temp_str);
		}			
	}
	
	
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
//		String subforum="http://bbs.nju.edu.cn/bbsdoc?board=D_Computer";
		String subforum="http://bbs.nju.edu.cn/bbsdoc?board=AcademicReport";
		update_OneSubforum(subforum);
			
		
	}

}
