/*
 *���ã���ȡĳsubforum�е�����theme_page������ 
 *���룺ĳsubforum��url
 *�������subforum�е�����theme_page�ĸ�ʽ�����ݴ������ݿ�
 *
 * 
 */
package Store_New_Theme;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import Get_Url.*;

public class Lily_Get_ContentOfAllThemePage
{
	public static void lily_Get_ContentOfAllThemePage(String url)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		ArrayList<String> all_theme_url=Lily_GetAllThemePageUrl.lily_GetAllThemePageUrl(url);
		for(int i=0;i<all_theme_url.size();i++)
		{
			Lily_Get_ContentOfOneThemePage.lily_Get_ContentOfOneThemePage(all_theme_url.get(i), i+1);
			try{Thread.sleep(1000);}catch(Exception e){}
		}
	}
	
	
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		//    	String url="http://bbs.nju.edu.cn/bbsdoc?board=IFA_IS";
    	//		String url="http://bbs.nju.edu.cn/bbsdoc?board=NJU_zhixing";
		//       String url="http://bbs.nju.edu.cn/bbsdoc?board=NJU_HOME";

 //       String url="http://bbs.nju.edu.cn/bbsdoc?board=WesternstyleChess";
		String url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
    	lily_Get_ContentOfAllThemePage(url);
    	

	}

}
