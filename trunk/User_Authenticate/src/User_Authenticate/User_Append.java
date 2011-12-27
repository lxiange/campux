/*���ã��û����
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ����û��������룬���ұ�user_authen���޴��û���������Ӵ��û�����������ȷ��Ӧ����ʾ��
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

public class User_Append 
{
	public static String user_Append(Document document)
	{
		String response="";
		String user_name_label="u";
		String user_psw_label="p";
		
		//root�������ӽڵ�"<u>"��"<p>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_name_label)||!all_child.contains(user_psw_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		String user_psw=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_psw_label);
		
		// password convert to SHA512  --by yuy
		try{
			MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
			byte [] digest = mda.digest(user_psw.getBytes());
			user_psw = String.format("%0128x", new BigInteger(1, digest));
		}catch(Exception exc){
			exc.printStackTrace();
			System.exit(1);
		}
		
		
		//����2���û���Ϊ��
		if(user_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		//����3������Ϊ��
		if(user_psw=="")
		{
			response=Connect_DBTable_WS.wrong_Response("3");
			return response;
		}
		//����6�����ݿ����Ѿ��д��û���
		if(Connect_DBTable_UP.have_User(user_name)==true)
		{
			response=Connect_DBTable_WS.wrong_Response("6");
			return response;
		}
		//�ڱ�user_authen�д������û�����������ȷ��ʾ
		else
		{
			Connect_DBTable_UP.append_NewUser(user_name,user_psw);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
