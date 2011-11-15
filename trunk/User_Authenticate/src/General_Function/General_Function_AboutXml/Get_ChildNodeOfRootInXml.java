/*
 * 函数judge_ElementInXml用于判断xml document的root元素是否含有以element_name为name的子元素。有则返回true，无则返回false。
 * 函数get_TextOfElementInXml用于获取xml document的root元素的以element_name为name的子元素的text。
 *     前提是document的root肯定含有element_name元素。
 * 函数get_AllNameOfChildNodeOfRoot用于获取document的root元素的所有子节点的name，以ArrayList返回。
 * 函数get_NumberOfChildNodeOfRoot用于获取document的root元素的子节点数量。
 */

package General_Function.General_Function_AboutXml;

import java.util.Iterator;
import java.util.List;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.Element;

public class Get_ChildNodeOfRootInXml 
{
	
	//获取document的root元素的所有子节点的name，以ArrayList返回
	public static ArrayList<String> get_AllNameOfChildNodeOfRoot(Document document)
	{
		ArrayList<String> child=new ArrayList<String>();
		Element root=document.getRootElement();
		String str_root=root.getName();
		List list=root.elements();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Element ele=(Element)it.next();
			String temp_str=ele.getName();
			child.add(temp_str);
		}
		return child;
	}
	
	//获取document的root元素的子节点数量
	public static int get_NumberOfChildNodeOfRoot(Document document)
	{
		int num=0;
		Element root=document.getRootElement();
		String str_root=root.getName();
	//	List list=document.selectNodes("/"+str_root+"/"+"aa");
		List list=root.elements();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			it.next();
			num++;
		}
		
		return num;
	}
	
	//检测某document里面是否含有给定的"<a>...</a>"
	public static boolean judge_ElementInXml(Document document,String element_name)
	{
		boolean flag=false;
		Element root=document.getRootElement();
		String str_root=root.getName();
		
		List list=document.selectNodes("/"+str_root+"/"+element_name);
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Element ele=(Element)it.next();
			String temp_str=ele.getName();
//			System.out.println(temp_str);
			if(temp_str.equals(element_name))
			{
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	//获取document的root元素的以element_name为name的childnode的text
	public static String get_TextOfElementInXml(Document document,String element_name)
	{
		String str="";
		Element root=document.getRootElement();
		String str_root=root.getName();
		
		List list=document.selectNodes("/"+str_root+"/"+element_name);
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Element ele=(Element)it.next();	
			String temp_str=ele.getName();
//			System.out.println(temp_str);
			if(temp_str.equals(element_name))
			{
				str=ele.getText();
				break;
			}
		}
		return str;
	}
	
	public static void main(String[] args)
	{
		String source="<fw><si>1364</si><p>app_a\\d_d\\ab.txt</p><b>5</b><d><![CDATA[aleiejof]]></d></fw>";
		String element_name="a";
		Document doc=Convert_StringToXml.convert_StringToXml(source);
		
		ArrayList<String> child=get_AllNameOfChildNodeOfRoot(doc);
		for(int i=0;i<child.size();i++)
			System.out.println(child.get(i));
		System.out.println(get_TextOfElementInXml(doc,"d"));
//		int aa=get_NumberOfChildNodeOfRoot(doc);
	//	System.out.println(aa);
/*		boolean flag=judge_ElementInXml(doc,element_name);
		if(flag==true)
		{
			String text=get_TextOfElementInXml(doc,element_name);
			System.out.println(text);
		}
		else System.out.println("NO");
		
*/		
//		String root=get_RootNameOfXml(doc);
//		System.out.println(root);
	}

}
