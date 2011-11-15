/*���ã�ö��app�û�
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ�������Ự��ţ����ұ�ran_con���д˴˱�ţ���������admin�飬��ö�������û�����������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package Store_Admin;
import java.util.ArrayList;

import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class App_Enum 
{
	public static String app_Enum(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root�������ӽڵ�"<si>�����ж����ӽڵ�����
		if(all_child.size()!=1||!all_child.contains(user_ran_con_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		
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
		//��ȡ"admin"�����
		String group_no=Connect_DBTable_WG.get_NoByName("admin");		
		if(Search_In_Arr.search_In_Arr(group_no, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("17");
			return response;
		}
		
		ArrayList<String> all_user=Connect_DBTable_AS.traverse_AS();
		for(int i=0;i<all_user.size();i++)
			response+="<u>"+all_user.get(i)+"</u>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}

}
