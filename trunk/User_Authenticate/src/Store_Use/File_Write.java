/*
 * 作用：app用户的文件写入
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、文件路径、起始字节、数据，并且表ran_con中有此编号，且其属于app组，则读取相应的文件片段，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Use;

import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;

import Auxiliary_Function.Connect_DBTable_AS;
import Auxiliary_Function.Connect_DBTable_UG;
import Auxiliary_Function.Connect_DBTable_WG;
import Auxiliary_Function.Connect_DBTable_WS;
import Auxiliary_Function.Create_RightResponseXml;
import Auxiliary_Function.DynamicTable_RCL;
import General_Function.Search_In_Arr;
import General_Function.General_Function_AboutFile.Connect_File;
import General_Function.General_Function_AboutFile.File_Function;
import General_Function.General_Function_AboutXml.Get_ChildNodeOfRootInXml;

public class File_Write 
{
	public static String file_Write(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_file_label="p";
		String begin_file_label="b";
		String date_label="d";
		//root不含有子节点"<si>","<p>","<b>","<d>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=4||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_file_label)||!all_child.contains(begin_file_label)||!all_child.contains(date_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_file_label);
		String begin_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, begin_file_label);
		String date=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, date_label);
		
/*		//错误35：数据格式错误
		if(!date.startsWith("<![CDATA[")||!date.endsWith("]]>"))
		{
			response=Connect_DBTable_WS.wrong_Response("35");
			return response;
		}
*/		//错误7：随机会话编号为空
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
	
		//如果文件不存在，创建文件
		if(!file.isFile())
		{
			//错误34：无法创建子文件(由于系统等其他原因)
			File apr=new File(absolute_path_prefix+user_name_a);
			if(!File_Function.create_SubFile(apr, sub_file))
			{
				response=Connect_DBTable_WS.wrong_Response("34");
				return response;
			}
		}
		String content=Connect_File.read_FromFile(absolute_path_of_sub_file);
		long len=content.length();
		int begin_index=Integer.valueOf(begin_file).intValue();  
		
		String app_user=ran_con.get_UserByRCN(user_ran_con_num);
		String space_allocated=Connect_DBTable_AS.get_AllocatedSpace(app_user);
		String space_used=Connect_DBTable_AS.get_UsedSpace(app_user);
		long space_allocated_long=Long.valueOf(space_allocated).longValue();//Byte
		long space_used_long=Long.valueOf(space_used).longValue();//Byte
		
//		System.out.println(ran_con.get_UserByRCN(user_ran_con_num));
//		System.out.println(absolute_path_of_sub_file);
//		System.out.println(len);
//		System.out.println(space_allocated);
//		System.out.println(space_used);
		//begin_index>=length时，从begin_index处将新数据追加进文件
		if(begin_index>len)
		{
			long new_file_size=date.length();
//			System.out.println(new_file_size);
			//错误37：app用户空间配额超出
			if(space_used_long+new_file_size>=space_allocated_long)
			{
				response=Connect_DBTable_WS.wrong_Response("37");
				return response;
			}
			//错误36：无法写入文件(由于系统等其他原因)
			if(!Connect_File.write_ToFileByAppend(absolute_path_of_sub_file, date))
			{
				response=Connect_DBTable_WS.wrong_Response("36");
				return response;
			}
			//写入数据成功，更新app_store 表中的对应项的space_used。
//			System.out.println(Long.toString(space_used_long+new_file_size));
			Connect_DBTable_AS.set_UsedSpace(app_user, Long.toString(space_used_long+new_file_size));
		}
		//begin_index<length时，从begin_index处将新数据覆盖进文件
		else
		{
			content=content.substring(0, begin_index);
			date=content+date;
			long new_file_size=date.length()-len;
			//错误37：app用户空间配额超出
			if(space_used_long+new_file_size>=space_allocated_long)
			{
				response=Connect_DBTable_WS.wrong_Response("37");
				return response;
			}
			//错误36：无法写入文件(由于系统等其他原因)
			if(!Connect_File.write_ToFile(absolute_path_of_sub_file, date))
			{
				response=Connect_DBTable_WS.wrong_Response("36");
				return response;
			}
			//写入数据成功，更新app_store 表中的对应项的space_used。
			Connect_DBTable_AS.set_UsedSpace(app_user, Long.toString(space_used_long+new_file_size));
		}
		
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	
	}

}
