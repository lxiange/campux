/*
 * ����DB�е�ran_con��
 * ����create_RCN���ڸ���user_name����һ����һ�޶���ran_con_number������(ran_con_number,user_name)�����ran_con��
 * ����insert_RCL���ڽ�(ran_con_number,user_name)�����ran_con��
 * ����have_Number���ڼ������Ƿ���ran_con_number��
 * ����get_UserByRCN���ڸ���ran_con_number��ȡuser_name
 * ����number_MatchUser����ran_con_number��user_name�ڱ�ran_con�н���ƥ�䡣��ƥ��ɹ��򷵻�true�����򷵻�false��
 * ����delete_ItemInRCL����ɾ��(ran_con_number, user_name)�
 */

package Auxiliary_Function;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;

public class Connect_DBTable_RCL 
{
	public static String create_RCN(String user_name)
	{
		String ran_con_number="";
		char[] arr=user_name.toCharArray();
		int total=0;
		for(int i=0;i<arr.length;i++)
			total+=(int)arr[i];
		
		ran_con_number=Integer.toString(total);
		insert_RCL(ran_con_number,user_name);
		return ran_con_number;
	}
	
	public static boolean insert_RCL(String ran_con_number,String user_name)
	{
        boolean flag=true;
		
        if(have_Number(ran_con_number)==true||number_MatchUser(ran_con_number,user_name)==true)
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
			String query_statement="insert into ran_con values('"+ ran_con_number+"','"+user_name+"');";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("Can't add user with ran_con_number: '"+ran_con_number+"' to table ran_con. This item have existed!");
//			s.printStackTrace();
			flag=false;
		}
		 
		return flag;
	}
	
	public static String get_UserByRCN(String ran_con_number)
	{
        String user_name="";
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from ran_con where ran_con_number='"+ran_con_number+"';";
	        ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			user_name=result.getString("user_name").trim();
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("There is no user with ran_con_number: '"+ran_con_number+"' in table ran_con.");
//			s.printStackTrace();
		}
		 
		return user_name;
		
	}
	
	public static boolean have_Number(String ran_con_number)
	{
        boolean flag=true;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from ran_con where ran_con_number='"+ran_con_number+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
            result.getString("ran_con_number").trim();
	
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
	
	
	//����ran_con_number��user_name�ڱ�ran_con�н���ƥ�䡣��ƥ��ɹ��򷵻�true�����򷵻�false
	public static boolean number_MatchUser(String ran_con_number,String user_name)
	{
		boolean flag=false;
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name_b="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name_b,psw);
			String query_statement="select * from ran_con where ran_con_number='"+ran_con_number+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			String str=result.getString("user_name").trim();
			if(user_name.equals(str))
				flag=true;
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s){}
		
		return flag; 
	}
	
	
	public static boolean delete_ItemInRCL(String ran_con_number,String user_name)
	{
		boolean flag=true;
		
		if(have_Number(ran_con_number)==false||number_MatchUser(ran_con_number,user_name)==false)
		{
			System.out.println("No such item in table ran_con!");
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
			String query_statement=" delete from ran_con where ran_con_number='"+ran_con_number+"';";
//			System.out.println(query_statement);
			Visit_SQLServer.execute_SQLServer(statement,query_statement);
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			System.out.println("Can't delete item with ran_con_number: '"+ran_con_number+"' from table ran_con. This item doesn't exist!");
//			s.printStackTrace();
			flag=false;
			return flag;
		}
		return flag;
	}
	
    public static void main(String[] args)
	{
		String aa="newa ma3n";
//		System.out.println(create_RCN(aa));
		String num="826";
		System.out.println(get_UserByRCN(num));

/*		if(have_Number(num)==true)
			System.out.println("Ok");
		else
			System.out.println("No");
*/		
/*		if(number_MatchUser(num,aa)==true)
			System.out.println("Good");
		else
			System.out.println("No good");
*/		
	    if(delete_ItemInRCL("826","newa ma3n")==true)
			System.out.println("delete ok");
		else
			System.out.println("delete fail");
		
	}

}
