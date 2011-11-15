/*
 * 获取xml document的root元素的name，以String返回
 * 
 */

package General_Function.General_Function_AboutXml;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

public class Get_RootOfXml 
{
	public static String  get_RootNameOfXml(Document document)
	{
		String result="";
		Element root=document.getRootElement();
		String str_root=root.getName();
		return str_root;
	}
	public static void main(String[] args)
	{
		String source="<v><a>pp</a></v>";
		Document doc=Convert_StringToXml.convert_StringToXml(source);
		String root=get_RootNameOfXml(doc);
		System.out.println(root);
	}

}
