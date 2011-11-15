/*
 * ����DB�е�app_store��
 * ����have_No���ڼ��app_store�����Ƿ����û���Ϊuser_group_no����
 * ����have_App���ڼ������Ƿ�����Ϊapp_name������򷵻�true�����򷵻�false��
 * ����get_AllocatedSpace���ڻ�ȡ��Ϊapp_name�����space_allocatedֵ��
 * ����get_UsedSpace���ڻ�ȡ��Ϊapp_name�����space_usedֵ��
 * ����is_Empty�����ж���Ϊapp_name�����space�Ƿ�Ϊ�ա�
 * ����is_Full�����ж���Ϊapp_name�����space�Ƿ�������
 * ����set_AllocatedSpace����������Ϊapp_name�����space_allocatedֵ��
 * ����set_UsedSpace����������Ϊapp_name�����space_usedֵ��
 * ����append_Item��������û���
 * ����delete_Item����ɾ���û���
 * ����traverse_AS���ڱ������е�app_name��
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
	//���app_store�����Ƿ����û���Ϊuser_group_no����
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
	//���app_store�����Ƿ����û���Ϊapp_name����
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
	
	//����get_AllocatedSpace���ڻ�ȡ��Ϊapp_name�����space_allocatedֵ��
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

	//����get_UsedSpace���ڻ�ȡ��Ϊapp_name�����space_usedֵ��
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
	
	//����is_Empty�����ж���Ϊapp_name�����space�Ƿ�Ϊ�ա�
	public static boolean is_Empty(String app_name)
	{
		boolean flag=false;
		String space_used=get_UsedSpace(app_name);
		if(Integer.valueOf(space_used).intValue()==0)
			flag=true;
		return flag;
	}
	
	//����is_Full�����ж���Ϊapp_name�����space�Ƿ�������
	public static boolean is_Full(String app_name)
	{
		boolean flag=false;
		String space_used=get_UsedSpace(app_name);
		String space_allocated=get_AllocatedSpace(app_name);
		if(Integer.valueOf(space_used).intValue()==Integer.valueOf(space_allocated).intValue())
			flag=true;
		return flag;
	}
	
	//����set_AllocatedSpace����������Ϊapp_name�����space_allocatedֵ��
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

	//����set_UsedSpace����������Ϊapp_name�����space_usedֵ��
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
	
	//����append_Item��������û������������Ŀ¼���½���app_nameͬ�����ļ��С���
	public static boolean append_Item(String user_group_no,String app_name,String app_path_prefix,String space_allocated)
	{
        boolean flag=true;
      //�˴������޸�Ĭ��ֵ
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
		//�������Ŀ¼���½���app_nameͬ�����ļ���
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

	//����append_Item��������û�����space_allocated��ΪĬ��ֵ��
	public static boolean append_Item(String user_group_no,String app_name,String app_path_prefix)
	{
		//�˴������޸�Ĭ��ֵ(MB)
		long temp_int=100;
		String default_space_allocated=Long.toString(temp_int);
		return append_Item(user_group_no,app_name,app_path_prefix,default_space_allocated);
	}
	
	//����delete_Item����ɾ���û���
	public static boolean delete_Item(String app_name,String app_path_prefix)
	{
        boolean flag=true;
        
		//�������Ŀ¼��ɾ����app_nameͬ�����ļ���
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
	
	//����traverse_AS���ڱ������е�app_name��
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
