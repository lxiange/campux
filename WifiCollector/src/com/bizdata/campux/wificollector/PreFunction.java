package com.bizdata.campux.wificollector;
/**
 * @Program Name : HealthyCall.com.bizdata.information.PreFunction.java
 * @Written by : Chenwei Liu
 * @Creation Date : 2011-5-11
 * @version : v1.00
 * @Company : Nanjing Bizdata Info-Tech Co., Ltd.
 * @Description : 
 * 
 * 
 * 
 * @ModificationHistory 
 * Who When What 
 * -------- ---------- ------------------------------------------------ 
 * Chenwei Liu 2011-5-11 TODO
 * 
 * 
 **/
import com.bizdata.campux.wificollector.MainService;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

public class PreFunction {

	// Screen Size
	private static int ScreenSize = 1; /* 0 == Small, 1 == Normal, 2 == Large */
	
	public static int getSDKVersion()
	{
		return Integer.parseInt(Build.VERSION.SDK);
	}
	public static void detectScreen(Activity activity)
	{
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        int lanscapeHight = 0 ; 
        if( activity.getResources().getConfiguration().orientation ==
        								Configuration.ORIENTATION_PORTRAIT)
        	lanscapeHight = metrics.heightPixels;
        else
        	lanscapeHight = metrics.widthPixels;
        	        
        if(lanscapeHight >= 800)
        	ScreenSize = 2;
        else if(lanscapeHight <= 320)
        	ScreenSize = 0;
        else 
        	ScreenSize = 1;
	}
	
	public static int getScreenSize()
	{
		return ScreenSize;
	}
	
	public static void exitSys(Context context)
	{
		if(MainService.getInstance() != null)
		{
			MainService.getInstance().stopSelf();
		}
	
		killSelf(context);
	}
	private static Handler EndHelper = new Handler() 
    {
    	public void handleMessage(Message msg)
    	{
    		android.os.Process.killProcess(android.os.Process.myPid());
    	}
    	
    }; 
    
    private static void killSelf(Context context)
    {
    	
    	if(PreFunction.getSDKVersion() <= 7)
    	{
           	((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
           									.restartPackage("com.bizdata.mycampus");
    	}
    	else
    	{
    		EndHelper.sendEmptyMessageDelayed(0, 1500);

    	}
    }
}
