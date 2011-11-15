/*
 * 作用：app用户的文件读取
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、文件路径、起始字节、终止字节，并且表ran_con中有此编号，且其属于app组，则读取相应的文件片段，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Use;
import java.util.ArrayList;
import java.io.*;

import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutFile.Connect_File;
import General_Function.General_Function_AboutFile.Directory_Function;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class File_Read 
{
	public static String file_Read(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_file_label="p";
		String begin_file_label="b";
		String end_file_label="e";
		//root不含有子节点"<si>","<p>","<b>","<e>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=4||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_file_label)||!all_child.contains(begin_file_label)||!all_child.contains(end_file_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_file_label);
		String begin_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, begin_file_label);
		String end_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, end_file_label);
		
		//错误7：随机会话编号为空
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//错误8：ran_con表中无此随机编号
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		//错误21：随机编号对应的user_name不是app组的成员
		String user_name_a=ran_con.get_UserByRCN(user_ran_con_num);
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsNoByUser(user_name_a);
		String group_no_a=Connect_DBTable_WG.get_NoByName("app");	
		if(Search_In_Arr.search_In_Arr(group_no_a, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("21");
			return response;
		}
		//错误31：文件名为空
		if(sub_file=="")
		{
			response=Connect_DBTable_WS.wrong_Response("31");
			return response;
		}
		//生成绝对路径名
		String absolute_path_of_sub_file=absolute_path_prefix+user_name_a+"\\"+sub_file;		
		File file=new File(absolute_path_of_sub_file);
		//错误29：文件系统中无此文件名
		if(file.isFile()==false)
		{
			response=Connect_DBTable_WS.wrong_Response("29");
			return response;
		}
		
		String content=Connect_File.read_FromFile(absolute_path_of_sub_file);

		int len=content.length();
		int begin_index=Integer.valueOf(begin_file).intValue();  
		int end_index=Integer.valueOf(end_file).intValue();  
		
		//错误33：读文件时越界
		if(begin_index>=len||begin_index<0||end_index<begin_index)
		{
			
			response=Connect_DBTable_WS.wrong_Response("33");
			return response;
		}
		String target="";
		if(end_index>=len)
			target=content.substring(begin_index,len);
		else
			target=content.substring(begin_index,end_index);
		response="<![CDATA["+target+"]]>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	
	}

}
