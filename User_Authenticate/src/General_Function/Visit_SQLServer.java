/*
 * 包含两个函数：
 * visit_SQLServer(String,String,String,String)用于建立程序与与SQLServer中某数据库的连接 
 * 返回Statement型对象
 * 
 * execute_SQLServer(Statement,String)用于向已建立的数据库执行查询语句
 * 返回ResultSet型对象
 * 
 */


package General_Function;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;

public class Visit_SQLServer 
{
	//precondition:SQLServer数据库中已经建立好相应的数据库database_name
	//postcondition:建立起程序与SQLServer中数据库database_name的连接
	public static Statement visit_SQLServer(String localhost_num, String database_name, String user_name, String psw)throws IOException,ClassNotFoundException,SQLException
	{
		 Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		 String temp_str="jdbc:sqlserver://localhost:"+localhost_num+";Databasename="+database_name;
		 Connection connection = DriverManager.getConnection(temp_str,user_name,psw);
		 Statement statement = connection.createStatement();
		 return statement;
				
	}
	
	//precondition:已经建立起到SQLServer的连接
	//postcondition:执行查询语句query_statement
	public static ResultSet execute_SQLServer(Statement statement,String query_statement)throws IOException,ClassNotFoundException,SQLException
	{
		statement.execute(query_statement);
		ResultSet result=statement.getResultSet();
		return result;
	}
		
	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException
	{
		String localhost_num="1433";
		String database_name="lily";
		String user_name="sa";
		String psw="123456";		
		Statement statement = visit_SQLServer(localhost_num,database_name,user_name,psw);		
		String query_statement="select * from lily_date";
		
		ResultSet result=execute_SQLServer(statement,query_statement);
		String str="";
		while(result.next())
		{
			str=result.getString("theme_url");
			System.out.println(str);
		}		
		
//	     Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.1.5:1433;Databasename=lily","sa","shangshuBiz5_");
	}

}
