/*
 * 获取某subforum所有的frame_page的url
 * 
 * 
 */

package com.bizdata.lilycrawler.network;

import java.io.IOException;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Get_SourceFile;

public class Lily_GetAllFramePageUrl {
	public static ArrayList<String> lily_GetAllFramePage(String url)
			throws IOException, InterruptedException {
		ArrayList<String> all_page = new ArrayList<String>();
		// 得到第一页
		all_page.add(url);
		String source = Get_SourceFile.get_SourceFile(url);
		// System.out.println("<<<<"+source+">>>>");
		// 网络出问题时，需要重新读取源文件
		while (true) {
			if (source.length() < 700)// source.contains("错误的讨论区")||
			{
				System.out
						.println("Network connect again, please wait for 5s!");
				Thread.sleep(5000);
				source = Get_SourceFile.get_SourceFile(url);
			} else
				break;
		}
		// 获取含第二页url的String
		String rough_second_page = lily_Get_SecondRoughMainPage(source);
		// System.out.println(rough_second_page);
		// 只有一页的情况
		if (rough_second_page.equals("***"))
			return all_page;

		String url_prefix = url + "&start=";
		String url_postfix = "&type=doc";
		// 根据第二页url摘取总页数信息
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);
		// 获得后续所有frame_page的url
		ArrayList<String> remain_page = lily_RemainPage_Analyzer(total_num);

		for (int i = 0; i < remain_page.size(); i++)
			all_page.add(url_prefix + remain_page.get(i) + url_postfix);

		return all_page;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// String
		// url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy&start=265&type=doc";
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=D_Computer";
		ArrayList<String> all_page = lily_GetAllFramePage(url);
		for (int i = 0; i < all_page.size(); i++)
			System.out.println(all_page.get(i));
	}

	// 获取含第二页url的String
	public static String lily_Get_SecondRoughMainPage(String source) {
		String result = "***";
		String temp_str_a = Fragment_Searcher.fragment_Searcher(source,
				"<nobr>", "</nobr>");
		ArrayList<String> temp_arr = Fragment_Searcher.fragment_Searcher_All(
				temp_str_a, "<a href=", "</a>");
		for (int i = 0; i < temp_arr.size(); i++)
			if (temp_arr.get(i).contains("上一页")) {
				result = temp_arr.get(i);
				break;
			}
		return result;
	}

	// 根据第二页frame_page的url摘取总页数信息
	public static int lily_SecondMainPage_Analyzer(String url) {
		int page_num = 0;
		String temp_str = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
				url, "&start=", "&type=");

		page_num = Integer.valueOf(temp_str);
		return page_num;
	}

	// 获得后续所有frame_page的url
	public static ArrayList<String> lily_RemainPage_Analyzer(int total_num) {
		ArrayList<String> temp_arr = new ArrayList<String>();

		int residue = total_num % 20;
		int other = total_num / 20;
		for (int i = 0; i <= other; i++) {
			temp_arr.add((total_num - 20 * i) + "");
		}
		if (residue != 0) {
			temp_arr.add(0 + "");
			// 最后一个目录页的帖子重复问题，留待以后处理。
		}

		return temp_arr;
	}

	// 判断最后一页是否需要特殊处理，即是否含有20个theme
	public static int lily_Judge_LastPage(String url) throws IOException {
		String source = Get_SourceFile.get_SourceFile(url);
		String rough_second_page = lily_Get_SecondRoughMainPage(source);
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);
		int residue = (total_num + 20) % 20;
		return residue;
	}

}