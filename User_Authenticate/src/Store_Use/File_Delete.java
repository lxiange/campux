/*
 * ���ã�app�û����ļ�ɾ��
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ�������Ự��š��ļ�·�������ұ�ran_con���д˱�ţ���������app�飬��ɾ�����ļ���������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
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

public class File_Delete 
{
	public static String file_Delete(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_file_label="p";
		//root�������ӽڵ�"<si>"��"<p>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_file_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_file_label);
//		System.out.println(sub_directory);
		//����7������Ự���Ϊ��
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//����8��ran_con�����޴�������
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		//����21�������Ŷ�Ӧ��user_name����app��ĳ�Ա
		String user_name_a=ran_con.get_UserByRCN(user_ran_con_num);
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsNoByUser(user_name_a);
		String group_no_a=Connect_DBTable_WG.get_NoByName("app");	
		if(Search_In_Arr.search_In_Arr(group_no_a, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("21");
			return response;
		}
		//����31���ļ���Ϊ��
		if(sub_file=="")
		{
			response=Connect_DBTable_WS.wrong_Response("31");
			return response;
		}
		//���ɾ���·����
		String absolute_path_of_sub_file=absolute_path_prefix+user_name_a+"\\"+sub_file;
		File file=new File(absolute_path_of_sub_file);
		//����29���ļ�ϵͳ���޴��ļ���
		if(file.isFile()==false)
		{
	        response=Connect_DBTable_WS.wrong_Response("29");
			return response;
		}	
		long file_size=file.length();
		//����32���޷�ɾ�����ļ�(����ϵͳ������ԭ��)
		File apr=new File(absolute_path_prefix+user_name_a);
		if(File_Function.delete_SubFile(apr, sub_file)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("32");
			return response;
		}
		//ɾ���ɹ������¸���app_store ���еĶ�Ӧ���space_used��
		String app_user=ran_con.get_UserByRCN(user_ran_con_num);
		String space_used=Connect_DBTable_AS.get_UsedSpace(app_user);
//		System.out.println(space_used);
//		System.out.println(file_size);
		long space_used_long=Long.valueOf(space_used).longValue();//Byte
		Connect_DBTable_AS.set_UsedSpace(app_user, Long.toString(space_used_long-file_size));
		space_used=Connect_DBTable_AS.get_UsedSpace(app_user);
//		System.out.println(space_used);
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}

}
