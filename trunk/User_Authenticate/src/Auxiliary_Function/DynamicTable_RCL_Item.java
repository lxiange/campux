package Auxiliary_Function;

public class DynamicTable_RCL_Item
{
	private String ran_con_num;
	private String user_name;
	private String active_time;
	public DynamicTable_RCL_Item()
	{
		ran_con_num="";
		user_name="";
		active_time="";
	}
	public DynamicTable_RCL_Item(String r,String u,String a)
	{
		ran_con_num=r;
		user_name=u;
		active_time=a;
	}
	public String toString(){return "["+ran_con_num+",  "+user_name+",  "+active_time+"]";};
	public String get_RanConNum(){return ran_con_num;}
	public String get_UserName(){return user_name;}
	public String get_ActiveTime(){return active_time;}
	public void set_RanConNum(String r){ran_con_num=r;}
	public void set_UserName(String u){user_name=u;}
	public void set_ActiveTime(String a){active_time=a;}
}