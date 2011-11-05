/*
 * 
 * 获取某个frame_page下的所有主题的url(如果不含'合集'，总是为20个)
 * 各主题url是按照其在目录页中的倒序存储的
 * 
 */

package com.bizdata.lilycrawler.network;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Get_SourceFile;

public class Lily_GetThemePageUrl_OfOneFramePage {
	public static ArrayList<String> lily_GetThemePageUrl_OfOneFramePage(
			String frame_url) throws IOException, InterruptedException {
		ArrayList<String> all_theme_page = lily_Get_TitlesOfOneMainPage(frame_url);

		// 进行第一次过滤
		int not_good_page_count = 0;
		for (int i = 0; i < all_theme_page.size(); i++) {
			String temp_str = Fragment_Searcher
					.fragment_Searcher_WithoutBoundary(all_theme_page.get(i),
							"", ">");
			if (!pretreatment_1(all_theme_page.get(i), temp_str))
				all_theme_page.set(i, temp_str);
			else {
				all_theme_page.remove(i);
				i--;
				not_good_page_count++;
			}
		}

		/*
		 * String file_name="lily_result/first_Filted.txt"; FileOutputStream
		 * out=new FileOutputStream(file_name,true); byte[]
		 * buf=("Filted pages num: "
		 * +not_good_page_count+"\r\n"+"Total pages num: "
		 * +(not_good_page_count+all_theme_page.size())+"\r\n").getBytes();
		 * out.write(buf); out.flush(); out.close();
		 */
		for (int i = 0; i < all_theme_page.size(); i++) {
			String str = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					all_theme_page.get(i), "", "&num=");
			all_theme_page.set(i, str);
		}
		return all_theme_page;
	}

	// 第一次过滤
	public static boolean pretreatment_1(String source, String url)
			throws FileNotFoundException, IOException {
		boolean flag = false;
		// String str_a="[公告]";
		String str_b = "[合集]";
		// String str_c="【通知】";

		// if(source.contains(str_a)||source.contains(str_b)||source.contains(str_c))
		if (source.contains(str_b)) {
			flag = true;
			String file_name = "lily_result/first_Filted.txt";
			FileOutputStream out = new FileOutputStream(file_name, true);
			byte[] buf = (source + "\r\n").getBytes();
			out.write(buf);
			out.flush();
			out.close();
		}

		return flag;
	}

	// 获取某个frame_page页中的含有theme_page的url的String
	public static ArrayList<String> lily_Get_TitlesOfOneMainPage(String url)
			throws IOException {
		String source = Get_SourceFile.get_SourceFile(url);
		String prefix = "http://bbs.nju.edu.cn/";
		ArrayList<String> all_theme_OfOnePage = new ArrayList<String>();

		ArrayList<String> temp_arr = Fragment_Searcher.fragment_Searcher_All(
				source, "<a href=bbscon?", "</a>");

		for (int i = (temp_arr.size() - 1); i >= 0; i--) {
			String temp_str = Fragment_Searcher
					.fragment_Searcher_WithoutBoundary(temp_arr.get(i),
							"<a href=", " </a>");
			all_theme_OfOnePage.add(prefix + temp_str);
		}
		return all_theme_OfOnePage;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		// String
		// url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy&start=265&type=doc";
		ArrayList<String> all_theme_page = lily_GetThemePageUrl_OfOneFramePage(url);
		System.out.println(all_theme_page.size());
		for (int i = 0; i < all_theme_page.size(); i++)
			System.out.println(all_theme_page.get(i));
	}

}
