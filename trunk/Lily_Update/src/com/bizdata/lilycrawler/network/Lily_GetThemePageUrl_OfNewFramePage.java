/*
 * 
 * ��ȡĳ��subforum������������ҳ��url
 * 
 * 
 */

package com.bizdata.lilycrawler.network;

import java.io.IOException;
import java.util.ArrayList;

public class Lily_GetThemePageUrl_OfNewFramePage {
	public static ArrayList<String> lily_GetThemePageUrl_OfNewFramePage(
			String first_frame_page, int new_page, int page_increment)
			throws IOException, InterruptedException {
		ArrayList<String> all_theme_page = new ArrayList<String>();
		String aa = "&start=";
		String domain = first_frame_page.substring(first_frame_page
				.indexOf("board=") + 6);
		if (first_frame_page.contains(aa))
			domain = first_frame_page.substring(
					first_frame_page.indexOf("board=") + 6,
					first_frame_page.indexOf("&start="));

		// ��ȡnew_pageҳĿ¼ҳ��url
		ArrayList<String> all_frame_page = Lily_GetFramePageUrl_FromSomePage
				.lily_GetFramePageUrl_FromSomePage(first_frame_page, new_page);
		// �۲����һҳ�Ƿ���20������
		int residue = Lily_GetFramePageUrl_FromSomePage
				.lily_Judge_LastPage(first_frame_page);

		// subforumֻ��һҳ�����(old_newest_page�϶��ڴ�ҳ��)
		if (all_frame_page.size() == 1) {
			System.out.println("Now analyze the content page of " + domain
					+ " no." + "1");
			ArrayList<String> temp_arr = Lily_GetThemePageUrl_OfOneFramePage
					.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
							.get(all_frame_page.size() - 1));
			while (temp_arr.size() == 0) {
				System.out
						.println("Network connect again, please wait for 5s!");
				Thread.sleep(5000);
				temp_arr = Lily_GetThemePageUrl_OfOneFramePage
						.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
								.get(all_frame_page.size() - 1));
			}
			for (int k = 0; k < temp_arr.size(); k++)
				all_theme_page.add(temp_arr.get(k));
		}
		// subforum����ҳ��С��new_pageҳ�����һҳ��������20����������(�˴��������һҳʱ������)
		else if (all_frame_page.size() < new_page && residue != 0) {
			for (int i = 0; i < all_frame_page.size() - 1; i++) {
				System.out.println("Now analyze the content page of " + domain
						+ " no." + (i + 1));
				ArrayList<String> temp_arr = Lily_GetThemePageUrl_OfOneFramePage
						.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
								.get(i));
				while (temp_arr.size() == 0) {
					System.out
							.println("Network connect again, please wait for 5s!");
					Thread.sleep(5000);
					temp_arr = Lily_GetThemePageUrl_OfOneFramePage
							.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
									.get(i));
				}
				for (int k = 0; k < temp_arr.size(); k++)
					all_theme_page.add(temp_arr.get(k));
				Thread.sleep(400);
			}
			ArrayList<String> temp_arr = Lily_GetThemePageUrl_OfOneFramePage
					.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
							.get(all_frame_page.size() - 1));
			while (temp_arr.size() == 0) {
				System.out
						.println("Network connect again, please wait for 5s!");
				Thread.sleep(5000);
				temp_arr = Lily_GetThemePageUrl_OfOneFramePage
						.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
								.get(all_frame_page.size() - 1));
			}
			for (int k = 0; k < residue - 1; k++)
				all_theme_page.add(temp_arr.get(k));
		} else {
			for (int i = 0; i < all_frame_page.size(); i++) {
				System.out.println("Now analyze the content page of " + domain
						+ " no." + (i + 1));
				ArrayList<String> temp_arr = Lily_GetThemePageUrl_OfOneFramePage
						.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
								.get(i));
				while (temp_arr.size() == 0) {
					System.out
							.println("Network connect again, please wait for 5s!");
					Thread.sleep(5000);
					temp_arr = Lily_GetThemePageUrl_OfOneFramePage
							.lily_GetThemePageUrl_OfOneFramePage(all_frame_page
									.get(i));
				}
				for (int k = 0; k < temp_arr.size(); k++)
					all_theme_page.add(temp_arr.get(k));
				Thread.sleep(400);
			}
		}
		return all_theme_page;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// String url="http://bbs.nju.edu.cn/bbsdoc?board=Nirvana";
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		// String
		// url="http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy&start=265&type=doc";
		int new_page = 2;
		int page_increment = 3;
		ArrayList<String> all_theme_page = lily_GetThemePageUrl_OfNewFramePage(
				url, new_page, page_increment);
		System.out.println(all_theme_page.size());
		for (int i = 0; i < all_theme_page.size(); i++)
			System.out.println(all_theme_page.get(i));
	}

}