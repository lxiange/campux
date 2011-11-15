/*
 * ����DB�е�user_psw��
 * ����have_User���ڼ��user_authen�����Ƿ����û���Ϊuser_name������򷵻�true�����򷵻�false��
 * ����user_MatchPsw����user_name��user_psw�ڱ�user_authen�н���ƥ�䡣��ƥ��ɹ��򷵻�true�����򷵻�false��
 * ����append_NewUser����user_name��user_psw(ȷ��user_authen����û��user_name��֮��)�����user_name�user_authen���С�
 *     ����ӳɹ��򷵻�true�����򷵻�false��
 * ����delete_ItemInUP����ɾ�����ж�Ӧuser_name���(��Ӧ��ֻ����User_Delete�б����á�)
 * ����get_Psw����ȡ��ĳ�û�����Ӧ�����롣(��Ӧ��ֻ����User_Psw_Change�б����á�)
 * ����set_Psw�����޸�ĳ�û�����Ӧ�����롣(��Ӧ��ֻ����User_Psw_Change�б����á�)
 * ����get_Status���ڻ�ȡĳ�û�����Ӧ��status��
 * ����set_Status�����޸�ĳ�û�����Ӧ��status��
 * ����traverse_UP���ڱ���UP����Ԫ�أ�������user_name���ϡ�
 */
package Auxiliary_Function;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;
import java.util.*;
public class Connect_DBTable_UP 
{
	//���user_authen�����Ƿ����û���Ϊuser_name����
	public static boolean have_User(String user_name)
	{
		boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_psw where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("user_name").trim();
	
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
//			System.out.println("No such a user whose name is "+user_name+" in table user_authen.");
			flag=false;
		}
		return flag;
	}
	
	//����user_name��user_psw�ڱ�user_authen�н���ƥ�䡣��ƥ��ɹ��򷵻�true�����򷵻�false
	public static boolean user_MatchPsw(String user_name,String user_psw)
	{
		boolean flag=false;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_psw where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			String str=result.getString("user_psw").trim();
			if(user_psw.equals(str))
				flag=true;
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return flag; 
	}
		
	//����user_name��user_psw(ȷ��user_authen����û��user_name��֮��)�����user_name�user_authen����
	public static boolean append_NewUser(String user_name,String user_psw)
	{
        boolean flag=true;
		
        if(have_User(user_name)==true||user_MatchPsw(user_name,user_psw)==true)
		{
			System.out.println("This item have existed in table ran_con!");
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
			//workgroupĬ��ΪNull
			String query_statement="insert into user_psw values('"+ user_name+"','"+user_psw+"','0"+"');";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("Can't add new user with name: '"+user_name+"' to table user_authen.");
			s.printStackTrace();
			flag=false;
		}
		 
		return flag;
	}
	//delete_ItemInUPG����ɾ�����ж�Ӧuser_name�����ֻ����User_Delete�б����á�
	public static boolean delete_ItemInUP(String user_name)
	{
        boolean flag=true;
		
		if(have_User(user_name)==false)
		{
			System.out.println("No such user in table user_authen!");
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
			String query_statement=" delete from user_psw where user_name='"+user_name+"';";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("Can't delete item with user_name: '"+user_name+"' from table user_psw. This item doesn't exist!");
//			s.printStackTrace();
			flag=false;
			return flag;
		}
		return flag;
	}
	public static String get_Psw(String user_name)
	{
		String user_psw="";
		if(have_User(user_name)==false)
		{
			System.out.println("No such user in table user_authen!");
			return user_psw;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select *  from user_psw where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			user_psw=result.getString("user_psw").trim();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			return user_psw;
		}
		return user_psw;
	}
	
	
	public static boolean set_Psw(String user_name,String user_psw)
	{
		boolean flag=true;
		if(have_User(user_name)==false)
		{
			System.out.println("No such user in table user_authen!");
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
			String query_statement="update user_psw set user_psw='"+user_psw+"' where user_name='"+user_name+"';";
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("Can't change the psw of user_name: '"+user_name+"' from table user_psw.");
//			s.printStackTrace();
			flag=false;
			return flag;
		}
		return flag;
	}
	
	public static String get_Status(String user_name)
	{
		String user_status="";
		if(have_User(user_name)==false)
		{
			System.out.println("No such user in table user_authen!");
			return user_status;
		}
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select *  from user_psw where user_name='"+user_name+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			user_status=result.getString("user_status").trim();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			return user_status;
		}
		return user_status;
	}
	
	public static boolean set_Status(String user_name,String user_status)
	{
		boolean flag=true;
		if(have_User(user_name)==false)
		{
			System.out.println("No such user in table user_authen!");
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
			String query_statement="update user_psw set user_status='"+user_status+"' where user_name='"+user_name+"';";
			Visit_SQLServer.execute_SQLServer(statement,query_statement);			
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
	
	public static ArrayList<String> traverse_UP()
	{
		ArrayList<String> all_item=new ArrayList<String>();
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from user_psw;";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			while(result.next()==true)
            {
				all_item.add(result.getString("user_name").trim());
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
		String user_name="li";
		String user_psw="88s8";
		System.out.println(get_Status(user_name)+"aa");
		ArrayList<String> xx=traverse_UP();
		for(int i=0;i<xx.size();i++)
			System.out.println(xx.get(i));
		
//		boolean flag=have_User(user_name);
/*		boolean flag=user_MatchPsw(user_name,user_psw);
		if(flag==true) System.out.println("OK!");
		else 
		{
			System.out.println("NO!");
			boolean flag_a=append_NewUser(user_name,user_psw);
			if(flag_a==true)System.out.println("Append successful!");
			else System.out.println("Append failed!");
		}
*/	}

}
