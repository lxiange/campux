/*作用：app用户的目录创建
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、目录路径，并且表ran_con中有此编号，且其属于app组，则删除此目录，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Use;
import java.util.ArrayList;
import java.io.*;

import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutFile.Directory_Function;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class Directory_Delete 
{
	public static String directory_Delete(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_directory_label="p";
		//root不含有子节点"<si>"和"<p>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_directory_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_directory=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_directory_label);
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
		//错误22：目录名为空
		if(sub_directory=="")
		{
			response=Connect_DBTable_WS.wrong_Response("22");
			return response;
		}
		//生成绝对路径名
		String absolute_path_of_sub_directory=absolute_path_prefix+user_name_a+"\\"+sub_directory;		
		
		File file=new File(absolute_path_of_sub_directory);
		//错误23：文件系统中无此目录名
		if(file.isDirectory()==false)
		{
			response=Connect_DBTable_WS.wrong_Response("23");
			return response;
			
		}	
		//错误28：无法删除子目录(由于系统等其他原因)
		File apr=new File(absolute_path_prefix+user_name_a);
		if(Directory_Function.delete_SubDirectory(apr, sub_directory)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("28");
			return response;
		}
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

}
