/*
 *���ã�Ϊ��������ȡ�������жϼ������������ÿ�����ĳ�ϵ㴦������ȡ������
 *      ��ȡĳsubforum�е�ĳinterrupt_page֮�������theme_page������ �����������ݿ�
 *���룺ĳsubforum��url��interrupt_page(subforum�е�ĳtheme_page)��url
 *�������subforum����interrupt_page֮�������theme_page�ĸ�ʽ�����ݴ������ݿ�
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
		//��ȡ����theme_page��url
		ArrayList<String> all_theme_url=Lily_GetAllThemePageUrl.lily_GetAllThemePageUrl(url);
	
		//��ȡĳsubforum�е�ĳinterrupt_page֮�������theme_page��url
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
		//��ĳsubforum�е�ĳinterrupt_page֮�������theme_page�ĸ�ʽ�����ݴ������ݿ�
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
