/*
 * ���ã���ȡĳ������ҳ�Ľṹ�����ݣ���ArrayList<String>����
 * precondition: ĳ����ҳ��url
 * postcondition: ������ҳ�Ľṹ������
 * 
 * 
 */
package com.bizdata.lilycrawler.pageanalysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.bizdata.lilycrawler.utils.Fragment_Searcher;
import com.bizdata.lilycrawler.utils.Get_SourceFile;

public class Lily_Content_Analyzer {
	public static ArrayList<String> lily_Content_Analyzer(String url)
			throws IOException {
		ArrayList<String> temp_arr = new ArrayList<String>();

		String theme_url = url;

		String source = Get_SourceFile.get_SourceFile(url);
		source = Fragment_Searcher.fragment_Searcher(source, "<textarea",
				"</textarea>");
		// String
		// theme_domain=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"����: ",
		// ". ��ƪ����");
		String theme_domain = Fragment_Searcher
				.fragment_Searcher_WithoutBoundary(url,
						"http://bbs.nju.edu.cn/bbscon?board=", "&file=");

		String theme_date = Fragment_Searcher
				.fragment_Searcher_WithoutBoundary(source, "����վ: �Ͼ���ѧС�ٺ�վ (",
						")");
		if (theme_date.equals(""))
			theme_date = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					source, "����վ: �Ͼ���ѧС�ٺ�վ�Զ�����ϵͳ (", ")");
		if (theme_date.equals(""))
			theme_date = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					source, "����վ: �ϴ�С�ٺ� (", ")");
		String theme_title = Fragment_Searcher
				.fragment_Searcher_WithoutBoundary(source, "��  ��: ", "����վ:");
		String theme_ip = theme_Ip_Analyzer(source);
		String author_name = Fragment_Searcher
				.fragment_Searcher_WithoutBoundary(source, "������: ", ",");
		String reply_flag = "f";
		if (theme_title.contains("Re:")) {
			reply_flag = "t";
			int begin_index = theme_title.indexOf("Re: ") + 4;
			theme_title = theme_title.substring(begin_index);

		}

		if (source.contains("����վ: �ϴ�С�ٺ� ("))
			source = Fragment_Searcher.fragment_Eraser(source, "<textarea",
					"����վ: �ϴ�С�ٺ� ");
		else
			source = Fragment_Searcher.fragment_Eraser(source, "<textarea",
					"����վ: �Ͼ���ѧС�ٺ�վ ");
		source = Fragment_Searcher.fragment_Eraser(source, "(", ")");
		String theme_content = theme_Content_Analyzer(source);

		// ����ظ�ҳ�к���"�� ��***�Ĵ������ᵽ: ��"������reply_content����ɾȥ���������ݣ�����reply_from�������ӱ����õ���������
		String reply_from = "";
		String temp_str = Fragment_Searcher.fragment_Searcher(theme_content,
				"�� �� ", " �Ĵ������ᵽ: ��");
		if (temp_str != "") {
			int end_index = theme_content.indexOf("�� �� ");
			theme_content = theme_content.substring(0, end_index);
			reply_from = Fragment_Searcher.fragment_Searcher_WithoutBoundary(
					temp_str, "�� �� ", " �Ĵ������ᵽ: ��");
		}

		// �Ѷ�������ĿΪ�յ�urlд���ļ��У�����debug
		// if(theme_domain=="")when_NoContent("Failed_domain",url);
		// if(theme_date=="")when_NoContent("Failed_date",url);
		// if(theme_title=="")when_NoContent("Failed_title",url);
		// if(theme_ip=="")when_NoContent("Failed_ip",url);
		// if(author_name=="")when_NoContent("Failed_author",url);
		if (theme_content == "")
			when_NoContent("Failed_content", url);

		temp_arr.add(theme_url);
		temp_arr.add(theme_domain);
		temp_arr.add(theme_date);
		temp_arr.add(theme_title);
		temp_arr.add(theme_ip);
		temp_arr.add(author_name);
		temp_arr.add(theme_content);
		temp_arr.add(reply_flag);
		temp_arr.add(reply_from);
		return temp_arr;
	}

	public static void when_NoContent(String catagory, String url)
			throws FileNotFoundException, IOException {
		String file_name = "lily_result/" + catagory + ".txt";
		FileOutputStream out = new FileOutputStream(file_name, true);
		byte[] xx = (url + "\r\n").getBytes();
		out.write(xx);
		out.flush();
		out.close();
	}

	public static String theme_Ip_Analyzer(String source) {
		String ip = "";
		String str_a = "[FROM: ";
		int end_index = source.lastIndexOf(str_a);
		if (end_index != -1) {
			ip = source.substring(end_index);
			ip = Fragment_Searcher.fragment_Searcher_WithoutBoundary(ip,
					"[FROM: ", "]");
		}
		return ip;
	}

	public static String theme_Content_Analyzer(String source) {
		String temp_source = source;
		String str_a = "--";
		String target = "";
		int end_index = 0;

		// ��content�к��ж��"--"ʱ�������һ����Ϊcontent�Ľ������
		end_index = source.lastIndexOf(str_a);
		if (source.startsWith(", վ���ż�"))
			source = source.substring(6);
		if (end_index != -1)
			return source.substring(0, end_index);

		// ��content�в�����"--"ʱ��������"�� ��Դ"��"�� �޸�"Ϊ�������
		String str_b = "�� ��Դ";
		String str_c = "�� �޸�";
		end_index = source.indexOf(str_b);
		if (end_index != -1)
			return source.substring(0, end_index);
		end_index = source.indexOf(str_c);
		if (end_index != -1)
			return source.substring(0, end_index);

		// ����һ����Url��"����վ: �ϴ�С�ٺ� ("��Ϊ���ڿ�ʼ�ַ���������һ���ԡ� , ת�š���ͷ����Ҫɾȥ
		if (source.contains(", ת��")) {
			int index_a = source.indexOf(", ת��");
			System.out.println(index_a);
			source = source.substring(index_a + 4);
		} else
			System.out.println("fuck");

		// ����source�г���"</textarea>"����content����������û��content��᷵��""
		source = Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,
				"", "</textarea>");

		return source;
	}

	public static void main(String[] args) throws IOException {
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=NJUExpress&file=M.1315875879.A&num=17490";
		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=Advice&file=M.976870074.A&num=38";
		String url = "http://bbs.nju.edu.cn/bbscon?board=Mediastudy&file=M.1314543032.A&num=344";

		// String
		// url="http://bbs.nju.edu.cn/bbscon?board=WesternstyleChess&file=M.1092416437.A&num=410";
		ArrayList<String> temp_arr = lily_Content_Analyzer(url);
		// System.out.println(temp_arr.size());
		for (int i = 0; i < temp_arr.size(); i++)
			System.out.println(temp_arr.get(i));
	}

}