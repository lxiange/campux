/*���ã�app�û���Ŀ¼ö��
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ�������Ự��š�Ŀ¼·�������ұ�ran_con���д˱�ţ���������app�飬��ö�ٴ�Ŀ¼�µ��ļ�����������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package Store_Use;

import java.util.ArrayList;
import java.io.*;
import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutFile.Directory_Function;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class Directory_Enum 
{
	public static String directory_Enum(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String app_directory_label="p";
		//root�������ӽڵ�"<si>"��"<p>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(app_directory_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String app_directory=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_directory_label);
		
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
		//����22��Ŀ¼��Ϊ��
		if(app_directory=="")
		{
			response=Connect_DBTable_WS.wrong_Response("22");
			return response;
		}
		//���ɾ���·����
		app_directory=absolute_path_prefix+app_directory;
//		System.out.println(app_directory);
		File file=new File(app_directory);
		//����23���ļ�ϵͳ���޴�Ŀ¼��
		if(file.isDirectory()==false)
		{
			response=Connect_DBTable_WS.wrong_Response("23");
			return response;
		}
		//����24��appû�з��ʴ�Ŀ¼����Ȩ��
		if(file.canRead()==false)
		{
			response=Connect_DBTable_WS.wrong_Response("24");
			return response;
		}
	
		ArrayList<String> sub_file=Directory_Function.get_AllSubFileName(file);
		for(int i=0;i<sub_file.size();i++)
			response+="<f>"+sub_file.get(i)+"</f>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
		
	}

}
