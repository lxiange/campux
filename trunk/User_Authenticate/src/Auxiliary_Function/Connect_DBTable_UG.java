/*
 * 访问DB中的user_group表
 * 函数have_ItemInUG用于检测user_group表中是否含有(user_group_no,user_name,group_no)的项，是则返回true，否则返回false。
 * 函数get_GroupsByUser用于取得某user_name对应的所有group_name，以ArrayList返回。
 * 函数get_GroupsNoByUser用于取得某user_name对应的所有group_no，以ArrayList返回。
 * 函数get_UsersByGroup用于取得某group_name对应的所有user_name，以ArrayList返回。
 * 函数append_ItemInUG用于将(user_name,group_no)项插入user_group表。插入成功返回true，否则返回false。
 * 函数delete_ItemInUG用于将(user_name,group_no)项从user_group表中删除。删除成功返回true，否则返回false。
 */
package Auxiliary_Function;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;
import java.util.*;

public class Connect_DBTable_UG 
{
	//函数get_AllGroupsNo用于取得user_group表内的所有user_group_no，以ArrayList返回。
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
			String query_statement="select user_group_no from user_group;";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
            	groups.add(result.getString("user_group_no").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}

		return groups;
	}
	//函数get_AutoNoForNewItem用于自动为新添加的(user_name,group_no)生成合法的user_group_no。
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
		
	
	
	
	//函数have_ItemInUG用于检测user_group表中是否含有(user_group_no,user_name,group_no)的项，是则返回true，否则返回false。
	public static boolean have_ItemInUG(String user_group_no,String user_name,String group_no)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_group where user_group_no='"+user_group_no+"' and user_name='"+user_name+"' and group_no='"+group_no+"';";
//			System.out.println(query_statement);
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("user_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	
	//函数have_ItemInUG用于检测user_group表中是否含有(user_name,group_no)的项，是则返回true，否则返回false。
	public static boolean have_ItemInUG(String user_name,String group_no)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_group where user_name='"+user_name+"' and group_no='"+group_no+"';";
		//	System.out.println(query_statement);
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("user_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		return flag;
	}
	
	//函数get_GroupsByUser用于取得某user_name对应的所有group_name，以ArrayList返回。
	public static ArrayList<String> get_GroupsByUser(String user_name)
	{
		ArrayList<String> groups=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_group where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
       //     	groups.add(result.getString("group_no").trim());
            	query_statement="select * from workgroup where group_no='"+result.getString("group_no").trim()+"';";
            	result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            	result.next();
            	groups.add(result.getString("group_name").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return groups;
	}
	//函数get_GroupsNoByUser用于取得某user_name对应的所有group_no，以ArrayList返回。
	public static ArrayList<String> get_GroupsNoByUser(String user_name)
	{
		ArrayList<String> groups=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_group where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
       //     	groups.add(result.getString("group_no").trim());
            	query_statement="select * from workgroup where group_no='"+result.getString("group_no").trim()+"';";
            	result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            	result.next();
            	groups.add(result.getString("group_no").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return groups;
	}
	
	//函数get_UsersByGroup用于取得某group_name对应的所有user_name，以ArrayList返回。
	public static ArrayList<String> get_UsersByGroup(String group_no)
	{
		ArrayList<String> users=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_group where group_no='"+group_no+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
            while(result.next()==true)
            {
            	users.add(result.getString("user_name").trim());
            }
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return users;
	}
	
	//函数append_ItemInUG用于将(user_name,group_no)项插入user_group表，其前提是表中不存在此项。插入成功返回true，否则返回false。
	//如果group_no在表workgroup中对应的group_name是"app"，则还需要在表app_store中添加(user_group_no,user_name,default_space_allocated,0)项，并在物理根目录下新建与user_name同名的文件夹。
	public static boolean append_ItemInUG(String user_name,String group_no,String app_path_prefix)
	{
        boolean flag=true;
        
        if(have_ItemInUG(user_name,group_no)==true)
		{
			System.out.println("This item have existed in table user_group!");
			flag=false;
			return flag;
		}
		String user_group_no=get_AutoNoForNewItem();
		String group_name=Connect_DBTable_WG.get_NameByNo(group_no).trim();
//		System.out.println(group_name);
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="insert into user_group values('"+ user_group_no+"','"+user_name+"','"+group_no+"');";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
			if(group_name.equals("app"))
			{
				Connect_DBTable_AS.append_Item(user_group_no,user_name,app_path_prefix);
			}
			
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
		}
		 
		return flag;
	}
	
	//函数delete_ItemInUG用于将(user_name,group_no)项从user_group表中删除，其前提是表中存在此项。删除成功返回true，否则返回false。
	//如果group_no在表workgroup中对应的group_name是"app"，则还需要在表app_store中删除(user_group_no,user_name,default_space_allocated,0)项(由于在数据库中设定了关系，此步会自动完成)，并在物理根目录下删除与user_name同名的文件夹。
	public static boolean delete_ItemInUG(String user_name, String group_no,String app_path_prefix)
	{
		boolean flag=true;
		String group_name=Connect_DBTable_WG.get_NameByNo(group_no).trim();
		
		if(have_ItemInUG(user_name,group_no)==false)
		{
			System.out.println("No such item in table user_group!");
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
			String query_statement=" delete from user_group where user_name='"+user_name+"' and group_no='"+group_no+"';";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
			
			if(group_name.equals("app"))
			{
				Connect_DBTable_AS.delete_Item(user_name,app_path_prefix);
			}
			
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			flag=false;
			return flag;
		}
		return flag;
	}
	
	
	public static void main(String[] args)
	{
		String user_name="admin";
		String group_no="2";
		String absolute_path_prefix="D:\\all_app\\";
//		String user_group_no="2";
		
		if(append_ItemInUG(user_name,group_no,absolute_path_prefix)==true)
			System.out.println("OK");
		else System.out.println("NO");
		
/*		if(delete_ItemInUG(user_name,group_no)==true)
			System.out.println("OK");
		else System.out.println("NO");
*/		
		ArrayList<String> users=get_AllGroupsNo();
		for(int i=0;i<users.size();i++)
			System.out.println(users.get(i));
		
/*		ArrayList<String> groups=get_GroupsByUser(user_name);
		for(int i=0;i<groups.size();i++)
			System.out.println(groups.get(i));
*/		
/*		if(have_ItemInUG(user_name,group_no)==true)
			System.out.println("OK");
		else System.out.println("NO");
*/
	}
	

}
