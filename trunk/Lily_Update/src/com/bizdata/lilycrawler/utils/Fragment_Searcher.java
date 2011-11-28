package com.bizdata.lilycrawler.utils;

/* ����fragment_Searcher��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������ҵ��򷵻ش��Ӵ������Ҳ����򷵻�""
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * ������ҵ����Ӵ�����""
 *
 * ����fragment_Searcher_WithoutBoundary��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������ҵ��򷵻ش��Ӵ������Ҳ����򷵻�""���ҵ������ݲ�����begin_str��end_str��
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * ������ҵ����Ӵ�����""
 *
 *
 * ����fragment_Searcher_All��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������и��������ҵ��򷵻������Ӵ���ArrayList�����Ҳ����򷵻�""
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * ������ҵ������������Ӵ���ԭ�ַ���
 *
 * ����fragment_Eraser��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������ҵ��򷵻�ɾ���˴��Ӵ���ԭ�ַ��������Ҳ����򷵻�ԭ�ַ���
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * �����ɾ�����Ӵ���ԭ�ַ���
 *
 * ����fragment_Eraser_WithoutBoundary��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������ҵ��򷵻�ɾ���˴��Ӵ���ԭ�ַ��������Ҳ����򷵻�ԭ�ַ�����ɾ�������ݲ�����begin_str��end_str��
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * �����ɾ�����Ӵ���ԭ�ַ���
 *
 *
 * ����fragment_Eraser_All��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������и��������ҵ��򷵻�ɾ���˴����Ӵ���ԭ�ַ��������Ҳ����򷵻�ԭ�ַ���
 * ���룺�ַ���source���Ӵ���ʼ��begin_str���Ӵ�������end_str
 * �����ɾ�������������Ӵ���ԭ�ַ���
 *
 *
 * ����fragment_Replace��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ��ĵ�һ�������������滻��Ϊ��һ���ַ���
 * ���룺�ַ���source��ԭ�Ӵ�initial_str���滻�Ӵ�substitute_str
 * ������滻�˵�һ���Ӵ���ԭ�ַ���
 *
 *
 * ����fragment_Replace_All��
 * ��ĳ���ַ�����Ѱ��ĳ���Ӵ������и���������ÿ�������滻��Ϊ��һ���ַ���
 * ���룺�ַ���source��ԭ�Ӵ�initial_str���滻�Ӵ�substitute_str
 * ������滻������ԭ�Ӵ���ԭ�ַ���
 */

//package java_yssy_class;
import java.io.IOException;
import java.util.ArrayList;

public class Fragment_Searcher {
	public static String fragment_Searcher(String source, String begin_str,
			String end_str) {
		int len_source = source.length();
		int len_end_str = end_str.length();

		int begin_index = source.indexOf(begin_str);
		if (begin_index == -1)
			return "";
		// if(begin_index==-1) return error_Happen(begin_str);

		int end_index = source.indexOf(end_str, begin_index) + len_end_str;
		if (end_index == len_end_str - 1)
			return error_Happen(end_str);

		String result = source.substring(begin_index, end_index);
		return result;

	}

	public static String fragment_Searcher_WithoutBoundary(String source,
			String begin_str, String end_str) {
		int len_source = source.length();
		int len_begin_str = begin_str.length();
		int len_end_str = end_str.length();

		int begin_index = source.indexOf(begin_str) + len_begin_str;
		if (begin_index == len_begin_str - 1)
			return "";
		// if(begin_index==-1) return error_Happen(begin_str);

		int end_index = source.indexOf(end_str, begin_index);
		if (end_index == -1)
			return error_Happen(end_str);

		String result = source.substring(begin_index, end_index);
		return result;

	}

	public static ArrayList<String> fragment_Searcher_All(String source,
			String begin_str, String end_str) {
		int len_source = source.length();
		int len_end_str = end_str.length();
		ArrayList<String> content_list = new ArrayList<String>();
		String temp;

		for (int i = 0; i < len_source; i++) {
			int begin_index = source.indexOf(begin_str);
			if (begin_index == -1)
				break;

			int end_index = source.indexOf(end_str, begin_index) + len_end_str;
			if (end_index == len_end_str - 1)
				break;

			temp = source.substring(begin_index, end_index);

			content_list.add(temp);

			source = source.substring(end_index, len_source);
			len_source = source.length();
		}

		return content_list;

	}

	public static String error_Happen(String error_str) {
		// System.out.println("\""+error_str+"\""+" can't be found!");
		return "";

	}

	public static String fragment_Eraser(String source, String begin_str,
			String end_str) {
		int len_source = source.length();
		int len_end_str = end_str.length();

		int begin_index = source.indexOf(begin_str);
		if (begin_index == -1)
			return source;
		// if(begin_index==-1) return error_Happen(begin_str);

		int end_index = source.indexOf(end_str, begin_index) + len_end_str;
		// if(end_index==len_end_str-1) return error_Happen(end_str);

		String prefix = source.substring(0, begin_index);
		String postfix = source.substring(end_index, len_source);
		String remain = prefix + postfix;
		return remain;
	}

	public static String fragment_Eraser_WithoutBoundary(String source,
			String begin_str, String end_str) {
		int len_source = source.length();
		int len_begin_str = begin_str.length();
		int len_end_str = end_str.length();

		int begin_index = source.indexOf(begin_str);
		if (begin_index == -1)
			return source;
		// if(begin_index==-1) return error_Happen(begin_str);

		int end_index = source.indexOf(end_str, begin_index);
		// if(end_index==len_end_str-1) return error_Happen(end_str);

		String prefix = source.substring(0, begin_index + len_begin_str);
		String postfix = source.substring(end_index, len_source);
		String remain = prefix + postfix;
		return remain;
	}

	public static String fragment_Eraser_All(String source, String begin_str,
			String end_str) {
		int len_source = source.length();
		for (int i = 0; i < len_source; i++) {
			source = fragment_Eraser(source, begin_str, end_str);
			len_source = source.length();
		}
		return source;
	}

	public static String fragment_Replace(String source, String initial_str,
			String substitute_str) {
		source = source.replaceFirst(initial_str, substitute_str);
		return source;
	}

	public static String fragment_Replace_All(String source,
			String initial_str, String substitute_str) {
		source = source.replace(initial_str, substitute_str);
		return source;
	}

	public static void main(String[] args) throws IOException {
		String url = "������: Searet (���ε�mj), ����������: er3, ����������: sdf, ����������: cc, ����";
		/*
		 * String source=Get_SourceFile.get_SourceFile(url);
		 * 
		 * 
		 * String
		 * replySum=fragment_Searcher(source,"<div class=\"info\">","</div>");
		 * String
		 * authorName=fragment_Searcher(source,"<!--��ʼ�������ߺ�����-->","<!--�����������ߺ�����-->"
		 * ); String
		 * allContent=fragment_Searcher(source,"<!--��ʼ��������-->","<!--������������-->");
		 * 
		 * String allContent=fragment_Replace_All(url,"or","pp");
		 */
		String allContent = fragment_Searcher_WithoutBoundary(url, "������: ",
				", ����");
		System.out.println(allContent);
		ArrayList<String> temp = fragment_Searcher_All(url, "������: ", ", ����");
		for (int i = 0; i < temp.size(); i++)
			System.out.println(temp.get(i));
	}

}