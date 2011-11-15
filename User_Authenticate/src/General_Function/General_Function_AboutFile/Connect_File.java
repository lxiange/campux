package General_Function.General_Function_AboutFile;
import java.io.*;

public class Connect_File 
{
	//读取文件中数据
	public static String read_FromFile(String file_name)
	{
		String str="";
		try
		{
			FileInputStream in=new FileInputStream(file_name);
			int len=in.available();
			byte[] buf=new byte[len];
			in.read(buf,0,len);
			str=new String(buf,0,len);		
		}
		catch(IOException e){e.printStackTrace();}

		return str;
	}
	//向文件中覆盖数据
	public static boolean write_ToFile(String file_name,String content)
	{
		boolean flag=false;
		try
		{
			byte[] buf=content.getBytes();
			FileOutputStream out=new FileOutputStream(file_name);
			out.write(buf);
	        out.close();
	        flag=true;
		}
		catch(IOException e){e.printStackTrace();}
		return flag;
	}
	//向文件中追加数据
	public static boolean write_ToFileByAppend(String file_name,String content)
	{
		boolean flag=false;
		try
		{
			byte[] buf=content.getBytes();
			FileOutputStream out=new FileOutputStream(file_name,true);
			out.write(buf);
	        out.close();
	        flag=true;
		}
		catch(IOException e){e.printStackTrace();}
		return flag;
	}
	
	
	public static void main(String[] args)
	{
		String file_name="d:/1.xml";
		String str=read_FromFile(file_name);
		System.out.println(str);	
		
		file_name="d:/2.txt";
		write_ToFile(str,file_name);
		
	}

}
