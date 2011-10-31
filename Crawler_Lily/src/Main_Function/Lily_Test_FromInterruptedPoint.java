
package Main_Function;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import Get_Url.*;
import Get_All_Subforum_Name.*;
import Store_New_Theme.*;

public class Lily_Test_FromInterruptedPoint 
{
	public static void lily_Test_FromInterruptedPoint(String interrupt_subforum,String interrupt_point)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		//获取所有classified_subforum的name
		ArrayList<String> arr_a=Lily_Get_AllSubForumName.lily_Get_ClassifiedSubForumName();
		//获取某个classified_subforum的所有subforum的name
		ArrayList<String> arr_b=Lily_Get_AllSubForumName.lily_Get_SubForumName(arr_a.get(6));
		
		ArrayList<String> arr_c=new ArrayList<String>();
		int begin_index=0;
		
		//寻找interrupt_subforum的位置
		for(int i=0;i<arr_b.size();i++)
			if(arr_b.get(i).equals(interrupt_subforum))
				begin_index=i;
		//寻找interrupt_theme_page的位置
		for(int i=begin_index;i<arr_b.size();i++)
			arr_c.add(arr_b.get(i));
				
		for(int i=0;i<arr_c.size();i++)
		{
			Lily_Get_ContentOfAllThemePage_FromInterruptedPoint.lily_Get_ContentOfAllThemePage_FromInterruptPoint(arr_c.get(i), interrupt_point);
			Thread.sleep(5000);
		}
	}
	
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{                             
		String interrupt_subforum="http://bbs.nju.edu.cn/bbsdoc?board=PCGames";//http://bbs.nju.edu.cn/bbsdoc?board=QuYi
		String interrupt_point="";
		lily_Test_FromInterruptedPoint(interrupt_subforum,interrupt_point);
	}

}
