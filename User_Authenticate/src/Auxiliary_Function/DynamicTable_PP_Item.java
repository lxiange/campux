package Auxiliary_Function;

public class DynamicTable_PP_Item
{
	private String user_name;
	private int wrong_psw_time;
	private String active_time;
	public DynamicTable_PP_Item()
	{
		user_name="";
		wrong_psw_time=0;
		active_time="";
	}
	public DynamicTable_PP_Item(String u,int w,String a)
	{
		user_name=u;
		wrong_psw_time=w;
		active_time=a;
	}
	public String toString(){return "["+user_name+",  "+wrong_psw_time+",  "+active_time+"]";};
	public String get_UserName(){return user_name;}
	public int get_WrongPswTime(){return wrong_psw_time;}
	public String get_ActiveTime(){return active_time;}
	public void set_UserName(String u){user_name=u;}
	public void set_WrongPswTime(int w){wrong_psw_time=w;}
	public void set_ActiveTime(String a){active_time=a;}
}