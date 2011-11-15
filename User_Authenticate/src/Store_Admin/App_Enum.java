/*作用：枚举app用户
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号，并且表ran_con中有此此编号，且其属于admin组，则枚举所有用户名，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Admin;
import java.util.ArrayList;

import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class App_Enum 
{
	public static String app_Enum(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root不含有子节点"<si>，或含有多余子节点的情况
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
		//错误17：随机编号对应的user_name不是admin组的成员
		String user_name=ran_con.get_UserByRCN(user_ran_con_num);
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsNoByUser(user_name);	
		//获取"admin"的组号
		String group_no=Connect_DBTable_WG.get_NoByName("admin");		
		if(Search_In_Arr.search_In_Arr(group_no, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("17");
			return response;
		}
		
		ArrayList<String> all_user=Connect_DBTable_AS.traverse_AS();
		for(int i=0;i<all_user.size();i++)
			response+="<u>"+all_user.get(i)+"</u>";
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	}

}
