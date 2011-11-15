/*
 * 访问内存中动态中动态生成的ran_con表：
 * 函数create__DynamicTable_RCL用于产生一个空表ran_con
 * 函数create_RCN用于根据user_name产生一个独一无二的ran_con_number。
 * 函数create_Item用于根据(ran_con_number,user_name,active_time)产生一个表项。
 * 
 * 
 */
package Auxiliary_Function;
import java.util.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import General_Function.Visit_SQLServer;

public class Connect_DynamicTable_RCL 
{
	//函数create__DynamicTable_RCL用于产生一个空表ran_con
	public static DynamicTable_RCL create__DynamicTable_RCL()
	{
		DynamicTable_RCL table=new DynamicTable_RCL(); 
		return table;
	}
	//函数create_RCN用于根据user_name产生一个独一无二的ran_con_number。此处将用hash函数修改
	public static String create_RCN(String user_name)
	{
		String ran_con_number="";
		char[] arr=user_name.toCharArray();
		int total=0;
		for(int i=0;i<arr.length;i++)
			total+=(int)arr[i];
		
		ran_con_number=Integer.toString(total);
		return ran_con_number;
	}
	
	//函数create_Item用于根据(ran_con_number,user_name,active_time)产生一个表项。
	public static DynamicTable_RCL_Item create_Item(String r,String u,String a)
	{
		DynamicTable_RCL_Item new_item=new DynamicTable_RCL_Item(r,u,a);
		return new_item;
	}

	
	
}

