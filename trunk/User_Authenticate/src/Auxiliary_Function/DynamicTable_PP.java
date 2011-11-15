/* 
 * ��DynamicTable_PP�еĺ�����
 * ����get_Size���ڻ�ȡ��Ĵ�С
 * ����insert_PP���ڽ�(user_name,wrong_psw_time,active_time)�������
 * ����have_User���ڼ������Ƿ���user_name��
 * ����get_WrongPswTime���ڸ���user_name��ȡwrong_psw_time
 * ����get_ActiveTime���ڸ���user_name��ȡactive_time
 * ����delete_ItemFromPP���ڽ�user_name����user_name�ı���ɾ��
 * ����get_ItemInPP���ڻ�ȡ���еĵ�i��Ԫ��
 */

package Auxiliary_Function;

import java.util.ArrayList;

public class DynamicTable_PP
{
	public ArrayList<DynamicTable_PP_Item> table=new ArrayList<DynamicTable_PP_Item>();
	public DynamicTable_PP(){}
	
	//��ȡ��Ĵ�С
	public int get_Size(){return table.size();}
	
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
	
	//����user_name��ȡ��user_name�����û�ҵ��򷵻ؿմ�
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
	
	//����user_name��ȡwrong_psw_time����û�ҵ��򷵻ؿմ�
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
	
	//����get_ActiveTime���ڸ���user_name��ȡactive_time
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
	 // ����insert_PP���ڽ�item�������
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
	//����delete_ItemFromPP���ڽ�user_name����user_name�ı���ɾ��
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

	//����get_ItemInPP���ڻ�ȡ���еĵ�i��Ԫ��
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

