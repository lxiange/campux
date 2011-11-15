/* 
 * 类DynamicTable_PP中的函数：
 * 函数get_Size用于获取表的大小
 * 函数insert_PP用于将(user_name,wrong_psw_time,active_time)存入表中
 * 函数have_User用于检查表中是否含有user_name项
 * 函数get_WrongPswTime用于根据user_name获取wrong_psw_time
 * 函数get_ActiveTime用于根据user_name获取active_time
 * 函数delete_ItemFromPP用于将user_name等于user_name的表项删除
 * 函数get_ItemInPP用于获取表中的第i个元素
 */

package Auxiliary_Function;

import java.util.ArrayList;

public class DynamicTable_PP
{
	public ArrayList<DynamicTable_PP_Item> table=new ArrayList<DynamicTable_PP_Item>();
	public DynamicTable_PP(){}
	
	//获取表的大小
	public int get_Size(){return table.size();}
	
	//判断表中是否含有user_name为user_name的项
	public boolean have_User(String user_name)
	{
		boolean flag=false;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user_name))
			{
				flag=true;
				return flag;
			}
		}
		return flag;
	}
	
	//根据user_name获取含user_name的项，若没找到则返回空串
	public int get_WrongPswTime(String user_name)
	{
		int wrong_psw_time=0;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user_name))
			{
				wrong_psw_time=table.get(i).get_WrongPswTime();
				return wrong_psw_time;
			}
		}
		return wrong_psw_time;
		
	}
	
	//根据user_name获取wrong_psw_time，若没找到则返回空串
	public DynamicTable_PP_Item get_ItemByUser(String user_name)
	{
		DynamicTable_PP_Item item=new DynamicTable_PP_Item();
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user_name))
			{
				item=table.get(i);
				return item;
			}
		}
		return item;
		
	}
	
	//函数get_ActiveTime用于根据user_name获取active_time
	public String get_ActiveTime(String user_name)
	{
		String active_time="";
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user_name))
			{
				active_time=table.get(i).get_ActiveTime();
				return active_time;
			}
		}
		return active_time;
		
	}
	 // 函数insert_PP用于将item存入表中
	public boolean insert_PP(DynamicTable_PP_Item item)
	{
		boolean flag=true;
		if(table.contains(item)==true)
		{
			flag=false;
			return flag;
		}
		table.add(item);
		return flag;
	}
	//函数delete_ItemFromPP用于将user_name等于user_name的表项删除
	public boolean delete_ItemFromPP(String user_name)
	{
		boolean flag=false;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user_name))
			{
				table.remove(i);
				flag=true;
				return flag;
			}
		}		
		return flag;
	}

	//函数get_ItemInPP用于获取表中的第i个元素
	public DynamicTable_PP_Item get_ItemInPP(int i)
	{
		DynamicTable_PP_Item item=new DynamicTable_PP_Item();
		if(table.size()<=i)
			return item;
		else
		{
			item=table.get(i);
			return item;
		}
	}
	
    public String toString()
    {
    	String str="";
    	for(int i=0;i<this.get_Size();i++)
    	{
    		str+=table.get(i).toString()+"\r\n";
    	}
    	return str;
    }
}

