/*
 * 作用：用户组枚举
 *precondition：某Document
 *postcondition: 若能从Document中识别出会话编号， 则把表workgroup中所有的项枚举出来，否则返回相应的错误响应的提示。
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Group_Enum
{
	public static String user_Group_Enum(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root不含有子节点"<si>"，或含有多余子节点的情况
		if(all_child.size()!=1||!all_child.contains(user_ran_con_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		
		//错误7：随机会话编号为空
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
		
		ArrayList<String> groups=Connect_DBTable_WG.get_AllGroupsName();
		for(int i=0;i<groups.size();i++)
			response+="<g>"+groups.get(i)+"</g>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}

}
