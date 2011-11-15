/*
 * ���ã��û��Ự��֤
 *precondition��ĳDocument
 *postcondition: ���ܴ�Document��ʶ�������Ự��ţ����ܹ��ͱ�ran_con�е�ĳ����ȷƥ�䣬�򷵻ش����user_name��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
*/

package User_Authenticate;
import java.util.ArrayList;

import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Conv_Authen 
{
	public static String user_Conv_Authen(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="s";
		
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root�������ӽڵ�"<s>�����ж����ӽڵ�����
		if(all_child.size()!=1||!all_child.contains(user_ran_con_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		//����7������û����Ϊ��
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
		else
		{
			String user_name=ran_con.get_UserByRCN(user_ran_con_num);
			response=Create_RightResponseXml.create_RightResponseXml(user_name);
			return response;
		}
	}

}
