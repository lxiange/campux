/*
 * 访问DB中的app_store表
 * 函数have_No用于检测app_store表中是否含有用户名为user_group_no的项
 * 函数have_App用于检测表中是否含有名为app_name的项，是则返回true，否则返回false。
 * 函数get_AllocatedSpace用于获取名为app_name的项的space_allocated值。
 * 函数get_UsedSpace用于获取名为app_name的项的space_used值。
 * 函数is_Empty用于判断名为app_name的项的space是否为空。
 * 函数is_Full用于判断名为app_name的项的space是否已满。
 * 函数set_AllocatedSpace用于设置名为app_name的项的space_allocated值。
 * 函数set_UsedSpace用于设置名为app_name的项的space_used值。
 * 函数append_Item用于添加用户。
 * 函数delete_Item用于删除用户。
 * 函数traverse_AS用于遍历表中的app_name。
 * 
 * 
 */
package Auxiliary_Function;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;
import General_Function.General_Function_AboutFile.Directory_Function;

import java.util.*;

public class Connect_DBTable_AS 
{
	//检测app_store表中是否含有用户名为user_group_no的项
	public static boolean have_No(String user_group_no)
	{
		boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from app_store where user_group_no='"+user_group_no+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("app_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	//检测app_store表中是否含有用户名为app_name的项
	public static boolean have_App(String app_name)
	{
		boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from app_store where app_name='"+app_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("app_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	
	//函数get_AllocatedSpace用于获取名为app_name的项的space_allocated值。
	public static String get_AllocatedSpace(String app_name)
	{
		String result="";
		if(have_App(app_name)==false)
		{
			System.out.println("No such app in table app_store!");
			return result;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select *  from app_store where app_name='"+app_name+"';";
			ResultSet result_a=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result_a.next();
			result=result_a.getString("space_allocated").trim();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			return result;
		}
		return result;
	}

	//函数get_UsedSpace用于获取名为app_name的项的space_used值。
	public static String get_UsedSpace(String app_name)
	{
		String result="";
		if(have_App(app_name)==false)
		{
			System.out.println("No such app in table app_store!");
			return result;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select *  from app_store where app_name='"+app_name+"';";
			ResultSet result_a=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result_a.next();
			result=result_a.getString("space_used").trim();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			return result;
		}
		return result;
	}
	
	//函数is_Empty用于判断名为app_name的项的space是否为空。
	public static boolean is_Empty(String app_name)
	{
		boolean flag=false;
		String space_used=get_UsedSpace(app_name);
		if(Integer.valueOf(space_used).intValue()==0)
			flag=true;
		return flag;
	}
	
	//函数is_Full用于判断名为app_name的项的space是否已满。
	public static boolean is_Full(String app_name)
	{
		boolean flag=false;
		String space_used=get_UsedSpace(app_name);
		String space_allocated=get_AllocatedSpace(app_name);
		if(Integer.valueOf(space_used).intValue()==Integer.valueOf(space_allocated).intValue())
			flag=true;
		return flag;
	}
	
	//函数set_AllocatedSpace用于设置名为app_name的项的space_allocated值。
	public static boolean set_AllocatedSpace(String app_name,String space_allocated)
	{
		boolean flag=true;
		if(have_App(app_name)==false)
		{
			System.out.println("No such app in table app_store!");
			flag=false;
			return flag;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="update app_store set space_allocated='"+space_allocated+"' where app_name='"+app_name+"';";
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
	//		s.printStackTrace();
			return flag;
		}
		return flag;
	}

	//函数set_UsedSpace用于设置名为app_name的项的space_used值。
	public static boolean set_UsedSpace(String app_name,String space_used)
	{
		boolean flag=true;
		if(have_App(app_name)==false)
		{
			System.out.println("No such app in table app_store!");
			flag=false;
			return flag;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="update app_store set space_used='"+space_used+"' where app_name='"+app_name+"';";
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
	//		s.printStackTrace();
			return flag;
		}
		return flag;
	}
	
	//函数append_Item用于添加用户，并在物理根目录下新建与app_name同名的文件夹。。
	public static boolean append_Item(String user_group_no,String app_name,String app_path_prefix,String space_allocated)
	{
        boolean flag=true;
      //此处用于修改默认值
        long temp_int=Integer.valueOf(space_allocated).longValue()*1024*1024;
        space_allocated=Long.toString(temp_int);
        if(have_App(app_name)||have_No(user_group_no))
		{
			System.out.println("This item have existed in table app_store!");
			flag=false;
			return flag;
		}
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="insert into app_store values('"+user_group_no+"','"+ app_name+"','"+space_allocated+"','0"+"');";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
		    s.printStackTrace();
			flag=false;
		}
		//在物理根目录下新建与app_name同名的文件夹
		File apr=new File(app_path_prefix);
		System.out.println(app_path_prefix);
		System.out.println(app_name);
		System.out.println(app_path_prefix+app_name);
		if(Directory_Function.add_SubDirectory(apr, app_name)==false)
		{
			flag=false;
		}
		 
		return flag;
	}

	//函数append_Item用于添加用户，其space_allocated项为默认值。
	public static boolean append_Item(String user_group_no,String app_name,String app_path_prefix)
	{
		//此处用于修改默认值(MB)
		long temp_int=100;
		String default_space_allocated=Long.toString(temp_int);
		return append_Item(user_group_no,app_name,app_path_prefix,default_space_allocated);
	}
	
	//函数delete_Item用于删除用户。
	public static boolean delete_Item(String app_name,String app_path_prefix)
	{
        boolean flag=true;
        
		//在物理根目录下删除与app_name同名的文件夹
		File apr=new File(app_path_prefix);
/*		System.out.println(app_path_prefix);
		System.out.println(app_name);
		System.out.println(app_path_prefix+app_name);
*/		if(!Directory_Function.delete_SubDirectory(apr, app_name))
		{
			flag=false;
		}
        if(!have_App(app_name))
		{
//			System.out.println("No such app in table app_store!");
			flag=false;
			return flag;
		}
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="delete from app_store where app_name='"+app_name+"';";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
//		    s.printStackTrace();
			flag=false;
		}
		
		return flag;
	}
	
	//函数traverse_AS用于遍历表中的app_name。
	public static ArrayList<String> traverse_AS()
	{
		ArrayList<String> all_item=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from app_store;";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			while(result.next()==true)
            {
				all_item.add(result.getString("app_name").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			return all_item;
		}
		
		return all_item;
	}
	

	public static void main(String[] args)
	{
		String app_name="q";
//		String time="100";
//		System.out.println(get_AllocatedSpace(app_name));
//		System.out.println(get_UsedSpace(app_name));
		if(have_No(app_name))
			System.out.println("OK");
		else 
			System.out.println("No");
		
		ArrayList<String> xx=traverse_AS();
		for(int i=0;i<xx.size();i++)
			System.out.println(xx.get(i));
	}

}
