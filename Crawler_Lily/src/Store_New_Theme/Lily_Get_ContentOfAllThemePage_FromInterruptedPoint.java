/*
 *作用：为便于在爬取过程中中断及重启程序，设置可以在某断点处进行爬取，即，
 *      获取某subforum中的某interrupt_page之后的所有theme_page的内容 ，并存入数据库
 *输入：某subforum的url，interrupt_page(subforum中的某theme_page)的url
 *输出：此subforum中在interrupt_page之后的所有theme_page的格式化内容存入数据库
 *
 * 
 */
package Store_New_Theme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Get_Url.*;


public class Lily_Get_ContentOfAllThemePage_FromInterruptedPoint 
{
	public static void lily_Get_ContentOfAllThemePage_FromInterruptPoint(String url,String interrupt_point)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		//获取所有theme_page的url
		ArrayList<String> all_theme_url=Lily_GetAllThemePageUrl.lily_GetAllThemePageUrl(url);
	
		//获取某subforum中的某interrupt_page之后的所有theme_page的url
		int begin_index=0;
		for(int i=0;i<all_theme_url.size();i++)
		{
			if(all_theme_url.get(i).equals(interrupt_point))		
			{
				begin_index=i;
				break;
			}
		}
		ArrayList<String> remain_theme_url=new ArrayList<String>();
		for(int i=begin_index+1;i<all_theme_url.size();i++)
		{
			remain_theme_url.add(all_theme_url.get(i));
		}
		//将某subforum中的某interrupt_page之后的所有theme_page的格式化内容存入数据库
		for(int i=0;i<remain_theme_url.size();i++)
		{
			Lily_Get_ContentOfOneThemePage.lily_Get_ContentOfOneThemePage(remain_theme_url.get(i), i+1);
			Thread.sleep(1000);
		}

	
	}
	
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		String init_url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		String interrupt_point="http://bbs.nju.edu.cn/bbscon?board=Mediastudy&file=M.1213246782.A";
		lily_Get_ContentOfAllThemePage_FromInterruptPoint(init_url,interrupt_point);
	}

}
