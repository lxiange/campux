/*
 * precondition:ĳ��ȷ����file��File�ͱ���
 * postcondition���Դ�File������һЩ������
 * ����get_FileCreateDate���ڻ�ȡ�ļ��Ĵ���ʱ�䣬��String����
 * ����get_FileModifiedDate���ڻ�ȡ�ļ�������޸�ʱ�䣬��String����
 * ����delete_SubFile����ɾ����Ϊsub_file�����ļ�
 * ����create_SubFile�����½���Ϊsub_file�����ļ�
 * ����create_SubFile�����½���Ϊsub_file�����ļ�
 * ����get_FileSize���ڻ�ȡ�ļ����ݵĴ�С(byte)����String����
 */
package General_Function.General_Function_AboutFile;
import java.io.*;
import java.text.*;
import java.util.*;

import General_Function.Date_And_String;

public class File_Function 
{
	//����get_FileCreateDate���ڻ�ȡ�ļ��Ĵ���ʱ��(�޷���ʾ��������Ҫ�Ľ�)
	public static String get_FileCreateDate(File file) 
	{       
        try 
        {
            Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir " + file.getAbsolutePath() + " /tc");
            BufferedReader br = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
            for (int i = 0; i < 5; i++) 
            {
                br.readLine();
            }
            String stuff = br.readLine();
            StringTokenizer st = new StringTokenizer(stuff);
            String dateC = st.nextToken();
            String time = st.nextToken();
            String datetime = dateC.concat(time);
            datetime=Date_And_String.date_ToString(Date_And_String.str_ToDate(datetime));
            
            br.close();
            return datetime;
        } catch (Exception e) {
            return null;
        }
    }
	
	//����get_FileModifiedDate���ڻ�ȡ�ļ�������޸�ʱ��
	public static String get_FileModifiedDate(File file)
	{
		 long l=file.lastModified();
		 String str=Date_And_String.date_ToString(Date_And_String.long_ToDate(l));
		 return str;
	}
		
	//����delete_SubFile����ɾ����Ϊsub_file�����ļ�
	public static boolean delete_SubFile(File file,String sub_file)
	{
//		if(Directory_Function.have_SubFile(file,sub_file)==false)
//     		return false;
		String prefix=file.getAbsolutePath();
		String absolute_path=prefix+"\\"+sub_file;
//		System.out.println(absolute_path);
		File node=new File(absolute_path);
		if(node.delete())
		    return true; 
		else return false;
	}
	//����create_SubFile�����½���Ϊsub_file�����ļ�
	public static boolean create_SubFile(File file,String sub_file)
	{
		if(Directory_Function.have_SubFile(file,sub_file))
     		return false;
		String prefix=file.getAbsolutePath();
		String absolute_path=prefix+"\\"+sub_file;
		
//		System.out.println(absolute_path);		
		String dir=absolute_path.substring(0,absolute_path.lastIndexOf("\\"));
		File dir_node=new File(dir);
		dir_node.mkdirs();
//		System.out.println(dir);
		File node=new File(absolute_path);
		try
		{
			if(node.createNewFile())
				return true;
		}catch(Exception e){}
		return false;
	}
	
	//����get_FileSize���ڻ�ȡ�ļ����ݵĴ�С(byte)
	public static String get_FileSize(File file)
	{
		return Long.toString(file.length());
	}
	
	
	public static void main(String[] args)
	{ 
		String root="D:\\all_app\\";
		String relative_path="app_a\\c.txt";
		String absolute_path="application_a\\d_d\\2.txt";
		File r=new File(root);
		File file=new File(absolute_path);
		if(create_SubFile(r,absolute_path))
			System.out.println("OK");
		else System.out.println("NO");
//		System.out.println(r.length());
//		System.out.println(get_FileCreateDate(r));
//		System.out.println(get_FileModifiedDate(r));
/*		String init_time=get_FileCreateDate(file);
		String kk=str_ToDateLong(init_time);
		System.out.println(new Date(Long.valueOf(kk).longValue()).toString());
		
		String modify_time=get_FileModifiedDate(file);
		System.out.println(new Date(Long.valueOf(modify_time).longValue()).toString());
		
		System.out.println(str_ToDateLong(init_time));
		System.out.println(str_ToDateLong(modify_time));
*/
/*		if(create_SubFile(r,relative_path))
			System.out.println("OK");
		else System.out.println("NO");
*/	}

}
