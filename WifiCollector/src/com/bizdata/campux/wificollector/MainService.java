/**
 * 
 */
package com.bizdata.campux.wificollector;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * @Program Name : MyCampus.com.bizdata.mycampus.MainService.java
 * @Written by : Chenwei Liu
 * @Creation Date : 2011-11-4
 * @version : v1.00
 * @Company : Nanjing Basted Info-Tech Co., Ltd.
 * @Description : 
 * 
 * 
 * 
 * @ModificationHistory 
 * Who When What 
 * -------- ---------- ------------------------------------------------ 
 * Chenwei Liu 2011-11-4 TODO
 * 
 * 
 **/
public class MainService extends Service {
	private static List<ScanResult> wifiList;
	private WifiReceiver receiverWifi;
	
	private static MainService _single = null;
	private WifiManager _wifiManager = null;
	private WifiInfo _wifiInfo = null;
	private static String _ip = "IP_address";	
	
	private static boolean _wifi_scan_permission = false;
	public static MainService getInstance()
	{
		if (_single != null)
			return _single;
		return null;
	}
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private Handler _handler = new Handler();
	private Runnable _refresh = new Runnable() 
	{
		@Override  
            public void run() {
				getWIFI();
				_handler.postDelayed(_refresh,1000);
            }
    };
	@Override
	public void onCreate() {
		wifiList=new ArrayList<ScanResult>();
		_handler.postDelayed(_refresh,1000);
		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}
	@Override
	public void onDestroy(){
		unregisterReceiver(receiverWifi);
		_handler.removeCallbacks(_refresh);
		super.onDestroy();
	}
	private void getWIFI()
	{
		if (_wifiManager == null)
		{
			_wifiManager = (WifiManager)getSystemService(this.WIFI_SERVICE);
			
		}
		if (!_wifiManager.isWifiEnabled())
		{
			//_wifiManager.setWifiEnabled(true);
			MainService.set_ip("ÇëÏÈ´ò¿ªWIFI");
			return;
		}
		
		if (MainService.is_wifi_scan_permission())
		{
			try
			{
				_wifiManager.startScan();
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
		}
		//if (_wifiInfo == null ||_wifiInfo.getIpAddress()==0)
		//{
		_wifiInfo = _wifiManager.getConnectionInfo();
		//}
		if(_wifiInfo!=null 
				&& !intToIp(_wifiInfo.getIpAddress()).equals(MainService.get_ip()) 
				&& _wifiInfo.getIpAddress()!=0)
		{
			MainService.set_ip(intToIp(_wifiInfo.getIpAddress()));
			MainService.set_bssid(_wifiInfo.getBSSID());
		}
		
	}
	static String _bssid = "";
	public static void set_bssid(String bssid) {
		if( bssid!=null)
			_bssid = bssid;
	}
	public static String get_bssid(){
		return _bssid;
	}
	public static String intToIp(int i)
	{ 
		return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF); 
	}

	public static void set_ip(String _ip) {
		MainService._ip = _ip;
	}
	public static String get_ip() {
		return _ip;
	}
	public static WifiInfo getConnectionInfo(){
		if(_single==null)
			return null;
		_single._wifiInfo = _single._wifiManager.getConnectionInfo();
		return _single._wifiInfo;
	}
	public static void setWifiList(List<ScanResult> wifiList) {
		MainService.wifiList = wifiList;
	}
	public static List<ScanResult> getWifiList() {
		return wifiList;
	}
	public static void set_wifi_scan_permission(boolean _wifi_scan_permission) {
		MainService._wifi_scan_permission = _wifi_scan_permission;
	}
	public static boolean is_wifi_scan_permission() {
		return _wifi_scan_permission;
	}
	class WifiReceiver extends BroadcastReceiver {
		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
             setWifiList(_wifiManager.getScanResults());
           
           //mainText.setText(sb);
		}
   }

}
