/*
 * ��һ��xml documentת��ΪString
 */
package General_Function.General_Function_AboutXml;

import org.dom4j.Document;

public class Convert_XmlToString 
{
	public static String convert_XmlToString(Document document)
	{
		String text=document.asXML();
		return text;
	}	

}
