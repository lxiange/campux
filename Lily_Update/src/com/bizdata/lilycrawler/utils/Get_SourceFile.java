package com.bizdata.lilycrawler.utils;

/*
 *输入：网址domain
 *功能：利用函数getWebContent(String domain)获取网址为domain的网站的源文件
 *输出：一个String字符串，内容为domain的源文件      
 */
//package java_yssy_class;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Get_SourceFile {
	public static String get_SourceFile(String domain) throws IOException {
		// System.out.println("开始读取内容...("+domain+")");
		StringBuffer sb = new StringBuffer();

		try {
			URL url = new URL(domain);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));

			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			System.out.println("error happend");
		}

		String sbstr = sb.toString();

		return sbstr;
	}

	public static void main(String[] args) throws IOException {
		String domain = "https://bbs.sjtu.edu.cn/bbstdoc,board,SJTUNews,page,219.html";
		String source = get_SourceFile(domain);
		System.out.println(source);
	}

}