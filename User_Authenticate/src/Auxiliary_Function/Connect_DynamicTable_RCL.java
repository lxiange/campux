/*
 * �����ڴ��ж�̬�ж�̬���ɵ�ran_con��
 * ����create__DynamicTable_RCL���ڲ���һ���ձ�ran_con
 * ����create_RCN���ڸ���user_name����һ����һ�޶���ran_con_number��
 * ����create_Item���ڸ���(ran_con_number,user_name,active_time)����һ�����
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
	//����create__DynamicTable_RCL���ڲ���һ���ձ�ran_con
	public static DynamicTable_RCL create__DynamicTable_RCL()
	{
		DynamicTable_RCL table=new DynamicTable_RCL(); 
		return table;
	}
	//����create_RCN���ڸ���user_name����һ����һ�޶���ran_con_number���˴�����hash�����޸�
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
	
	//����create_Item���ڸ���(ran_con_number,user_name,active_time)����һ�����
	public static DynamicTable_RCL_Item create_Item(String r,String u,String a)
	{
		DynamicTable_RCL_Item new_item=new DynamicTable_RCL_Item(r,u,a);
		return new_item;
	}

	
	
}

