/*
 * 作用：获取某个主题页的结构化内容，以ArrayList<String>返回
 * precondition: 某主题页的url
 * postcondition: 此主题页的结构化内容
 * 
 * 
 */
package Store_New_Theme;
import java.io.*;
import java.util.*;

import General_Function.Fragment_Searcher;
import General_Function.Get_SourceFile;

public class Lily_Content_Analyzer 
{
	public static ArrayList<String> lily_Content_Analyzer(String url)throws IOException
	{
		ArrayList<String> temp_arr=new ArrayList<String>();
		
		String theme_url=url;
				
		String source=Get_SourceFile.get_SourceFile(url);
		source=Fragment_Searcher.fragment_Searcher(source, "<textarea", "</textarea>");
//		String theme_domain=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"信区: ", ". 本篇人气");
		String theme_domain=Fragment_Searcher.fragment_Searcher_WithoutBoundary(url, "http://bbs.nju.edu.cn/bbscon?board=", "&file=");
		
		String theme_date=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"发信站: 南京大学小百合站 (", ")");
		if(theme_date.equals(""))
			theme_date=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"发信站: 南京大学小百合站自动发信系统 (", ")");
		if(theme_date.equals(""))
			theme_date=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"发信站: 南大小百合 (", ")");
		String theme_title=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"标  题: ", "发信站:");
		String theme_ip=theme_Ip_Analyzer(source);
		String author_name=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source,"发信人: ", ",");
		String reply_flag="f";
		if(theme_title.contains("Re:"))
		{
			reply_flag="t";
			int begin_index=theme_title.indexOf("Re: ")+4;
			theme_title=theme_title.substring(begin_index);
			
		}
		
		if(source.contains("发信站: 南大小百合 ("))
			source=Fragment_Searcher.fragment_Eraser(source,"<textarea","发信站: 南大小百合 ");
		else source=Fragment_Searcher.fragment_Eraser(source,"<textarea","发信站: 南京大学小百合站 ");
		source=Fragment_Searcher.fragment_Eraser(source,"(",")");
		String theme_content=theme_Content_Analyzer(source);
		
		//如果回复页中含有"【 在***的大作中提到: 】"，则在reply_content项中删去其后面的内容，且在reply_from项中添加被引用的作者名。
		String reply_from="";
		String temp_str=Fragment_Searcher.fragment_Searcher(theme_content, "【 在 ", " 的大作中提到: 】");
		if(temp_str!="")
		{
			int end_index=theme_content.indexOf("【 在 ");
			theme_content=theme_content.substring(0, end_index);
			reply_from=Fragment_Searcher.fragment_Searcher_WithoutBoundary(temp_str, "【 在 ", " 的大作中提到: 】");
		}
		
		
		
		//把读到的项目为空的url写到文件中，便于debug
//		if(theme_domain=="")when_NoContent("Failed_domain",url);
//		if(theme_date=="")when_NoContent("Failed_date",url);
//		if(theme_title=="")when_NoContent("Failed_title",url);
//		if(theme_ip=="")when_NoContent("Failed_ip",url);
//		if(author_name=="")when_NoContent("Failed_author",url);
		if(theme_content=="")when_NoContent("Failed_content",url);
		
				
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
	
	public static void when_NoContent(String catagory,String url)throws FileNotFoundException,IOException
	{
		String file_name="lily_result/"+catagory+".txt";
		FileOutputStream out=new FileOutputStream(file_name,true);
		byte[] xx=(url+"\r\n").getBytes();
		out.write(xx);
		out.flush();
		out.close();
	}
	
	public static String theme_Ip_Analyzer(String source)
	{
		String ip="";
		String str_a="[FROM: ";
		int end_index=source.lastIndexOf(str_a);
		if(end_index!=-1)
		{
			ip=source.substring(end_index);
			ip=Fragment_Searcher.fragment_Searcher_WithoutBoundary(ip, "[FROM: ", "]");
		}
		return ip;
	}
	public static String theme_Content_Analyzer(String source)
	{
		String temp_source=source;
		String str_a="--";
		String target="";
		int end_index=0;
		
		//当content中含有多个"--"时，以最后一个作为content的结束标记
		end_index=source.lastIndexOf(str_a);
		if(source.startsWith(", 站内信件"))
			source=source.substring(6);
		if(end_index!=-1)return source.substring(0,end_index);
				
		//当content中不含有"--"时，考虑以"※ 来源"或"※ 修改"为结束标记
		String str_b="※ 来源";
		String str_c="※ 修改";
		end_index=source.indexOf(str_b);
		if(end_index!=-1)return source.substring(0,end_index);
		end_index=source.indexOf(str_c);
		if(end_index!=-1)return source.substring(0,end_index);
		
		//很少一部分Url以"发信站: 南大小百合 ("作为日期开始字符，其内容一般以“ , 转信”开头，需要删去
		if(source.contains(", 转信"))
		{
			int index_a=source.indexOf(", 转信");
			System.out.println(index_a);
			source=source.substring(index_a+4);
		}
		else System.out.println("fuck");
		
		//考虑source中除了"</textarea>"都是content的情况。如果没有content则会返回""
		source=Fragment_Searcher.fragment_Searcher_WithoutBoundary(source, "", "</textarea>");
		
		return source;
	}
	
	
	public static void main(String[] args)throws IOException
	{
//		String url="http://bbs.nju.edu.cn/bbscon?board=NJUExpress&file=M.1315875879.A&num=17490";
//		String url="http://bbs.nju.edu.cn/bbscon?board=Advice&file=M.976870074.A&num=38";
		String url="http://bbs.nju.edu.cn/bbscon?board=Mediastudy&file=M.1314543032.A&num=344";
		
//		String url="http://bbs.nju.edu.cn/bbscon?board=WesternstyleChess&file=M.1092416437.A&num=410";
		ArrayList<String> temp_arr=lily_Content_Analyzer(url);
//		System.out.println(temp_arr.size());
		for(int i=0;i<temp_arr.size();i++)
			System.out.println(temp_arr.get(i));
	}

}
