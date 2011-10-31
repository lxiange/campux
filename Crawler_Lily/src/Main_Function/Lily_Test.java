package Main_Function;
import Get_All_Subforum_Name.*;

import java.io.*;
import java.util.*;
import java.sql.*;

import Store_New_Theme.*;

public class Lily_Test 
{
	public static void lily_Test()throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		ArrayList<String> arr_a=Lily_Get_AllSubForumName.lily_Get_ClassifiedSubForumName();
		ArrayList<String> arr_b=Lily_Get_AllSubForumName.lily_Get_SubForumName(arr_a.get(5));
		
		for(int i=0;i<arr_b.size();i++)
		{
			Lily_Get_ContentOfAllThemePage.lily_Get_ContentOfAllThemePage(arr_b.get(i));
		}
	}
	
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		lily_Test();
	}

}
