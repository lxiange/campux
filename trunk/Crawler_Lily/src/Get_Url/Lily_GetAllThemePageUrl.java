/* 作用：获取某个subforum的所有主题页的url
 * 
 * 
 */

package Get_Url;
import java.io.IOException;
import java.util.*;
import General_Function.*;

public class Lily_GetAllThemePageUrl 
{
	public static ArrayList<String> lily_GetAllThemePageUrl(String subforum)throws IOException,InterruptedException
	{
		ArrayList<String> all_theme_page=new ArrayList<String>();
		ArrayList<String> all_frame_page=Lily_GetAllFramePageUrl.lily_GetAllFramePage(subforum);
		
		//获取domain
		String domain=Fragment_Searcher.fragment_Eraser(subforum, "", "http://bbs.nju.edu.cn/bbsdoc?board=");
		
		//subforum只有一页的情况
		if(all_frame_page.size()==1)
		{
			System.out.println("Now analyze the content page of "+domain+" no."+"1");
			ArrayList<String> temp_arr=Lily_GetThemePageUrl_OfOneFramePage.lily_GetThemePageUrl_OfOneFramePage(all_frame_page.get(0));
			for(int k=0;k<temp_arr.size();k++)
				all_theme_page.add(temp_arr.get(k));
		}
		//判断最后一页是否含有20个主题
		else		
		{
			int residue=Lily_GetAllFramePageUrl.lily_Judge_LastPage(subforum);
			//subforum含有多页的情况
			//最后一页含有20个主题的情况
			if(residue==0)
			{
				for(int i=0;i<all_frame_page.size();i++)
				{
					System.out.println("Now analyze the content page of "+domain+" no."+(i+1));
					ArrayList<String> temp_arr=Lily_GetThemePageUrl_OfOneFramePage.lily_GetThemePageUrl_OfOneFramePage(all_frame_page.get(i));
					for(int k=0;k<temp_arr.size();k++)
						all_theme_page.add(temp_arr.get(k));
					
					try{Thread.sleep(400);}catch(Exception e){}
				}
			}
			//最后一页不含有20个主题的情况
			else
			{
				for(int i=0;i<all_frame_page.size()-1;i++)
				{
					System.out.println("Now analyze the content page of "+domain+" no."+(i+1));
					ArrayList<String> temp_arr=Lily_GetThemePageUrl_OfOneFramePage.lily_GetThemePageUrl_OfOneFramePage(all_frame_page.get(i));
					for(int k=0;k<temp_arr.size();k++)
					    all_theme_page.add(temp_arr.get(k));
					
					try{Thread.sleep(400);}catch(Exception e){}
				}
				//对于最后一页，要先取得20个theme_page url(rough)，再进行第一次过滤，否则可能会出错
				ArrayList<String> temp_arr=Lily_GetThemePageUrl_OfOneFramePage.lily_Get_TitlesOfOneMainPage(all_frame_page.get(all_frame_page.size()-1));
				for(int k=20-residue;k<temp_arr.size();k++)
				{
					if(!Lily_GetThemePageUrl_OfOneFramePage.pretreatment_1(temp_arr.get(k)))
					{
						String temp_str=Fragment_Searcher.fragment_Searcher_WithoutBoundary(all_theme_page.get(k), "", ">");
						temp_arr.set(k, temp_str);
					}
					else
					{
						temp_arr.remove(k);
						k--;
					}
				}
				for(int k=20-residue;k<temp_arr.size();k++)
				{
					String str=Fragment_Searcher.fragment_Searcher_WithoutBoundary(all_theme_page.get(k),"","&num=");
					all_theme_page.add(temp_arr.get(k));
				}
				
			}			
		}
		
		//删除重复的主题页url
		Remove_Duplicate.remove_Duplicate(all_theme_page);
		return all_theme_page;
	}
	
	public static void main(String[] args)throws IOException,InterruptedException
	{
     	String url="http://bbs.nju.edu.cn/bbsdoc?board=Olympics";
     	ArrayList<String> all_theme_page=lily_GetAllThemePageUrl(url);
     	for(int i=0;i<all_theme_page.size();i++)
     		System.out.println(all_theme_page.get(i));
     	System.out.println(all_theme_page.size());
	}

}
