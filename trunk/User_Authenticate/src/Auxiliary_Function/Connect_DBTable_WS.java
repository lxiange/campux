/*
 * 访问DB中的wrong_set表
 * 
 */

package Auxiliary_Function;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;

public class Connect_DBTable_WS 
{
	//错误情况下对用户的响应。根据wrong_no查找wrong_reason,制作成xml,并转换成String.若找不到相应的项，则返回default项对应的wrong_reason
	public static String wrong_Response(String wrong_no)
	{
		String response="";
		
		String localhost_num="1433";
		String database_name="UA";
		String user_name="sa";
		String psw="123456";
		try
		{
			Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name,psw);
			String query_statement="select * from wrong_set where wrong_no='"+wrong_no+"';";
			ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
			result.next();
			String str=result.getString("wrong_reason").trim();
			response="<err c='"+wrong_no+"'>"+str+"</err>";
		}
		catch(IOException i){i.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace();}
		catch(SQLException s)
		{
			try
			{
	//			System.out.println("No such a item with no."+wrong_no+" in table wrong_set.");
				Statement statement = Visit_SQLServer.visit_SQLServer(localhost_num,database_name,user_name,psw);
				String query_statement="select * from wrong_set where wrong_no='default';";
				ResultSet result=Visit_SQLServer.execute_SQLServer(statement,query_statement);
				result.next();
				String str=result.getString("wrong_reason").trim();
				response="<err c='default'>"+str+"</err>";
			}
			catch(IOException i){i.printStackTrace();}
			catch(ClassNotFoundException c){c.printStackTrace();}
			catch(SQLException sa){}
		}
		return response;
	}
	
	public static void main(String[] args)
	{
		String wrong_no="we";
		String response=wrong_Response(wrong_no);
		System.out.println(response);
	}
	

}
