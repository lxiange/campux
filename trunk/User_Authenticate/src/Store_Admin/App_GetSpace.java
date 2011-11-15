/*���ã���ȡapp�Ŀռ����
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ�������Ự��š��û��������ұ�ran_con���д˱�ţ���������admin�飬���ȡ�û�����Ӧ��space_allocated��space_used��������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package Store_Admin;
import java.util.ArrayList;

import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class App_GetSpace 
{
	public static String app_GetSpace(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String app_name_label="u";
		//root�������ӽڵ�"<si>"��"<u>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(app_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String app_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_name_label);
		
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
		//����17�������Ŷ�Ӧ��user_name����admin��ĳ�Ա
		String user_name=ran_con.get_UserByRCN(user_ran_con_num);
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsNoByUser(user_name);	
		String group_no_a=Connect_DBTable_WG.get_NoByName("admin");		
		if(Search_In_Arr.search_In_Arr(group_no_a, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("17");
			return response;
		}
		//����2���û���Ϊ��
		if(app_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		//����19��app_store�����޴��û�
		if(Connect_DBTable_AS.have_App(app_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("19");
			return response;
		}
		
		String space_allocated=Connect_DBTable_AS.get_AllocatedSpace(app_name);
		String space_used=Connect_DBTable_AS.get_UsedSpace(app_name);
		long space_allocated_long=Long.valueOf(space_allocated).longValue()/(1024*1024);
		long space_used_long=Long.valueOf(space_used).longValue()/(1024*1024);
		space_allocated=Long.toString(space_allocated_long);
		space_used=Long.toString(space_used_long);
		String str="<q>"+space_allocated+"(MB)</q>";
		str+="<a>"+space_used+"(MB)</a>";
		response=Create_RightResponseXml.create_RightResponseXml(str);
		return response;
	}

}
