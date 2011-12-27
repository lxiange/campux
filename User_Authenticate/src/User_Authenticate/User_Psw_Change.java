/*作用：用户密码修改
 *precondition：某段Document
 *postcondition: 若能从Document中识别出随机编号、用户密码，并且表ran_con中随机编号对应的用户名，在表user_authen中修改此用户名的密码，返回正确响应的提示，
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

public class User_Psw_Change 
{
	public static String user_Psw_Change(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String user_psw_label="p";
		
		//root不含有子节点"<si>"和"<u>"，或含有多余子节点的情况
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
		
		//错误7：随机用户编号为空
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//错误2：用户密码为空
		if(user_psw=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		
		//错误8：ran_con表中无此随机编号
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		
		//找出ran_con中编号对应的用户名，
		String user_name=ran_con.get_UserByRCN(user_ran_con_num);
		//错误4：user_authen表中无此用户
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		else
		{
			//找出user_authen中用户名对应的密码，以user_psw替换之
			Connect_DBTable_UP.set_Psw(user_name,user_psw);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
		
		
			
	}

}
