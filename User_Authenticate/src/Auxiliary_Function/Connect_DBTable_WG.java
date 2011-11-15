/*
 * 访问DB中的workgroup表
 * 函数have_NoInWG用于检测workgroup表中是否含有group_no的项，是则返回true，否则返回false。
 * 函数have_NameInWG用于检测workgroup表中是否含有group_name的项，是则返回true，否则返回false。
 * 函数have_ItemInWG用于检测workgroup表中是否含有(group_no,group_name)的项，是则返回true，否则返回false。
 * 函数get_NameByNo用于根据group_no获得group_name，异常情况下返回空串
 * 函数get_NoByName用于根据group_name获得group_no，异常情况下返回空串
 * 函数get_AllGroupsName用于取得workgroup表内的所有group_name，以ArrayList返回。
 * 函数get_AllGroupsNo用于取得workgroup表内的所有group_no，以ArrayList返回。
 * 函数get_AutoNoForNewItem用于自动为新添加的group_name生成合法的group_no。
 * 函数append_ItemInWG用于将(group_no,group_name)项插入workgroup表。插入成功返回true，否则返回false。
 * 函数delete_ItemInWG用于将(group_no,group_name)项从workgroup表中删除。删除成功返回true，否则返回false。
 * 
 */
package Auxiliary_Function;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import General_Function.Visit_SQLServer;

public class Connect_DBTable_WG 
{
	//函数have_NoInWG用于检测workgroup表中是否含有group_no的项，是则返回true，否则返回false。
	public static boolean have_NoInWG(String group_no)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from workgroup where group_no='"+group_no+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("group_no").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	
	
	//函数have_NameInWG用于检测workgroup表中是否含有group_name的项，是则返回true，否则返回false。
	public static boolean have_NameInWG(String group_name)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from workgroup where group_name='"+group_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("group_no").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	
	
	//函数have_ItemInWG用于检测workgroup表中是否含有(group_no,group_name)的项，是则返回true，否则返回false。
	public static boolean have_ItemInWG(String group_no,String group_name)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from workgroup where group_no='"+group_no+"' and group_name='"+group_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("group_no").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	 
	
	//函数get_NameByNo用于根据group_no获得group_name
	public static String get_NameByNo(String group_no)
	{
        String group_name="";

		if(have_NoInWG(group_no)==false)
		{
			System.out.println("No such item in table workgroup!");
			return group_name;
		}
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from workgroup where group_no='"+group_no+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			group_name=result.getString("group_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return group_name;
	}
	
	
    //函数get_NoByName用于根据group_name获得group_no
	public static String get_NoByName(String group_name)
	{
        String group_no="";

		if(have_NameInWG(group_name)==false)
		{
			System.out.println("No such item in table workgroup!");
			return group_no;
		}
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from workgroup where group_name='"+group_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			group_no=result.getString("group_no").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return group_no;
	}
	
	//函数get_AllGroupsName用于取得workgroup表内的所有group_name，以ArrayList返回。
	public static ArrayList<String> get_AllGroupsName()
	{
		ArrayList<String> groups=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select group_name from workgroup;";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
            	groups.add(result.getString("group_name").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return groups;
	}
	
	//函数get_AllGroupsNo用于取得workgroup表内的所有group_no，以ArrayList返回。
	public static ArrayList<String> get_AllGroupsNo()
	{
		ArrayList<String> groups=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select group_no from workgroup;";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
            	groups.add(result.getString("group_no").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}

		return groups;
	}
	//函数get_AutoNoForNewItem用于自动为新添加的group_name生成合法的group_no。
	public static String get_AutoNoForNewItem()
	{
		String new_no="";
		ArrayList<String> groups = get_AllGroupsNo();
		for(int i=0;i<groups.size();i++)
		{
			if(Integer.valueOf(groups.get(i)).intValue()!=(i+1))
			{
				new_no=Integer.toString(i+1);
				return new_no;
			}
		}
		new_no=Integer.toString(groups.size()+1);
		return new_no;
	}
		

	//函数append_ItemInWG用于将(group_no,group_name)项插入workgroup表。插入成功返回true，否则返回false。
	public static boolean append_ItemInWG(String group_no,String group_name)
	{
        boolean flag=true;
        
        if(have_ItemInWG(group_no,group_name)==true)
		{
			System.out.println("This item have existed in table workgroup!");
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
			String query_statement="insert into workgroup values('"+ group_no+"','"+group_name+"');";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		 
		return flag;
	}	
	
	//函数delete_ItemInWG用于将(group_no,group_name)项从workgroup表中删除。删除成功返回true，否则返回false。
	public static boolean delete_ItemInWG(String group_no,String group_name)
	{
        boolean flag=true;
        
        if(have_ItemInWG(group_no,group_name)==false)
		{
			System.out.println("No such item in table workgroup!");
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
			String query_statement="delete from workgroup where group_no='"+ group_no+"' and group_name='"+group_name+"';";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		 
		return flag;
	}
	

	public static void main(String[] args)
	{
		String group_no="8";
		String group_name="group8";
		String user_name="zhou";
		ArrayList<String> groups =get_AllGroupsName();
		for(int i=0;i<groups.size();i++)
			System.out.println(groups.get(i));
//		System.out.println(get_AutoNoForNewItem());
/*		if(delete_ItemInWG(group_no,group_name)==true)
			System.out.println("OK");
		else System.out.println("NO");
*///		System.out.println(get_NoByName(group_name));
/*		if(have_ItemInWG(group_no,group_name)==true)
			System.out.println("OK");
		else System.out.println("NO");
*/	}
}
