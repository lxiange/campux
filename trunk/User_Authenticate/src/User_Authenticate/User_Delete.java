/*���ã��û�ɾ��
 *precondition��ĳ��Document
 *postcondition: ���ܴ�Document��ʶ��������š��û��������ұ�ran_con���д��û�������ӱ�ran_con�ͱ�user_authen��ɾȥ���û�����������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package User_Authenticate;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;
import java.util.*;

public class User_Delete 
{
	public static String user_Delete(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String user_name_label="u";
		
		//root�������ӽڵ�"<si>"��"<u>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(user_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		
		//����7������û����Ϊ��
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
		//����8��ran_con�����޴�������
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		
/*		//����9�����б�ź��û�����ƥ��(ɾ���û�ֻ����admin�Ĳ���������ƥ��)
		if(ran_con.have_Item(user_ran_con_num, user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("9");
			return response;
		}
*/		
		//����4��user_authen�����޴��û�
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		
		//�ӱ�ran_con�ͱ�user_authen��ɾȥuser_name��
		else
		{
			ran_con.delete_ItemFromRCL(ran_con.get_RCNByUSer(user_name));
			Connect_DBTable_UP.delete_ItemInUP(user_name);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
