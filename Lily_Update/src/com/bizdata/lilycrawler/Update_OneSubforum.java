package com.bizdata.lilycrawler;

/*
 * ������1: ����һ��subforum
 * 
 * 
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bizdata.lilycrawler.config.Lily_FetchOldNewestUrl;
import com.bizdata.lilycrawler.config.Lily_RefreshNewestUrl;
import com.bizdata.lilycrawler.network.Lily_GetThemePageUrl_OfNewFramePage;
import com.bizdata.lilycrawler.pageanalysis.Lily_Get_ContentOfOneThemePage;
import com.bizdata.lilycrawler.pageanalysis.Lily_Get_ContentOfSomeThemePage_InLatestDate;
import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Search_In_Arr;

public class Update_OneSubforum {
	public static void update_OneSubforum(String subforum_name)
			throws IOException, ClassNotFoundException, SQLException,
			InterruptedException {
		// ȡ��lily_date���е�subforum_name��Ӧ��theme_url
		ArrayList<String> old_arr = Lily_FetchOldNewestUrl
				.lily_Fetch_Old_Newest_Url(subforum_name);
		String old_url = old_arr.get(1);
		// lily_date����û��subforum_name�����
		if (old_url.equals("NotFound")) {
			String temp_str = Fragment_Searcher.fragment_Eraser(subforum_name,
					"", "http://bbs.nju.edu.cn/bbsdoc?board=");
			System.out.println("There is no subforum with name: " + temp_str);
			return;
		}
		/*
		 * for(int i=0;i<old_arr.size();i++) System.out.println(old_arr.get(i));
		 */String old_date = old_arr.get(2);
		int new_page = Integer.valueOf(old_arr.get(3)).intValue();
		int page_increment = Integer.valueOf(old_arr.get(4)).intValue();

		// ȡ��new_page��Ŀ¼ҳ�к��е�����ҳ������url
		ArrayList<String> url_arr = Lily_GetThemePageUrl_OfNewFramePage
				.lily_GetThemePageUrl_OfNewFramePage(subforum_name, new_page,
						page_increment);
		int location = Search_In_Arr.search_In_Arr(old_url, url_arr);
		// new_page��Ŀ¼ҳ�е�����ҳ������old_url�����
		int time = 0;
		while (location < 0) {
			Thread.sleep(1000);
			new_page += page_increment;
			time++;
			// ����϶�ҳ���涼û��old_newest_url����������һҳ�Ѿ�����վɾ��������date�ȽϷ����ݶ�ֻ�洢new_page+5*page_incrementĿ¼ҳ�ں��е�����ҳ
			if (time >= 5) {
				Date begin_date = Lily_Get_ContentOfSomeThemePage_InLatestDate
						.date_Analyzer(old_date);

				Calendar new_calendar = Calendar.getInstance();
				new_calendar.set(2020, 1, 1, 0, 0, 0);
				Date end_date = new_calendar.getTime();
				Lily_Get_ContentOfSomeThemePage_InLatestDate
						.lily_Get_ContentOfSomeThemePage_InLatestDate(
								subforum_name, new_page, begin_date, end_date);
				break;
			}

			url_arr = Lily_GetThemePageUrl_OfNewFramePage
					.lily_GetThemePageUrl_OfNewFramePage(subforum_name,
							new_page, page_increment);
			location = Search_In_Arr.search_In_Arr(old_url, url_arr);
		}

		// ��ȡ�õ�������ҳ������������ݿ�
		for (int i = 0; i < location; i++) {
			Lily_Get_ContentOfOneThemePage.lily_Get_ContentOfOneThemePage(
					url_arr.get(i), i + 1);
			Thread.sleep(1000);
		}

		// �������ݿ��е�lily_date���ж�Ӧ�������
		if (location != 0) {
			Lily_RefreshNewestUrl.lily_Refresh_Newest_Url(subforum_name,
					url_arr.get(0));
		} else {
			String temp_str = Fragment_Searcher.fragment_Eraser(subforum_name,
					"", "http://bbs.nju.edu.cn/bbsdoc?board=");
			System.out.println("No new theme in subforum with name: "
					+ temp_str);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException, InterruptedException {
		// String subforum="http://bbs.nju.edu.cn/bbsdoc?board=D_Computer";
		String subforum = "http://bbs.nju.edu.cn/bbsdoc?board=AcademicReport";
		update_OneSubforum(subforum);

	}

}