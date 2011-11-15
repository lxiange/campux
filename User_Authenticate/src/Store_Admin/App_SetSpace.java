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

public class App_SetSpace 
{
	public static String app_SetSpace(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String app_name_label="u";
		String app_space_label="q";
		//root�������ӽڵ�"<si>","<u>"��"<q>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=3||!all_child.contains(user_ran_con_label)||!all_child.contains(app_name_label)||!all_child.contains(app_space_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String app_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_name_label);
		String app_space=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_space_label);
//		System.out.println(app_space);
		if(app_space.contains("(MB)"))
			app_space=Fragment_Searcher.fragment_Eraser(app_space, "(", "MB)");
//		System.out.println(app_space);
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
		//����20�����õ�app�ռ�Ϊ��
		if(app_space=="")
		{
			response=Connect_DBTable_WS.wrong_Response("20");
			return response;
		}
		
		long l = Long.valueOf(app_space).longValue()*1024*1024;
		app_space=Long.toString(l);
		Connect_DBTable_AS.set_AllocatedSpace(app_name, app_space);
		Connect_DBTable_AS.set_UsedSpace(app_name, "0");
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

/*	public static void main(String[] args)
	{
		String str="<mus></mus>";
		Document doc=Convert_StringToXml.convert_StringToXml(str);
		DynamicTable_RCL ran_con=new DynamicTable_RCL();
		app_SetSpace(doc,ran_con);
	}
*/
}
