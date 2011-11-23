package com.bizdata.campux.wificollector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView _ip_TextView = null;
	private TextView _wifi_TextView = null;
	private EditText _location = null;
	
	private String _scan = "扫描";
	private String _end_scan = "关闭";
	private Button _wifi_scan = null;
	private Button _wifi_add = null;
	private Button _wifi_show = null;
	private Handler _handler = new Handler();
	
	private String pathName = "/sdcard/WIFI/";
	private String fileName = "wifi.txt";
	
	private boolean _file_create = false;
	//BSSID,capabilities,frequency,level,SSID
	private ArrayList<String> _wifi_list = null;
	private Runnable _refresh = new Runnable() 
	{
		@Override  
            public void run() {
			getWIFIScan();
			    _ip_TextView.setText(MainService.get_ip());
				_handler.postDelayed(_refresh,1000);
            }
    };
    private void getWIFIScan()
    {
    	String conBSSID = MainService.get_bssid();
    	String conIP = MainService.get_ip();
    	
    	if (MainService.getWifiList().size()>0)
    	{
    		String temp = "";
    		for (int i=0;i<MainService.getWifiList().size();i++)
    		{
    			 ScanResult tempS = MainService.getWifiList().get(i);
    			 temp = temp + (conBSSID.equals(tempS.BSSID) ? ">>" : "  ") + 
    			 tempS.SSID + "#"+tempS.BSSID + "," + String.valueOf(tempS.level)+ "\n";
    		}
    		_wifi_TextView.setText(temp);
    	}
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _wifi_list = new ArrayList<String>();
        //读取WIFI
        readWIFI();
        //创建文本文件
        createFile();
        _ip_TextView = (TextView)findViewById(R.id.textView1);
        _wifi_TextView = (TextView)findViewById(R.id.textView_wifi);
        _location = (EditText)findViewById(R.id.editText_location);
        _wifi_scan = (Button)findViewById(R.id.button_scan);
        _wifi_add = (Button)findViewById(R.id.button_add);
        _wifi_show = (Button)findViewById(R.id.button_show);
        
        
        if(MainService.getInstance() == null)
   	   	{
   	   		startService(new Intent(this, MainService.class));
   	   	}
        _ip_TextView.setText(MainService.get_ip());
        _handler.postDelayed(_refresh,1000);
        
        _wifi_scan.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				String tmp = String.valueOf(_wifi_scan.getText());
				if (tmp.equals("扫描"))
				{
					MainService.set_wifi_scan_permission(true);
					_wifi_scan.setText("关闭");
				}
				else if (tmp.equals("关闭"))
				{
					MainService.set_wifi_scan_permission(false);
					_wifi_scan.setText("扫描");
					_wifi_TextView.setText("扫描结果...");
					List<ScanResult> empty = new ArrayList<ScanResult>();
					empty.clear();
					MainService.setWifiList(empty);
				}
			}      	
        });
        _wifi_add.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(is_file_create()&&MainService.getWifiList().size()>0)
				{
					if (_location.getText().length()<=0)
					{
						Toast.makeText(MainActivity.this, "请输入地点", Toast.LENGTH_SHORT).show();
						return;
					}
					writeWIFI(MainService.getWifiList());
					Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					//_location.setText("");
				}
				else
				{
					Toast.makeText(MainActivity.this, "未获得热点信息", Toast.LENGTH_SHORT).show();
					return;
				}
			}      	
        });
        _wifi_show.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(is_file_create())
				{
					Intent intent = new Intent(MainActivity.this,ShowActivity.class);
					intent.putStringArrayListExtra("WIFI_LIST", _wifi_list);
					startActivity(intent);
				}
			}      	
        });
        
    }
    /** 
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true 
	 *  
	 * @param fileName 
	 * @param content 
	 */  
	public static void writeMethod(String file, String conent) {  
	      BufferedWriter out = null;  
	       try {  
	          out = new BufferedWriter(new OutputStreamWriter(  
	                  new FileOutputStream(file, true)));  
	          out.write(conent);  
	      } catch (Exception e) {  
	          e.printStackTrace();  
	     } finally {  
	         try {  
	             out.close();  
	         } catch (IOException e) {  
	             e.printStackTrace();  
	          }  
	       }  
	   }  
	private  void createFile(){
		try {
			File path = new File(pathName);
			File file = new File(pathName + fileName);

			if( !path.exists()) {

				Log.d("WIFIFile", "Create the path:" + pathName);

				path.mkdir();
			}

			if( !file.exists()) {

				Log.d("WIFIFile", "Create the file:" + fileName);

				file.createNewFile();

				set_file_create(true);
				
			}
			else
			{
				set_file_create(true);
			}
	    } catch(Exception e) {

		    Log.e("WIFIFile", "Error on writeFilToSD.");

		    e.printStackTrace();
		    set_file_create(false);
		   

		}
	    
	}
	private void readWIFI()
	{
		File file = new File(pathName + fileName);
		if( !file.exists()) 
		{
			return;
		}
		InputStream _in = null;
		try {
			_in = new BufferedInputStream(new FileInputStream(file));
		} 
		catch (FileNotFoundException e3) {
		   // TODO Auto-generated catch block
		   e3.printStackTrace();
		}
		BufferedReader _br = null;
		try {
		   _br = new BufferedReader(new InputStreamReader(_in, "UTF8"));
		} 
		catch (UnsupportedEncodingException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		}
		String tmp;
		 
		try {
		   while((tmp=_br.readLine())!=null){
		    //在这对tmp操作
			_wifi_list.add(tmp);
		 }
		   _br.close();
		   _in.close();
		} 
		catch (IOException e) {
		   // TODO Auto-generated catch block
		 e.printStackTrace();
		}

	}
	/**
	 * 通过判断SSID来检测是否已添加
	 * @param conent
	 * @return
	 */
	private boolean checkExist(String conent){
		for (int i=0;i<_wifi_list.size();i++)
		{
			if (_wifi_list.get(i).contains(conent))
			{
				return true;
			}
		}
		return false;
	}
	private void writeWIFI(List<ScanResult> wifiList) {

		//_wifi_list.add(conent);
	    String sdStatus = Environment.getExternalStorageState();

	    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {

	    Log.d("TestFile", "SD card is not avaiable/writeable right now.");

	    return;

	    }

	    //首先获得地址和IP
	    String conBSSID = MainService.get_bssid();
    	String conIP = MainService.get_ip();
    	String conLevel = "";
    	
    	String content= _location.getText().toString().trim()+","+conIP;
    	
    	
	    for (int i=0;i<wifiList.size();i++)
	    {
	    	ScanResult tempS = wifiList.get(i);
	    	if( conBSSID.equals(tempS.BSSID) )
	    		conLevel = String.valueOf(tempS.level);
	    }
	    content = content + "," + conBSSID + "," + conLevel;
	    
	    for (int i=0;i<wifiList.size();i++)
	    {
	    	ScanResult tempS = wifiList.get(i);
	    	if( !conBSSID.equals(tempS.BSSID) )
	    	content = content+ "," + tempS.BSSID + "," 
	    	          + String.valueOf(tempS.level);
	    }
	    
	    _wifi_list.add(content);
    	try {
    		writeMethod(pathName + fileName,content+"\n");
    	} catch(Exception e) {
    		Log.e("TestFile", "Error on writeWIFI.");

    		e.printStackTrace();
    	}
	}
	public void set_file_create(boolean _file_create) {
		this._file_create = _file_create;
	}
	public boolean is_file_create() {
		return _file_create;
	}
	
	 @Override
	 public void onBackPressed() {
		 PreFunction.exitSys(this); 
		 return;
	 }

}