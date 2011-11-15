/*
 * 作用：用户会话认证
 *precondition：某Document
 *postcondition: 若能从Document中识别出随机会话编号，并能够和表ran_con中的某项正确匹配，则返回此项的user_name，
 *               否则返回相应的错误响应的提示。
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
		//root不含有子节点"<s>，或含有多余子节点的情况
		if(all_child.size()!=1||!all_child.contains(user_ran_con_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		//错误7：随机用户编号为空
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//错误8：ran_con表中无此随机编号
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
