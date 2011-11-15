/*
 * 作用：app用户的目录(或文件)属性获取
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、目录(或文件)路径，并且表ran_con中有此编号，且其属于app组，则获取此目录(或文件)的属性(创建时间、最近修改时间、文件长度)，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Use;
import java.util.ArrayList;
import java.io.*;

import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutFile.Directory_Function;
import General_Function.General_Function_AboutFile.File_Function;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class File_GetProperty 
{
	public static String file_GetProperty(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_file_label="p";
		//root不含有子节点"<si>"和"<p>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_file_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_file_label);
//		System.out.println(sub_directory);
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
//		System.out.println(absolute_path_of_sub_file);
		File file=new File(absolute_path_of_sub_file);
		
		if(!file.isFile()&&!file.isDirectory())
		{
			//错误29：文件系统中无此文件名
			if(!file.isFile())
			{
				response=Connect_DBTable_WS.wrong_Response("29");
				return response;
			}
			//错误23：文件系统中无此目录名
			else if(!file.isDirectory())
			{
				response=Connect_DBTable_WS.wrong_Response("23");
				return response;
			}	
		}	
		
		String initial_time="";
		String latest_change_time="";
		String file_size="";
		//对于路径是一个文件的情况
		if(file.isFile())
		{
			response="<t>f</t>";
			initial_time=File_Function.get_FileCreateDate(file);
			latest_change_time=File_Function.get_FileModifiedDate(file);
			file_size=File_Function.get_FileSize(file);
		}
		else
		{
			response="<t>d</t>";
			initial_time=Directory_Function.get_DirectoryCreateDate(file);
			latest_change_time=Directory_Function.get_DirectoryModifiedDate(file);
			file_size=Directory_Function.get_DirectorySize(file);
		}
		response+="<i>"+initial_time+"</i>";
		response+="<m>"+latest_change_time+"</m>";
		response+="<l>"+file_size+"(B)"+"</l>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}


}
