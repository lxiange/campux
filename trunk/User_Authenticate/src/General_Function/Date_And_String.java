package General_Function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_And_String 
{
	//����long_ToDate������longֵת��ΪDateֵ
	public static Date long_ToDate(long k)
	{
		Date date=new Date(k);
		return date;
	}
	//����date_ToLong������Dateֵת��Ϊlongֵ
	public static long date_ToLong(Date date)
	{
		return date.getTime();
	}
	//����date_ToString������Dateֵת��ΪString
	public static String date_ToString(Date date)
	{
		return date.toString();
	}
	//����str_ToDate����������ĳ��Date��ʽ��Stringת��ΪDate����Date���ͷ���
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
