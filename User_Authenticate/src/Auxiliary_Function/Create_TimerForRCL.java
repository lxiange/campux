
package Auxiliary_Function;
import java.util.*;

public class Create_TimerForRCL 
{
	private final Timer timer=new Timer();
	
	public DynamicTable_RCL table=Connect_DynamicTable_RCL.create__DynamicTable_RCL();
	public Create_TimerForRCL(DynamicTable_RCL table)
	{
		this.table=table;
	}
	public void start()
	{
	    timer.schedule(new TimerTask()
	    {
	    	public void run()
	        {
	    		set_TimerForRCL(table);
/*	    		if(table.get_Size()!=0)System.out.println(table.toString());
	    		else System.out.println("GOGO");
*/	        }
	    	private void set_TimerForRCL(DynamicTable_RCL table)
	    	{
	    		for(int i=0;i<table.get_Size();i++)
			    {
			    	String old_active_time=table.get_ItemInRCL(i).get_ActiveTime();
			    	String new_active_time=Integer.toString((Integer.valueOf(old_active_time).intValue())-10000); 
			    	table.get_ItemInRCL(i).set_ActiveTime(new_active_time);
			    	if(Integer.valueOf(new_active_time).intValue()<=0)
			    		table.delete_ItemFromRCL(table.get_ItemInRCL(i).get_RanConNum());
			    }
	    	}
	    }
	    ,10000,10000);
	    
	}

	public static void main(String[] args)
	{
		DynamicTable_RCL ran_con=Connect_DynamicTable_RCL.create__DynamicTable_RCL();
		DynamicTable_RCL_Item item=new DynamicTable_RCL_Item("getch","bitch","600000");
		ran_con.insert_RCL(item);
		Create_TimerForRCL ran_con_timer=new Create_TimerForRCL(ran_con);
		ran_con_timer.start();
	}

}
