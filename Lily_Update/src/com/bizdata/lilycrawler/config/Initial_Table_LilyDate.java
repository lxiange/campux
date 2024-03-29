/*
 * 初始化数据库表'lily_date'
 */
package com.bizdata.lilycrawler.config;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bizdata.lilycrawler.network.Lily_GetThemePageUrl_OfOneFramePage;
import com.bizdata.lilycrawler.pageanalysis.Lily_Content_Analyzer;
import com.bizdata.lilycrawler.pageanalysis.Lily_Get_AllSubForumName;
import com.bizdata.lilycrawler.utils.Visit_SQLServer;

public class Initial_Table_LilyDate {
	public static void initial_Table_LilyDate(String classified_subforum)
			throws IOException, ClassNotFoundException, SQLException,
			InterruptedException {
		ArrayList<String> subforum = Lily_Get_AllSubForumName
				.lily_Get_SubForumName(classified_subforum);
		for (int i = 0; i < subforum.size(); i++) {
			System.out.println("Now analyze subforum: " + subforum.get(i));

			ArrayList<String> all_theme_page = Lily_GetThemePageUrl_OfOneFramePage
					.lily_GetThemePageUrl_OfOneFramePage(subforum.get(i));
			// 网络出问题时，重复读取
			while (all_theme_page.size() == 0) {
				System.out
						.println("Network connect again, please wait for 5s!");
				Thread.sleep(5000);
				all_theme_page = Lily_GetThemePageUrl_OfOneFramePage
						.lily_GetThemePageUrl_OfOneFramePage(subforum.get(i));
			}
			ArrayList<String> first_page_content = Lily_Content_Analyzer
					.lily_Content_Analyzer(all_theme_page.get(1));

			String theme_url = first_page_content.get(0);
			String theme_date = first_page_content.get(2);
			initial_One_Subforum(subforum.get(i), theme_url, theme_date, "2",
					"2");
			Thread.sleep(1000);
		}
	}

	// 初始化数据库表'lily_date'中的某subforum对应的一项
	public static void initial_One_Subforum(String subforum, String theme_url,
			String theme_date, String new_page_num, String new_page_increment)
			throws IOException, ClassNotFoundException, SQLException,
			InterruptedException {
		Statement statement = Visit_SQLServer.visit_SQLServer("1433", "lily",
				"sa", "123456");
		String query_statement = "insert into lily_date values ('" + subforum
				+ "','" + theme_url + "','" + theme_date + "','" + new_page_num
				+ "','" + new_page_increment + "');";
		Visit_SQLServer.execute_SQLServer(statement, query_statement);
		System.out.println(query_statement);

	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException, InterruptedException {
		String str = "nanjingdaxue";
		initial_Table_LilyDate(str);
	}

}
