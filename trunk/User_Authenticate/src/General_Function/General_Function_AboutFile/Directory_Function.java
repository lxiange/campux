/*
 * precondition:ĳ��ȷ����directory��File�ͱ���
 * postcondition���Դ�File������һЩ������
 * ����get_AllChildrenName��ȡĿ¼���ڵ��������ļ�����Ŀ¼�����֣���ArrayList<String>����
 * ����get_AllChildrenPath��ȡĿ¼���ڵ��������ļ�����Ŀ¼�ľ���·������ArrayList<String>����
 * ����get_AllSubDirectoryName��ȡĿ¼���ڵ�������Ŀ¼�����֣���ArrayList<String>����
 * ����get_AllSubDirectoryPath��ȡĿ¼���ڵ�������Ŀ¼�ľ���·������ArrayList<String>����
 * ����get_AllSubFileName��ȡĿ¼���ڵ��������ļ������֣���ArrayList<String>����
 * ����get_AllSubFilePath��ȡĿ¼���ڵ��������ļ��ľ���·������ArrayList<String>����
 * ����have_SubDirectory�����ж��Ƿ������Ϊsub_directory����Ŀ¼
 * ����have_SubFile�����ж��Ƿ������Ϊsub_file�����ļ�
 * ����add_SubDirectory���ڴ�����Ϊsub_directory���µ���Ŀ¼
 * ����delete_SubDirectory����ɾ����Ϊsub_directory����Ŀ¼
 * ����get_DirectoryCreateDate���ڻ�ȡĿ¼�Ĵ���ʱ�䣬��String����
 * ����get_DirectoryModifiedDate���ڻ�ȡĿ¼������޸�ʱ�䣬��String����
 * ����get_DirectorySize���ڻ�ȡĿ¼���������ݵĴ�С(byte)����String����
 */
package General_Function.General_Function_AboutFile;
import java.io.*;
import java.util.*;

import General_Function.Date_And_String;

public class Directory_Function 
{
	//����get_AllChildrenName��ȡĿ¼���ڵ��������ļ�����Ŀ¼�����֣���ArrayList<String>����
	public static ArrayList<String> get_AllChildrenName(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		for(int i=0;i<str_arr.length;i++)
			list.add(str_arr[i]);
		
		return list;
	}
	//����get_AllChildrenPath��ȡĿ¼���ڵ��������ļ�����Ŀ¼�ľ���·������ArrayList<String>����
	public static ArrayList<String> get_AllChildrenPath(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
			list.add(prefix+"\\"+str_arr[i]);
		return list;
	}
		
	//����get_AllSubDirectoryName��ȡĿ¼���ڵ�������Ŀ¼�����֣���ArrayList<String>����
	public static ArrayList<String> get_AllSubDirectoryName(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		if(str_arr.length==0)return list;
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
		{
			String absolute_path=prefix+"\\"+str_arr[i];
			File node=new File(absolute_path);
			if(node.isDirectory())
				list.add(str_arr[i]);
		}
		
		return list;
	}
	//����get_AllSubDirectoryPath��ȡĿ¼���ڵ�������Ŀ¼�ľ���·������ArrayList<String>����
	public static ArrayList<String> get_AllSubDirectoryPath(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
		{
			String absolute_path=prefix+"\\"+str_arr[i];
			File node=new File(absolute_path);
			if(node.isDirectory())
				list.add(absolute_path);
		}
		return list;
	}
			
	//����get_AllSubFileName��ȡĿ¼���ڵ��������ļ������֣���ArrayList<String>����
	public static ArrayList<String> get_AllSubFileName(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
		{
			String absolute_path=prefix+"\\"+str_arr[i];
			File node=new File(absolute_path);
			if(node.isFile())
				list.add(str_arr[i]);
		}
		
		return list;
	}
   //����get_AllSubFilePath��ȡĿ¼���ڵ��������ļ��ľ���·������ArrayList<String>����
	public static ArrayList<String> get_AllSubFilePath(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
		{
			String absolute_path=prefix+"\\"+str_arr[i];
			File node=new File(absolute_path);
			if(node.isFile())
				list.add(absolute_path);
		}
		return list;
	}
	
	
	//����have_SubDirectory�����ж��Ƿ������Ϊsub_directory��(proper)��Ŀ¼
	public static boolean have_SubDirectory(File file,String sub_directory)
	{
		boolean flag=false;
		ArrayList<String> all_child=get_AllSubDirectoryName(file);
		for(int i=0;i<all_child.size();i++)
		{
			if(all_child.get(i).equals(sub_directory))
			{
				flag=true;
				return flag;
			}
		}
		
		return flag;
	}
	//����have_SubFile�����ж��Ƿ������Ϊsub_file�����ļ�
	public static boolean have_SubFile(File file,String sub_file)
	{
		boolean flag=false;
		ArrayList<String> all_child=get_AllSubFileName(file);
		for(int i=0;i<all_child.size();i++)
		{
			if(all_child.get(i).equals(sub_file))
			{
				flag=true;
				return flag;
			}
		}
		
		return flag;
	}

	//����add_SubDirectory���ڴ�����Ϊsub_directory���µ���Ŀ¼
	public static boolean add_SubDirectory(File file,String sub_directory)
	{
		if(have_SubDirectory(file,sub_directory))
			return false;
		String prefix=file.getAbsolutePath();
		String absolute_path=prefix+"\\"+sub_directory;
		File node=new File(absolute_path);
		if(node.mkdirs())
		    return true; 
		else return false;
	}
	//����delete_SubDirectory����ɾ����Ϊsub_directory����Ŀ¼
	public static boolean delete_SubDirectory(File file,String sub_directory)
	{
//		if(have_SubDirectory(file,sub_directory)==false)
//			return false;
		String prefix=file.getAbsolutePath();
		String absolute_path=prefix+"\\"+sub_directory;
//		System.out.println(absolute_path);
		File node=new File(absolute_path);
		if(delete_Dir(node))
		    return true; 
		else return false;
	}
	
	//��������delete_Dir����ɾ���ǿ�Ŀ¼
	public static boolean delete_Dir(File dir)
	{
		boolean flag=false;
		for(File file:dir.listFiles())
		{
			if(file.isFile())
				file.delete();
			else if(file.isDirectory())
				delete_Dir(file);
		}
		dir.delete();
		flag=true;
		return flag;
	}
	
	// ����get_DirectoryModifiedDate���ڻ�ȡĿ¼������޸�ʱ�䣬��String����
	public static String get_DirectoryModifiedDate(File dir)
	{
		 long l=dir.lastModified();
		 String str=Date_And_String.date_ToString(Date_And_String.long_ToDate(l));
		 return str;
	}
	
	//����get_DirectorySize���ڻ�ȡĿ¼���������ݵĴ�С(byte)����String����
	public static String get_DirectorySize(File dir)
	{
		String len=Long.toString(get_DirSize(dir));		
		return len;
	}
	//��������get_DirSizer���ڻ�ȡ�ǿ�Ŀ¼�������ݵĴ�С
	public static long get_DirSize(File dir)
	{
		long len=0;
		for(File file:dir.listFiles())
		{
			if(file.isFile())
				len+=file.length();
			else if(file.isDirectory())
				len+=get_DirSize(file);
		}
		return len;
	}
	
	//����get_DirectoryCreateDate���ڻ�ȡĿ¼�Ĵ���ʱ�䣬��String����
	public static String get_DirectoryCreateDate(File dir)
	{
		 try 
	        {
	            Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir " + dir.getAbsolutePath() + " /tc");
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
	
	public static void main(String[] args)
	{
		String absolute_path="D:\\StormMedia\\";
		File file=new File(absolute_path);
		System.out.println(absolute_path+"football\\matcheew");
		if(have_SubDirectory(file,"football\\matcheew"))System.out.println("OK");
		else System.out.println("NO");

//		System.out.println(get_DirectoryCreateDate(file));
//		System.out.println(get_DirectoryModifiedDate(file));
//		System.out.println(get_DirectorySize(file));
//		ArrayList<String> list=get_AllSubDirectoryName(file);
//		for(int i=0;i<list.size();i++)
//			System.out.println(list.get(i));
	}

}
