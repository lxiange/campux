/*作用：app添加
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、用户名，并且表ran_con中有此编号，且其属于admin组，则将用户名添加到app_store中，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Admin;
import java.util.ArrayList;

import General_Function.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class App_Add 
{
	public static String app_Add(Document document,DynamicTable_RCL ran_con,String app_path_prefix)
	{ 		
		String response="";
		String user_ran_con_label="si";
		String app_name_label="u";
		//root不含有子节点"<si>"和"<u>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(app_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String app_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_name_label);
		
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
		String group_no_a=Connect_DBTable_WG.get_NoByName("admin");		
		if(Search_In_Arr.search_In_Arr(group_no_a, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("17");
			return response;
		}
		//错误2：用户名为空
		if(app_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("2");
			return response;
		}
		String group_no=Connect_DBTable_WG.get_NoByName("app");
		//错误18：user_group表中已经有此用户
		if(Connect_DBTable_UG.have_ItemInUG(user_name, group_no))
		{
			response=Connect_DBTable_WS.wrong_Response("18");
			return response;
		}
		
		Connect_DBTable_UG.append_ItemInUG(app_name, group_no,app_path_prefix);
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

}
