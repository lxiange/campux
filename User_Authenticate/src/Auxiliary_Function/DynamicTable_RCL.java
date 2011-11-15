/* 
 * 类DynamicTable_RCL中的函数：
 * 函数get_Size用于获取表ran_con的大小
 * 函数insert_RCL用于将(ran_con_number,user_name,active_time)存入表ran_con中
 * 函数have_RCN用于检查表中是否含有ran_con_number项
 * 函数have_User用于检查表中是否含有ran_con_number项
 * 函数get_UserByRCN用于根据ran_con_number获取user_name
 * 函数delete_ItemFromRCL用于将ron_con_num等于num的表项删除
 * 函数get_ItemInRCL用于获取表中的第i个元素
 */

package Auxiliary_Function;

import java.util.ArrayList;

public class DynamicTable_RCL
{
	public ArrayList<DynamicTable_RCL_Item> table=new ArrayList<DynamicTable_RCL_Item>();
	public DynamicTable_RCL(){}
	
	//获取表的大小
	public int get_Size(){return table.size();}
	//判断表中是否含有ran_con_num为num的项
	public boolean have_RCN(String num)
	{
		boolean flag=false;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_RanConNum().equals(num))
			{
				flag=true;
				return flag;
			}
		}
		return flag;
	}
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
	//判断表中是否含有(ran_con_num,user_name)项
	public boolean have_Item(String ran_con_num, String user_name)
	{
		boolean flag=false;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_RanConNum().equals(ran_con_num))
			{
				if(table.get(i).get_UserName().equals(user_name))
				{
					flag=true;
					return flag;
				}
				else
			    return flag;
			}
		}
		return flag;
	}
	
	//根据ran_con_num获取user_name，若没找到则返回空串
	public String get_UserByRCN(String num)
	{
		String user="";
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_RanConNum().equals(num))
			{
				user=table.get(i).get_UserName();
				return user;
			}
		}
		return user;
		
	}
	//根据user_name获取ran_con_num，若没找到则返回空串
	public String get_RCNByUSer(String user)
	{
		String num="";
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_UserName().equals(user))
			{
				num=table.get(i).get_RanConNum();
				return num;
			}
		}
		return num;
		
	}
	//函数insert_RCL用于将表项item存入表ran_con中
	public boolean insert_RCL(DynamicTable_RCL_Item item)
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
	//函数delete_ItemFromRCL用于将ron_con_num等于num的表项从表ran_con中删除
	public boolean delete_ItemFromRCL(String num)
	{
		boolean flag=false;
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).get_RanConNum().equals(num))
			{
				table.remove(i);
				flag=true;
				return flag;
			}
		}		
		return flag;
	}

	//函数get_ItemInRCL用于获取表中的第i个元素
	public DynamicTable_RCL_Item get_ItemInRCL(int i)
	{
		DynamicTable_RCL_Item item=new DynamicTable_RCL_Item();
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

