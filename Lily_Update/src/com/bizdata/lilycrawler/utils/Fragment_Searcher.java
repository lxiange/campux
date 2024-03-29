package com.bizdata.lilycrawler.utils;

/* 函数fragment_Searcher：
 * 在某个字符串中寻找某个子串。若找到则返回此子串，若找不到则返回""
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：找到的子串，或""
 *
 * 函数fragment_Searcher_WithoutBoundary：
 * 在某个字符串中寻找某个子串。若找到则返回此子串，若找不到则返回""。找到的内容不包含begin_str和end_str。
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：找到的子串，或""
 *
 *
 * 函数fragment_Searcher_All：
 * 在某个字符串中寻找某个子串的所有副本。若找到则返回所有子串的ArrayList，若找不到则返回""
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：找到的所有类似子串的原字符串
 *
 * 函数fragment_Eraser：
 * 在某个字符串中寻找某个子串。若找到则返回删除了此子串的原字符串，若找不到则返回原字符串
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：删除了子串的原字符串
 *
 * 函数fragment_Eraser_WithoutBoundary：
 * 在某个字符串中寻找某个子串。若找到则返回删除了此子串的原字符串，若找不到则返回原字符串。删除的内容不包含begin_str和end_str。
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：删除了子串的原字符串
 *
 *
 * 函数fragment_Eraser_All：
 * 在某个字符串中寻找某个子串的所有副本。若找到则返回删除了此种子串的原字符串，若找不到则返回原字符串
 * 输入：字符串source、子串开始串begin_str、子串结束串end_str
 * 输出：删除了所有类似子串的原字符串
 *
 *
 * 函数fragment_Replace：
 * 在某个字符串中寻找某个子串的第一个副本，将其替换成为另一个字符串
 * 输入：字符串source、原子串initial_str、替换子串substitute_str
 * 输出：替换了第一个子串的原字符串
 *
 *
 * 函数fragment_Replace_All：
 * 在某个字符串中寻找某个子串的所有副本，将其每个副本替换成为另一个字符串
 * 输入：字符串source、原子串initial_str、替换子串substitute_str
 * 输出：替换了所有原子串的原字符串
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
		String url = "发信人: Searet (午游的mj), 信区发信人: er3, 信区发信人: sdf, 信区发信人: cc, 信区";
		/*
		 * String source=Get_SourceFile.get_SourceFile(url);
		 * 
		 * 
		 * String
		 * replySum=fragment_Searcher(source,"<div class=\"info\">","</div>");
		 * String
		 * authorName=fragment_Searcher(source,"<!--开始首贴作者和日期-->","<!--结束首贴作者和日期-->"
		 * ); String
		 * allContent=fragment_Searcher(source,"<!--开始贴子内容-->","<!--结束贴子内容-->");
		 * 
		 * String allContent=fragment_Replace_All(url,"or","pp");
		 */
		String allContent = fragment_Searcher_WithoutBoundary(url, "发信人: ",
				", 信区");
		System.out.println(allContent);
		ArrayList<String> temp = fragment_Searcher_All(url, "发信人: ", ", 信区");
		for (int i = 0; i < temp.size(); i++)
			System.out.println(temp.get(i));
	}

}