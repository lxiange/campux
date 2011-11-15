/*
 * ���ã��û��������û���
 *precondition��ĳDocument
 *postcondition: ���ܴ�Document��ʶ����Ự��š��û������û������� �����user_group���н���(usr_name,group_name)����򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Connect_Group
{
	public static String user_Connect_Group(Document document,DynamicTable_RCL ran_con,String app_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String user_name_label="u";
		String group_name_label="g";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=3||!all_child.contains(user_ran_con_label)||!all_child.contains(user_name_label)||!all_child.contains(group_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		String group_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, group_name_label);
		
		//����7������Ự���Ϊ��
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//����2���û���Ϊ��
		if(user_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		//����10���û�����Ϊ��
		if(group_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("10");
			return response;
		}
		//����8��ran_con�����޴�������
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		//����4��user_psw�����޴��û���
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		//����12��workgroup���в����ڴ��û���
		if(Connect_DBTable_WG.have_NameInWG(group_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("12");
			return response;
		}
		
		String group_no=Connect_DBTable_WG.get_NoByName(group_name);
		//����14��user_group�����Ѵ���(user_name,group_no)��
		if(Connect_DBTable_UG.have_ItemInUG(user_name,group_no)==true)
		{
			response=Connect_DBTable_WS.wrong_Response("14"); 
			return response;
		}
		//����40��group_noΪ"app"
		if(group_name.equals("app"))
		{
			response=Connect_DBTable_WS.wrong_Response("40"); 
			return response;
		}
		
		Connect_DBTable_UG.append_ItemInUG(user_name, group_no,"");
//		System.out.println(user_name+" "+group_no);
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

}
