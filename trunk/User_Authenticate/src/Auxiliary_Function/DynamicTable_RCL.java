/* 
 * ��DynamicTable_RCL�еĺ�����
 * ����get_Size���ڻ�ȡ��ran_con�Ĵ�С
 * ����insert_RCL���ڽ�(ran_con_number,user_name,active_time)�����ran_con��
 * ����have_RCN���ڼ������Ƿ���ran_con_number��
 * ����have_User���ڼ������Ƿ���ran_con_number��
 * ����get_UserByRCN���ڸ���ran_con_number��ȡuser_name
 * ����delete_ItemFromRCL���ڽ�ron_con_num����num�ı���ɾ��
 * ����get_ItemInRCL���ڻ�ȡ���еĵ�i��Ԫ��
 */

package Auxiliary_Function;

import java.util.ArrayList;

public class DynamicTable_RCL
{
	public ArrayList<DynamicTable_RCL_Item> table=new ArrayList<DynamicTable_RCL_Item>();
	public DynamicTable_RCL(){}
	
	//��ȡ��Ĵ�С
	public int get_Size(){return table.size();}
	//�жϱ����Ƿ���ran_con_numΪnum����
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
	//�жϱ����Ƿ���user_nameΪuser_name����
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
	//�жϱ����Ƿ���(ran_con_num,user_name)��
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
	
	//����ran_con_num��ȡuser_name����û�ҵ��򷵻ؿմ�
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
	//����user_name��ȡran_con_num����û�ҵ��򷵻ؿմ�
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
	//����insert_RCL���ڽ�����item�����ran_con��
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
	//����delete_ItemFromRCL���ڽ�ron_con_num����num�ı���ӱ�ran_con��ɾ��
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

	//����get_ItemInRCL���ڻ�ȡ���еĵ�i��Ԫ��
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

