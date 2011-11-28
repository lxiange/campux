/*
 * ��ȡĳsubforum��ĳһҳ��ʼ֮���new_pageҳ��frame_page��url
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
		// �õ���һҳ
		all_page.add(url);
		String source = Get_SourceFile.get_SourceFile(url);

		while (true) {
			if (source.length() < 700)// source.contains("�����������")||
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

		// �õ���������ҳ
		String prefix = "&start=";
		String url_prefix = "";
		// url����subforum��ҳ�����
		if (url.contains(prefix))
			url_prefix = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					all_page.get(0), "", prefix) + prefix;
		// url��subforum��ҳ�����
		else
			url_prefix = url + prefix;
		String url_postfix = "&type=doc";
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);

		ArrayList<String> remain_page = lily_RemainPage_Analyzer(total_num);

		// �ݶ���ȡ�õ�ǰnew_pageҳĿ¼ҳ����������������ҳ
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
			if (temp_arr.get(i).contains("��һҳ")) {
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
			// ���һ��Ŀ¼ҳ�������ظ����⣬�����Ժ�����
		}

		return temp_arr;
	}

	// �ж����һҳ�Ƿ���Ҫ���⴦��
	public static int lily_Judge_LastPage(String url) throws IOException {
		String source = Get_SourceFile.get_SourceFile(url);
		String rough_second_page = lily_Get_SecondRoughMainPage(source);
		int total_num = lily_SecondMainPage_Analyzer(rough_second_page);
		int residue = (total_num + 20) % 20;
		return residue;
	}

}