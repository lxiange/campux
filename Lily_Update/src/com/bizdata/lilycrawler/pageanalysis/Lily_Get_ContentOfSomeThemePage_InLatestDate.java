/*
 * 根据subforum和Date型值begin_date、end_date，将subforum中时间处于begin_date和end_date之间的主题页存入数据库
 * 前提是确保数据库中本来没有这些项
 */

package com.bizdata.lilycrawler.pageanalysis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bizdata.lilycrawler.network.Lily_GetThemePageUrl_OfNewFramePage;

public class Lily_Get_ContentOfSomeThemePage_InLatestDate {
	public static void lily_Get_ContentOfSomeThemePage_InLatestDate(
			String subforum, int new_page, Date begin_date, Date end_date)
			throws IOException, ClassNotFoundException, SQLException,
			InterruptedException {
		ArrayList<String> all_theme_url = Lily_GetThemePageUrl_OfNewFramePage
				.lily_GetThemePageUrl_OfNewFramePage(subforum, new_page, 0);
		/*
		 * for(int i=0;i<all_theme_url.size();i++)
		 * System.out.println(all_theme_url.get(i));
		 * System.out.println(all_theme_url.size());
		 */if (begin_date.after(end_date)) {
			System.out.println("Wrong date!");
			return;
		}
		for (int i = 0; i < all_theme_url.size(); i++) {
			// 此处获得主题页时间可改进为在目录页获得（但是其不含有年份）
			ArrayList<String> temp_arr = Lily_Content_Analyzer
					.lily_Content_Analyzer(all_theme_url.get(i));
			// System.out.println(temp_arr.get(2));
			Thread.sleep(1000);
			Date theme_date = date_Analyzer(temp_arr.get(2));

			if (temp_arr.get(2) != "" && theme_date.before(end_date)
					&& theme_date.after(begin_date)) {
				Lily_Get_ContentOfOneThemePage.lily_Get_ContentOfOneThemePage(
						all_theme_url.get(i), i + 1);
				Thread.sleep(1000);
			} else
				break;
		}
	}

	public static Date date_Analyzer(String date_str) {
		String[] date_arr = date_str.split(" ");
		Calendar new_calendar = Calendar.getInstance();
		new_calendar.set(3000, 1, 1);
		Date new_date = new_calendar.getTime();
		if (date_arr.length != 5)
			return new_date;

		int year = Integer.valueOf(date_arr[4]).intValue();
		int month = Integer.valueOf(month_change(date_arr[1])).intValue();
		int day = Integer.valueOf(date_arr[2]).intValue();
		String[] hour_arr = date_arr[3].split(":");
		if (hour_arr.length != 3)
			return new_date;

		int hour = Integer.valueOf(hour_arr[0]).intValue();
		int minute = Integer.valueOf(hour_arr[1]).intValue();
		int second = Integer.valueOf(hour_arr[2]).intValue();
		new_calendar.set(year, month, day, hour, minute, second);
		new_date = new_calendar.getTime();
		return new_date;
	}

	public static String month_change(String month_str) {
		String temp_str = "";
		if (month_str.equals("Jen"))
			temp_str = "0";
		else if (month_str.equals("Feb"))
			temp_str = "1";
		else if (month_str.equals("Mar"))
			temp_str = "2";
		else if (month_str.equals("Apr"))
			temp_str = "3";
		else if (month_str.equals("May"))
			temp_str = "4";
		else if (month_str.equals("Jun"))
			temp_str = "5";
		else if (month_str.equals("Jul"))
			temp_str = "6";
		else if (month_str.equals("Aug"))
			temp_str = "7";
		else if (month_str.equals("Sep"))
			temp_str = "8";
		else if (month_str.equals("Oct"))
			temp_str = "9";
		else if (month_str.equals("Nov"))
			temp_str = "10";
		else if (month_str.equals("Dec"))
			temp_str = "11";

		return temp_str;
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException, InterruptedException {
		Calendar new_calendar = Calendar.getInstance();
		new_calendar.set(2010, 1, 1, 0, 0, 0);
		Date begin_date = new_calendar.getTime();
		new_calendar.set(2011, 10, 18, 0, 0, 0);
		Date end_date = new_calendar.getTime();
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		int new_page = 2;
		lily_Get_ContentOfSomeThemePage_InLatestDate(url, new_page, begin_date,
				end_date);
	}

}
