/*
 * precondition:某个确定是directory的File型变量
 * postcondition：对此File变量的一些处理函数
 * 函数get_AllChildrenName获取目录名内的所有子文件和子目录的名字，以ArrayList<String>返回
 * 函数get_AllChildrenPath获取目录名内的所有子文件和子目录的绝对路径，以ArrayList<String>返回
 * 函数get_AllSubDirectoryName获取目录名内的所有子目录的名字，以ArrayList<String>返回
 * 函数get_AllSubDirectoryPath获取目录名内的所有子目录的绝对路径，以ArrayList<String>返回
 * 函数get_AllSubFileName获取目录名内的所有子文件的名字，以ArrayList<String>返回
 * 函数get_AllSubFilePath获取目录名内的所有子文件的绝对路径，以ArrayList<String>返回
 * 函数have_SubDirectory用于判断是否存在名为sub_directory的子目录
 * 函数have_SubFile用于判断是否存在名为sub_file的子文件
 * 函数add_SubDirectory用于创建名为sub_directory的新的子目录
 * 函数delete_SubDirectory用于删除名为sub_directory的子目录
 * 函数get_DirectoryCreateDate用于获取目录的创建时间，以String返回
 * 函数get_DirectoryModifiedDate用于获取目录的最近修改时间，以String返回
 * 函数get_DirectorySize用于获取目录内所有内容的大小(byte)，以String返回
 */
package General_Function.General_Function_AboutFile;
import java.io.*;
import java.util.*;

import General_Function.Date_And_String;

public class Directory_Function 
{
	//函数get_AllChildrenName获取目录名内的所有子文件和子目录的名字，以ArrayList<String>返回
	public static ArrayList<String> get_AllChildrenName(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		for(int i=0;i<str_arr.length;i++)
			list.add(str_arr[i]);
		
		return list;
	}
	//函数get_AllChildrenPath获取目录名内的所有子文件和子目录的绝对路径，以ArrayList<String>返回
	public static ArrayList<String> get_AllChildrenPath(File file)
	{
		ArrayList<String> list=new ArrayList<String>();
		String[] str_arr=file.list();
		String prefix=file.getAbsolutePath();
		for(int i=0;i<str_arr.length;i++)
			list.add(prefix+"\\"+str_arr[i]);
		return list;
	}
		
	//函数get_AllSubDirectoryName获取目录名内的所有子目录的名字，以ArrayList<String>返回
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
	//函数get_AllSubDirectoryPath获取目录名内的所有子目录的绝对路径，以ArrayList<String>返回
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
			
	//函数get_AllSubFileName获取目录名内的所有子文件的名字，以ArrayList<String>返回
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
   //函数get_AllSubFilePath获取目录名内的所有子文件的绝对路径，以ArrayList<String>返回
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
	
	
	//函数have_SubDirectory用于判断是否存在名为sub_directory的(proper)子目录
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
	//函数have_SubFile用于判断是否存在名为sub_file的子文件
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

	//函数add_SubDirectory用于创建名为sub_directory的新的子目录
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
	//函数delete_SubDirectory用于删除名为sub_directory的子目录
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
	
	//辅助函数delete_Dir用于删除非空目录
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
	
	// 函数get_DirectoryModifiedDate用于获取目录的最近修改时间，以String返回
	public static String get_DirectoryModifiedDate(File dir)
	{
		 long l=dir.lastModified();
		 String str=Date_And_String.date_ToString(Date_And_String.long_ToDate(l));
		 return str;
	}
	
	//函数get_DirectorySize用于获取目录内所有内容的大小(byte)，以String返回
	public static String get_DirectorySize(File dir)
	{
		String len=Long.toString(get_DirSize(dir));		
		return len;
	}
	//辅助函数get_DirSizer用于获取非空目录所含内容的大小
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
	
	//函数get_DirectoryCreateDate用于获取目录的创建时间，以String返回
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
