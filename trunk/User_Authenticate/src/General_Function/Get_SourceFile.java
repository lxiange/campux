package General_Function;
/*
 *���룺��ַdomain
 *���ܣ����ú���getWebContent(String domain)��ȡ��ַΪdomain����վ��Դ�ļ�
 *�����һ��String�ַ���������Ϊdomain��Դ�ļ�      
*/
//package java_yssy_class;
import java.io.*;
import java.net.*;

public class Get_SourceFile
{
	public static String get_SourceFile(String domain)throws IOException
	{
	//	System.out.println("��ʼ��ȡ����...("+domain+")");
		StringBuffer sb=new StringBuffer();
		
		try
		{
			URL url=new URL(domain);
			BufferedReader in=new BufferedReader(new InputStreamReader(url.openStream()));
			
			String line;
			while((line=in.readLine())!=null)
			{
				sb.append(line);
			}
			in.close();
		}
		catch(Exception e)
		{
			System.out.println("error happend");
		}

		String sbstr=sb.toString();		
		
		return sbstr;
	}
	
	
	public static void main(String[] args)throws IOException
	{
		String domain="https://bbs.sjtu.edu.cn/bbstdoc,board,SJTUNews,page,219.html";
		String source=get_SourceFile(domain);
		System.out.println(source);
	}
	
}