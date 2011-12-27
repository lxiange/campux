/*���ã��û������޸�
 *precondition��ĳ��Document
 *postcondition: ���ܴ�Document��ʶ��������š��û����룬���ұ�ran_con�������Ŷ�Ӧ���û������ڱ�user_authen���޸Ĵ��û��������룬������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package User_Authenticate;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public class User_Psw_Change 
{
	public static String user_Psw_Change(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String user_psw_label="p";
		
		//root�������ӽڵ�"<si>"��"<u>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(user_psw_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_psw=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_psw_label);
		
		// password convert to MD5  --by yuy
		try{
			MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
			byte [] digest = mda.digest(user_psw.getBytes());
			user_psw = String.format("%0128x", new BigInteger(1, digest));
		}catch(Exception exc){
			exc.printStackTrace();
			System.exit(1);
		}
		
		//����7������û����Ϊ��
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//����2���û�����Ϊ��
		if(user_psw=="")
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
		
		//�ҳ�ran_con�б�Ŷ�Ӧ���û�����
		String user_name=ran_con.get_UserByRCN(user_ran_con_num);
		//����4��user_authen�����޴��û�
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		else
		{
			//�ҳ�user_authen���û�����Ӧ�����룬��user_psw�滻֮
			Connect_DBTable_UP.set_Psw(user_name,user_psw);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
		
		
			
	}

}
