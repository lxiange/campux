package General_Function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_And_String 
{
	//函数long_ToDate用来将long值转化为Date值
	public static Date long_ToDate(long k)
	{
		Date date=new Date(k);
		return date;
	}
	//函数date_ToLong用来将Date值转化为long值
	public static long date_ToLong(Date date)
	{
		return date.getTime();
	}
	//函数date_ToString用来将Date值转化为String
	public static String date_ToString(Date date)
	{
		return date.toString();
	}
	//函数str_ToDate用来将符合某种Date格式的String转化为Date，以Date类型返回
	public static Date str_ToDate(String str)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/ddHH:mm");
		Date date=new Date();
		try
		{
			date = sdf.parse(str);
		}catch(Exception e){e.printStackTrace();}
		return date;

	}
	
	
	public static void main(String[] args)
	{
		String time_str_1="2011/10/2323:14";
		Date date=str_ToDate(time_str_1);
		System.out.println(date);
		System.out.println(date_ToLong(date));
		System.out.println(date.toString());
	}
}
