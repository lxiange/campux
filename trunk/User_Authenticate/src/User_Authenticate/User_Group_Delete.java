/*
 * ���ã��û������
 *precondition��ĳDocument
 *postcondition: ���ܴ�Document��ʶ����Ự��š������� ���ڱ�workgroup��ɾ����Ӧ������򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Group_Delete 
{
	public static String user_Group_Delete(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String group_name_label="g";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root�������ӽڵ�"<si>"��"<g>"�����ж����ӽڵ�����
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(group_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String group_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, group_name_label);
		
		//����7������Ự���Ϊ��
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
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
		
		//����12���û��鲻����
		if(Connect_DBTable_WG.have_NameInWG(group_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("12");
			return response;
		}
		else
		{
			String group_no=Connect_DBTable_WG.get_NoByName(group_name);
			Connect_DBTable_WG.delete_ItemInWG(group_no, group_name);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
