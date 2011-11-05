/*
 *���ã���ȡĳtheme_page������ 
 *���룺ĳtheme_page��url,(numΪ����洢Ϊ�ļ�ʱʹ�ã��洢Ϊ���ݿ�ʱ�������ֵ����)
 *�������theme_page�ĸ�ʽ�����ݴ������ݿ�
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
		// ��ȡtheme_page�ĸ�ʽ������
		ArrayList<String> temp_arr = Lily_Content_Analyzer
				.lily_Content_Analyzer(url);

		// ���domain��date��title��ip��author_name��reply_from�������ܿ����Ǵ�����ҳ�ĸ�ʽ�������ˣ����¼��url���Ҳ�����ȡ
		if (too_Long(temp_arr)) {
			String file_name = "lily_result/format_failed.txt";
			FileOutputStream out = new FileOutputStream(file_name, true);
			byte[] xx = (url + "\r\n").getBytes();
			out.write(xx);
			out.flush();
			out.close();
		} else {
			// ��ĳ����ҳ��ȡ�������ݵ����ִ洢��ʽ֮һ���ļ�
			// String temp_str=formal_ToFile(url);
			// content_Store_ToFile(temp_str,num);

			// ��ĳ����ҳ��ȡ�������ݵ����ִ洢��ʽ֮�������ݿ�
			content_Store_ToDB(temp_arr);
		}

	}

	// ���domain��date��title��ip��author_name��reply_from�������ܿ����Ǵ�����ҳ�ĸ�ʽ�������ˣ����¼��url���Ҳ�����ȡ
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

	// ��ĳ����ҳ��ȡ�������ݵ����ִ洢��ʽ֮�������ݿ�
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
		// ���������е�'theme_or_reply'���ֵ�����������ĸ���
		if (theme_or_reply == "f")
			store_To_ThemeTable(url, domain, date, title, ip, author, content);
		else
			store_To_ReplyTable(url, domain, date, title, ip, author, content,
					from);
	}

	// ���������е�'theme_or_reply'���ֵ����theme_page�����ݴ����'lily_theme'
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

	// ���������е�'theme_or_reply'���ֵ����theme_page�����ݴ����'lily_reply'
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

	// ����һЩ���ݿⲻ������������ַ�
	public static String change_Symbol(String source) {
		source = Fragment_Searcher.fragment_Replace_All(source, "'", "''"); // "��"�޷���ȷ�������ݿ������
		return source;
	}

	// ��theme_page�����ݴ�����ļ���ʽ
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

	// ��theme_page�����ݴ����ļ�
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
