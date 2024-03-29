/*
 * 获取某subforum从某一页开始之后的new_page页的frame_page的url
 * 
 * 
 */

package com.bizdata.lilycrawler.network;

import java.io.IOException;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Get_SourceFile;

public class Lily_GetFramePageUrl_FromSomePage {
	public static ArrayList<String> lily_GetFramePageUrl_FromSomePage(
			String url, int new_page) throws IOException, InterruptedException {
		ArrayList<String> all_page = new ArrayList<String>();
		// 得到第一页
		all_page.add(url);
		String source = Get_SourceFile.get_SourceFile(url);

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
		String rough_second_page = lily_Get_SecondRoughMainPage(source);

		if (rough_second_page.equals(""))
			return all_page;

		// 得到后续所有页
		String prefix = "&start=";
		String url_prefix = "";
		// url不是subforum首页的情况
		if (url.contains(prefix))
			url_prefix = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					all_page.get(0), "", prefix) + prefix;
		// url是subforum首页的情况
		else
			url_prefix = url + prefix;
		String url_postfix = "&type=doc";
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);

		ArrayList<String> remain_page = lily_RemainPage_Analyzer(total_num);

		// 暂定所取得的前new_page页目录页中有已爬过的主题页
		for (int i = 0; i < new_page - 1 && i < remain_page.size(); i++)
			all_page.add(url_prefix + remain_page.get(i) + url_postfix);

		return all_page;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy&start=265&type=doc";
		// String url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		int new_page = 0;
		ArrayList<String> all_page = lily_GetFramePageUrl_FromSomePage(url,
				new_page);
		for (int i = 0; i < all_page.size(); i++)
			System.out.println(all_page.get(i));
	}

	public static String lily_Get_SecondRoughMainPage(String source) {
		String result = "";
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

	public static int lily_SecondMainPage_Analyzer(String url) {
		int page_num = 0;
		// System.out.println(url);
		String temp_str = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
				url, "&start=", "&type=");

		page_num = Integer.valueOf(temp_str).intValue();
		return page_num;
	}

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

	// 判断最后一页是否需要特殊处理
	public static int lily_Judge_LastPage(String url) throws IOException {
		String source = Get_SourceFile.get_SourceFile(url);
		String rough_second_page = lily_Get_SecondRoughMainPage(source);
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);
		int residue = (total_num + 20) % 20;
		return residue;
	}

}
