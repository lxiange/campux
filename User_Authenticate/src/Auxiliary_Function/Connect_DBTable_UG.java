/*
 * ����DB�е�user_group��
 * ����have_ItemInUG���ڼ��user_group�����Ƿ���(user_group_no,user_name,group_no)������򷵻�true�����򷵻�false��
 * ����get_GroupsByUser����ȡ��ĳuser_name��Ӧ������group_name����ArrayList���ء�
 * ����get_GroupsNoByUser����ȡ��ĳuser_name��Ӧ������group_no����ArrayList���ء�
 * ����get_UsersByGroup����ȡ��ĳgroup_name��Ӧ������user_name����ArrayList���ء�
 * ����append_ItemInUG���ڽ�(user_name,group_no)�����user_group������ɹ�����true�����򷵻�false��
 * ����delete_ItemInUG���ڽ�(user_name,group_no)���user_group����ɾ����ɾ���ɹ�����true�����򷵻�false��
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
	//����get_AllGroupsNo����ȡ��user_group���ڵ�����user_group_no����ArrayList���ء�
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
	//����get_AutoNoForNewItem�����Զ�Ϊ����ӵ�(user_name,group_no)���ɺϷ���user_group_no��
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
		
	
	
	
	//����have_ItemInUG���ڼ��user_group�����Ƿ���(user_group_no,user_name,group_no)������򷵻�true�����򷵻�false��
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
	
	//����have_ItemInUG���ڼ��user_group�����Ƿ���(user_name,group_no)������򷵻�true�����򷵻�false��
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
	
	//����get_GroupsByUser����ȡ��ĳuser_name��Ӧ������group_name����ArrayList���ء�
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
	//����get_GroupsNoByUser����ȡ��ĳuser_name��Ӧ������group_no����ArrayList���ء�
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
	
	//����get_UsersByGroup����ȡ��ĳgroup_name��Ӧ������user_name����ArrayList���ء�
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
	
	//����append_ItemInUG���ڽ�(user_name,group_no)�����user_group����ǰ���Ǳ��в����ڴ������ɹ�����true�����򷵻�false��
	//���group_no�ڱ�workgroup�ж�Ӧ��group_name��"app"������Ҫ�ڱ�app_store�����(user_group_no,user_name,default_space_allocated,0)����������Ŀ¼���½���user_nameͬ�����ļ��С�
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
	
	//����delete_ItemInUG���ڽ�(user_name,group_no)���user_group����ɾ������ǰ���Ǳ��д��ڴ��ɾ���ɹ�����true�����򷵻�false��
	//���group_no�ڱ�workgroup�ж�Ӧ��group_name��"app"������Ҫ�ڱ�app_store��ɾ��(user_group_no,user_name,default_space_allocated,0)��(���������ݿ����趨�˹�ϵ���˲����Զ����)�����������Ŀ¼��ɾ����user_nameͬ�����ļ��С�
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
