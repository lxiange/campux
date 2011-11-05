/*
 *作用：获取某theme_page的内容 
 *输入：某theme_page的url,(num为输出存储为文件时使用，存储为数据库时随意设个值即可)
 *输出：此theme_page的格式化内容存入数据库
 *
 * 
 */
package com.bizdata.lilycrawler.pageanalysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;

public class Lily_Get_ContentOfOneThemePage {
	public static void lily_Get_ContentOfOneThemePage(String url, int num)
			throws IOException, ClassNotFoundException, SQLException {
		// 获取theme_page的格式化内容
		ArrayList<String> temp_arr = Lily_Content_Analyzer
				.lily_Content_Analyzer(url);

		// 如果domain、date、title、ip、author_name、reply_from过长，很可能是此主题页的格式出问题了，则记录其url，且不予爬取
		if (too_Long(temp_arr)) {
			String file_name = "lily_result/format_failed.txt";
			FileOutputStream out = new FileOutputStream(file_name, true);
			byte[] xx = (url + "\r\n").getBytes();
			out.write(xx);
			out.flush();
			out.close();
		} else {
			// 对某主题页爬取到的内容的两种存储方式之一：文件
			// String temp_str=formal_ToFile(url);
			// content_Store_ToFile(temp_str,num);

			// 对某主题页爬取到的内容的两种存储方式之二：数据库
			content_Store_ToDB(temp_arr);
		}

	}

	// 如果domain、date、title、ip、author_name、reply_from过长，很可能是此主题页的格式出问题了，则记录其url，且不予爬取
	public static boolean too_Long(ArrayList<String> topic) {
		boolean flag = false;

		String domain = topic.get(1);
		String date = topic.get(2);
		String title = topic.get(3);
		String ip = topic.get(4);
		String author = topic.get(5);
		String from = topic.get(8);

		if ((domain.length() > 30) || (date.length() > 30)
				|| (title.length() > 50) || (ip.length() > 20)
				|| (author.length() > 50) || (from.length() > 50))
			flag = true;
		return flag;

	}

	// 对某主题页爬取到的内容的两种存储方式之二：数据库
	public static void content_Store_ToDB(ArrayList<String> topic)
			throws IOException, ClassNotFoundException, SQLException {
		String url = topic.get(0);
		String domain = topic.get(1);
		String date = topic.get(2);
		String title = change_Symbol(topic.get(3));
		String ip = topic.get(4);
		String author = change_Symbol(topic.get(5));
		String content = change_Symbol(topic.get(6));
		String from = change_Symbol(topic.get(8));
		String theme_or_reply = topic.get(7);
		// 根据内容中的'theme_or_reply'项的值，决定存入哪个表
		if (theme_or_reply == "f")
			store_To_ThemeTable(url, domain, date, title, ip, author, content);
		else
			store_To_ReplyTable(url, domain, date, title, ip, author, content,
					from);
	}

	// 根据内容中的'theme_or_reply'项的值，将theme_page的内容存入表'lily_theme'
	public static void store_To_ThemeTable(String url, String domain,
			String date, String title, String ip, String author, String content)
			throws IOException, ClassNotFoundException, SQLException {
		String query_Statement = "insert into lily_theme values(";
		query_Statement += "'" + url + "',";
		query_Statement += "'" + domain + "',";
		query_Statement += "'" + date + "',";
		title = new String(title.getBytes("GBK"));
		query_Statement += "'" + title + "',";
		query_Statement += "'" + ip + "',";
		author = new String(author.getBytes("GBK"));
		query_Statement += "'" + author + "',";
		content = new String(content.getBytes("GBK"));
		query_Statement += "'" + content + "'";
		query_Statement += ");";
		System.out.println(query_Statement);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection connection = DriverManager.getConnection(
				"jdbc:sqlserver://localhost:1433;Databasename=lily", "sa",
				"123456");
		// Connection connection =
		// DriverManager.getConnection("jdbc:sqlserver://192.168.1.5:1433;Databasename=lily","sa","shangshuBiz5_");
		Statement statement = connection.createStatement();
		statement.execute(query_Statement);

	}

	// 根据内容中的'theme_or_reply'项的值，将theme_page的内容存入表'lily_reply'
	public static void store_To_ReplyTable(String url, String domain,
			String date, String title, String ip, String author,
			String content, String reply_from) throws IOException,
			ClassNotFoundException, SQLException {
		String query_Statement = "insert into lily_reply values(";
		query_Statement += "'" + url + "',";
		query_Statement += "'" + domain + "',";
		query_Statement += "'" + date + "',";
		title = new String(title.getBytes("GBK"));
		query_Statement += "'" + title + "',";
		query_Statement += "'" + ip + "',";
		author = new String(author.getBytes("GBK"));
		query_Statement += "'" + author + "',";
		reply_from = new String(reply_from.getBytes("GBK"));
		query_Statement += "'" + reply_from + "',";
		content = new String(content.getBytes("GBK"));
		query_Statement += "'" + content + "'";
		query_Statement += ");";

		System.out.println(query_Statement);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection connection = DriverManager.getConnection(
				"jdbc:sqlserver://localhost:1433;Databasename=lily", "sa",
				"123456");
		// Connection connection =
		// DriverManager.getConnection("jdbc:sqlserver://192.168.1.5:1433;Databasename=lily","sa","shangshuBiz5_");
		Statement statement = connection.createStatement();
		statement.execute(query_Statement);
	}

	// 处理一些数据库不能正常处理的字符
	public static String change_Symbol(String source) {
		source = Fragment_Searcher.fragment_Replace_All(source, "'", "''"); // "·"无法正确读入数据库的问题
		return source;
	}

	// 将theme_page的内容处理成文件格式
	public static String formal_ToFile(String url) throws IOException {
		ArrayList<String> temp_arr = Lily_Content_Analyzer
				.lily_Content_Analyzer(url);
		String newLine = "\r\n";

		String temp_str = "";
		temp_str = temp_str + "Url: " + temp_arr.get(0);
		temp_str = temp_str + newLine + "Domain: " + temp_arr.get(1);
		temp_str = temp_str + newLine + "Date: " + temp_arr.get(2);
		temp_str = temp_str + newLine + "Title: " + temp_arr.get(3);
		temp_str = temp_str + newLine + "Ip: " + temp_arr.get(4);
		temp_str = temp_str + newLine + "Author: " + temp_arr.get(5);
		temp_str = temp_str + newLine + "Content: " + temp_arr.get(6);
		temp_str = temp_str + newLine + "Reply(t/f): " + temp_arr.get(7);
		temp_str = temp_str + newLine + "Reply_from: " + temp_arr.get(8);
		return temp_str;
	}

	// 将theme_page的内容存入文件
	public static void content_Store_ToFile(String source, int number)
			throws FileNotFoundException, IOException {
		String file_name = "lily_update_result/" + number + ".txt";
		FileOutputStream out = new FileOutputStream(file_name);
		byte[] buf = source.getBytes();
		out.write(buf);
		out.flush();
		out.close();
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException {
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=IFA_IS&file=M.1240397056.A&num=33";
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=NJUExpress&file=M.1316499236.A&num=16559";
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=NJUExpress&file=M.1316502506.A&num=16562";
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=AcademicReport&file=M.1270868965.A&num=6459";
		String url = "http://bbs.nju.edu.cn/bbstcon?board=NJUExpress&file=M.1316443140.A";
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=D_Maths&file=M.1316671294.A&num=11983";
		lily_Get_ContentOfOneThemePage(url, 312);
	}

}
