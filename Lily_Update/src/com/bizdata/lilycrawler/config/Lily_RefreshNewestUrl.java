/*
 * 用url更新DB中的lily_date表中某subforum的对应的最新theme_page的url
 *
 */

package com.bizdata.lilycrawler.config;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bizdata.lilycrawler.pageanalysis.Lily_Content_Analyzer;
import com.bizdata.lilycrawler.utils.Visit_SQLServer;

public class Lily_RefreshNewestUrl {
	public static void lily_Refresh_Newest_Url(String subforum_name, String url)
			throws IOException, ClassNotFoundException, SQLException,
			InterruptedException {
		ArrayList<String> new_arr = Lily_Content_Analyzer
				.lily_Content_Analyzer(url);
		String theme_url = new_arr.get(0);
		String theme_date = new_arr.get(2);
		// System.out.println(theme_date);

		Statement statement = Visit_SQLServer.visit_SQLServer("1433", "lily",
				"sa", "123456");
		String query_statement = "update lily_date set theme_url='" + theme_url
				+ "' where subforum_url='" + subforum_name + "'";
		statement.execute(query_statement);
		System.out.println(query_statement);

		query_statement = "update lily_date set theme_date='" + theme_date
				+ "' where subforum_url='" + subforum_name + "'";
		statement.execute(query_statement);
		System.out.println(query_statement);
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException, InterruptedException {
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=Mediastudy";
		String old_url = Lily_FetchOldNewestUrl.lily_Fetch_Old_Newest_Url(url)
				.get(1);
		System.out.println(old_url);
		String new_url = "http://bbs.nju.edu.cn/bbscon?board=Mediastudy&file=M.1213160758.A&num=269";
		lily_Refresh_Newest_Url(url, new_url);

		old_url = Lily_FetchOldNewestUrl.lily_Fetch_Old_Newest_Url(url).get(1);
		// System.out.println(old_url);
	}

}
