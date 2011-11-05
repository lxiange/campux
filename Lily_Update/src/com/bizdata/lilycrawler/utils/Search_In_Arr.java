/*
 * 在ArrayList<String>中寻找某个元素。找到则返回其位置，找不到则返回-1
 * 
 * 
 * 
 */

package com.bizdata.lilycrawler.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Search_In_Arr {
	public static int search_In_Arr(String element, ArrayList<String> obj_arr) {
		int location = -1;
		for (int i = 0; i < obj_arr.size(); i++) {
			if (element.equals(obj_arr.get(i))) {
				location = i;
				break;
			}
		}
		return location;
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException, InterruptedException {
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		int new_page = 20;
		// ArrayList<String> url_arr="";
		String old_url = "http://bbs.nju.edu.cn/bbscon?board=Mediastudy&file=M.1190777615.A";
		// int location=search_In_Arr(old_url,url_arr);

		// System.out.println(location);
	}

}
