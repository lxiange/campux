
/*���ã��û���֤
 *precondition��ĳDocument
 *postcondition: ���ܴ�Document��ʶ����û��������룬���ܹ��ͱ�user_authen�е�ĳ����ȷƥ�䣬
 *               ���ڱ�ran_con�н���user_name��Ӧ�������������Ự��ţ����򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
*/

package User_Authenticate;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;
import org.dom4j.*;

public class User_Authen 
{
	public static String user_Authen(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_name_label="u";
		String user_psw_label="p";
		
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root�������ӽڵ�"<u>"��"<p>"�����ж����ӽڵ�����
		if(all_child.size()!=2||!all_child.contains(user_name_label)||!all_child.contains(user_psw_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		String user_psw=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_psw_label);
		
		// password convert to MD5 --by yuy
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
		//����4�����ݿ����޴��û���
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		
		//����15:�û���������ö�ٱ���״̬�£�5���Ӻ��ٷ���
		if(Connect_DBTable_UP.get_Status(user_name).equals("0")==false)
		{
			response=Connect_DBTable_WS.wrong_Response("15");
			return response;
		}
		//����5�����ݿ��е��û��������벻ƥ��
		if(Connect_DBTable_UP.user_MatchPsw(user_name,user_psw)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("5");
			return response;
		}
		
		//�û���������ƥ����ȷ��������ȷ��ʾ1
		else
		{
	     	String ran_con_number=Connect_DynamicTable_RCL.create_RCN(user_name);
			DynamicTable_RCL_Item item=Connect_DynamicTable_RCL.create_Item(ran_con_number, user_name, "3600000");
			if(ran_con.have_User(user_name)==false)
				ran_con.insert_RCL(item);
			response=Create_RightResponseXml.create_RightResponseXml(ran_con_number);
			return response;
		}
	}

}
