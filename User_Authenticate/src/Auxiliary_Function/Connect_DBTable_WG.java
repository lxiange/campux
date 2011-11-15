/*
 * ����DB�е�workgroup��
 * ����have_NoInWG���ڼ��workgroup�����Ƿ���group_no������򷵻�true�����򷵻�false��
 * ����have_NameInWG���ڼ��workgroup�����Ƿ���group_name������򷵻�true�����򷵻�false��
 * ����have_ItemInWG���ڼ��workgroup�����Ƿ���(group_no,group_name)������򷵻�true�����򷵻�false��
 * ����get_NameByNo���ڸ���group_no���group_name���쳣����·��ؿմ�
 * ����get_NoByName���ڸ���group_name���group_no���쳣����·��ؿմ�
 * ����get_AllGroupsName����ȡ��workgroup���ڵ�����group_name����ArrayList���ء�
 * ����get_AllGroupsNo����ȡ��workgroup���ڵ�����group_no����ArrayList���ء�
 * ����get_AutoNoForNewItem�����Զ�Ϊ����ӵ�group_name���ɺϷ���group_no��
 * ����append_ItemInWG���ڽ�(group_no,group_name)�����workgroup������ɹ�����true�����򷵻�false��
 * ����delete_ItemInWG���ڽ�(group_no,group_name)���workgroup����ɾ����ɾ���ɹ�����true�����򷵻�false��
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
	//����have_NoInWG���ڼ��workgroup�����Ƿ���group_no������򷵻�true�����򷵻�false��
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
	
	
	//����have_NameInWG���ڼ��workgroup�����Ƿ���group_name������򷵻�true�����򷵻�false��
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
	
	
	//����have_ItemInWG���ڼ��workgroup�����Ƿ���(group_no,group_name)������򷵻�true�����򷵻�false��
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
	 
	
	//����get_NameByNo���ڸ���group_no���group_name
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
	
	
    //����get_NoByName���ڸ���group_name���group_no
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
	
	//����get_AllGroupsName����ȡ��workgroup���ڵ�����group_name����ArrayList���ء�
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
	
	//����get_AllGroupsNo����ȡ��workgroup���ڵ�����group_no����ArrayList���ء�
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
	//����get_AutoNoForNewItem�����Զ�Ϊ����ӵ�group_name���ɺϷ���group_no��
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
		

	//����append_ItemInWG���ڽ�(group_no,group_name)�����workgroup������ɹ�����true�����򷵻�false��
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
	
	//����delete_ItemInWG���ڽ�(group_no,group_name)���workgroup����ɾ����ɾ���ɹ�����true�����򷵻�false��
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
