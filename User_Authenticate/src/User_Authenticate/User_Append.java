/*作用：用户添加
 *precondition：某段xml document
 *postcondition: 若能从document中识别出用户名、密码，并且表user_authen中无此用户名，则添加此用户名，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
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
		
		//root不含有子节点"<u>"和"<p>"，或含有多余子节点的情况
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
		
		
		//错误2：用户名为空
		if(user_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		//错误3：密码为空
		if(user_psw=="")
		{
			response=Connect_DBTable_WS.wrong_Response("3");
			return response;
		}
		//错误6：数据库中已经有此用户名
		if(Connect_DBTable_UP.have_User(user_name)==true)
		{
			response=Connect_DBTable_WS.wrong_Response("6");
			return response;
		}
		//在表user_authen中创建新用户，并返回正确提示
		else
		{
			Connect_DBTable_UP.append_NewUser(user_name,user_psw);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
