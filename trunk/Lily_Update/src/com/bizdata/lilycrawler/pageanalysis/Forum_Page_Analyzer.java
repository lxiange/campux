/*
 * 作用(辅助函数)：获取所有subforum的name
 * 
 */

package com.bizdata.lilycrawler.pageanalysis;

import java.io.IOException;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Get_SourceFile;

public class Forum_Page_Analyzer {
	public static ArrayList<String> forum_Page_Analyzer(String url)
			throws IOException {
		ArrayList<String> str_arr = new ArrayList<String>();
		String source = Get_SourceFile.get_SourceFile(url);

		source = Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,
				"<table", "</table>");
		ArrayList<String> temp_arr = Fragment_Searcher.fragment_Searcher_All(
				source, "<a href", "</a>");

		String label = "href=bbsdoc?board=";
		for (int i = 0; i < temp_arr.size(); i++)
			if (!temp_arr.get(i).contains(label))
				temp_arr.remove(i);
		for (int i = 0; i < temp_arr.size(); i = i + 2)
			str_arr.add(temp_arr.get(i));
		for (int i = 0; i < str_arr.size(); i++) {
			String temp_str = Fragment_Searcher
					.fragment_Searcher_WithoutBoundary(str_arr.get(i),
							"<a href=bbsdoc?board=", ">");
			str_arr.set(i, temp_str);
		}
		return str_arr;

	}

	public static void main(String[] args) throws IOException {
		String url = "http://bbs.nju.edu.cn/bbsboa?sec=5";
		ArrayList<String> temp_arr = forum_Page_Analyzer(url);
		System.out.println(temp_arr.size());
		for (int i = 0; i < temp_arr.size(); i++) {
			System.out.print("\"" + temp_arr.get(i) + "\",");
			if ((i + 1) % 10 == 0)
				System.out.print("\r\n");
		}
	}

}
