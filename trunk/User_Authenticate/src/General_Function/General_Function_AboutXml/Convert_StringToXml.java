/*
 * ��������������
 * ����judge_StringToXml(String) �ж�ĳ��String�ܷ�ת����xml document�����򷵻�true�����򷵻�false��
 * ���� convert_StringToXml��һ��String ת��Ϊxml document����Document���ء�ǰ���Ǵ�String����xml��ʽ��
 * 
 */

package General_Function.General_Function_AboutXml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

public class Convert_StringToXml 
{
	public static boolean judge_StringToXml(String source)
	{
		boolean flag=true;
		try
		{
			Document document=DocumentHelper.parseText(source);
		}
		catch(DocumentException d)
		{
			flag=false;
		}
		return flag;
	}
	
	public static Document convert_StringToXml(String source)
	{
		Document document=DocumentHelper.createDocument();
		try
		{
			document=DocumentHelper.parseText(source);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return document;
	}
	
	public static void main(String[] args)
	{
//		String source="<v><a>pp</a></v>";
		String source="<v><ab>pp</a></v>";
		boolean flag=judge_StringToXml(source);
		if(flag==true)System.out.println("OK");
		else System.out.println("NO");
		Document doc=convert_StringToXml(source);
	}

}
