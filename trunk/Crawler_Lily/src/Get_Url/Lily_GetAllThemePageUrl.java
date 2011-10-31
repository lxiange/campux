/* ���ã���ȡĳ��subforum����������ҳ��url
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
		
		//��ȡdomain
		String domain=Fragment_Searcher.fragment_Eraser(subforum, "", "http://bbs.nju.edu.cn/bbsdoc?board=");
		
		//subforumֻ��һҳ�����
		if(all_frame_page.size()==1)
		{
			System.out.println("Now analyze the content page of "+domain+" no."+"1");
			ArrayList<String> temp_arr=Lily_GetThemePageUrl_OfOneFramePage.lily_GetThemePageUrl_OfOneFramePage(all_frame_page.get(0));
			for(int k=0;k<temp_arr.size();k++)
				all_theme_page.add(temp_arr.get(k));
		}
		//�ж����һҳ�Ƿ���20������
		else		
		{
			int residue=Lily_GetAllFramePageUrl.lily_Judge_LastPage(subforum);
			//subforum���ж�ҳ�����
			//���һҳ����20����������
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
			//���һҳ������20����������
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
				//�������һҳ��Ҫ��ȡ��20��theme_page url(rough)���ٽ��е�һ�ι��ˣ�������ܻ����
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
		
		//ɾ���ظ�������ҳurl
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
