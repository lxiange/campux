/*
 * 从DB中的lily_date表中获取某subforum的最新theme_page的url
 *
 */
package com.bizdata.lilycrawler.config;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Visit_SQLServer;

public class Lily_FetchOldNewestUrl {
	public static ArrayList<String> lily_Fetch_Old_Newest_Url(String initial_url)
			throws IOException, ClassNotFoundException, SQLException {
		Statement statement = Visit_SQLServer.visit_SQLServer("1433", "lily",
				"sa", "123456");
		String query_statement = "select * from lily_date";

		ResultSet result = Visit_SQLServer.execute_SQLServer(statement,
				query_statement);
		String str_a = "";
		String str_b = "";
		ArrayList<String> target = new ArrayList<String>();
		target.add(initial_url);
		while (result.next()) {
			str_a = result.getString("subforum_url").trim();
			str_b = result.getString("theme_url").trim();

			if (initial_url.equals(str_a)) {
				target.add(str_b);
				String str = result.getString("theme_date").trim();
				target.add(str);
				str = result.getString("new_page_num").trim();
				target.add(str);
				str = result.getString("new_page_increment").trim();
				target.add(str);
				return target;
			}
		}
		target.add("NotFound");
		return target;
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException {
		// String url="http://bbs.nju.edu.cn/bbsdoc?board=AcademicReport";
		String url = "http://bbs.nju.edu.cn/bbsdoc?board=CCAsS";
		ArrayList<String> str = lily_Fetch_Old_Newest_Url(url);
		System.out.println(str.get(1));
		if (str.get(0).equals(""))
			System.out.println("Empty");
	}

}
