/**
 * 
 */
package com.bizdata.campux.wificollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizdata.mycampus.R;
import com.bizdata.mycampus.R.id;
import com.bizdata.mycampus.R.layout;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @Program Name : MyCampus.com.bizdata.mycampus.ShowActivity.java
 * @Written by : Chenwei Liu
 * @Creation Date : 2011-11-21
 * @version : v1.00
 * @Company : Nanjing Bizdata Info-Tech Co., Ltd.
 * @Description : 
 * 
 * 
 * 
 * @ModificationHistory 
 * Who When What 
 * -------- ---------- ------------------------------------------------ 
 * Chenwei Liu 2011-11-21 TODO
 * 
 * 
 **/
public class ShowActivity extends ListActivity {

	private static List<String> _wifi_list;
	private static List<Map<String, String>> _result_list;
	private ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState){
	     super.onCreate(savedInstanceState);
	     listView = new ListView(this);
	     Bundle extras = getIntent().getExtras();
	     _wifi_list = extras.getStringArrayList("WIFI_LIST");
	     _result_list = new ArrayList<Map<String, String>>();
	     //BSSID,capabilities,frequency,level,SSID
	     for (int i=0;i<_wifi_list.size();i++)
	     {
	    	 String[] temp = _wifi_list.get(i).split(",");
	    	 //首先取得地址和IP
	    	 String tempR = temp[0] + ","+temp[1] + "\n";
	    	 for (int j=2;j<temp.length-3;j=j+3)
	    	 {
	    		 tempR = tempR + temp[j]+","+temp[j+1]+","+temp[j+2]+"\n";
	    	 }
	    	 Map<String, String> map = new HashMap<String, String>();
	    	 map.put("info", tempR);
	    	 _result_list.add(map);
	     }
	     SimpleAdapter adapter = new SimpleAdapter(this,_result_list, R.layout.listitem,new String[]{"info"},new int[]{R.id.info});
	     //listView.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem,_result_list));
	     //setContentView(listView);
	     setListAdapter(adapter);

	}

}
