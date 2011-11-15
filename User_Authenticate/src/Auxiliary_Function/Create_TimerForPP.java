package Auxiliary_Function;
import java.util.*;

public class Create_TimerForPP 
{
    private final Timer timer=new Timer();
    public DynamicTable_PP table=new DynamicTable_PP();
	public Create_TimerForPP (DynamicTable_PP table)
	{
		this.table=table;
	}
	public void start()
	{
	    timer.schedule(new TimerTask()
	    {
	    	public void run()
	        {
	    		set_TimerForPP(table);
	    		if(table.get_Size()!=0)System.out.println(table.toString());
	  //  		else System.out.println("GOGO");
	        }
	    	private void set_TimerForPP(DynamicTable_PP table)
	    	{
	    		for(int i=0;i<table.get_Size();i++)
			    {
			    	String old_active_time=table.get_ItemInPP(i).get_ActiveTime();
			    	String new_active_time=Integer.toString((Integer.valueOf(old_active_time).intValue())-10000); 
			    	table.get_ItemInPP(i).set_ActiveTime(new_active_time);
			    	if(Integer.valueOf(new_active_time).intValue()<=0)
			    	{
			    		Connect_DBTable_UP.set_Status(table.get_ItemInPP(i).get_UserName(), "0");
			    		table.delete_ItemFromPP(table.get_ItemInPP(i).get_UserName());
			    	}
			    }
	    	}
	    }
	    ,10000,10000);
	    
	}

}
