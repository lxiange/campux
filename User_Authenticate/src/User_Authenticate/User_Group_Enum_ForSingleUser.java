/*
 * 作用：用户的用户组属性枚举
 *precondition：某Document
 *postcondition: 若能从Document中识别出会话编号、用户名， 则把表workgroup中所有与用户名相关的项枚举出来，否则返回相应的错误响应的提示。
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Group_Enum_ForSingleUser
{
	public static String user_Group_Enum_ForSingleUser(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String user_name_label="u";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(user_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		
		//错误7：随机会话编号为空
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//错误2：用户名为空
		if(user_name=="")
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
/*		//错误9：ran_con表中编号和用户名不匹配
		if(Connect_DBTable_RCL.number_MatchUser(user_ran_con_num, user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("9");
			return response;
		}
*/
		//错误4：user_authen表中无此用户
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsByUser(user_name);
		for(int i=0;i<groups.size();i++)
			response+="<g>"+groups.get(i)+"</g>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}

}
