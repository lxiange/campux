/*
 * 作用：用户关联到用户组
 *precondition：某Document
 *postcondition: 若能从Document中识别出会话编号、用户名、用户组名， 则把在user_group表中建立(usr_name,group_name)项，否则返回相应的错误响应的提示。
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Connect_Group
{
	public static String user_Connect_Group(Document document,DynamicTable_RCL ran_con,String app_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String user_name_label="u";
		String group_name_label="g";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=3||!all_child.contains(user_ran_con_label)||!all_child.contains(user_name_label)||!all_child.contains(group_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		String group_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, group_name_label);
		
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
		//错误10：用户组名为空
		if(group_name=="")
		{
			response=Connect_DBTable_WS.wrong_Response("10");
			return response;
		}
		//错误8：ran_con表中无此随机编号
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		//错误4：user_psw表中无此用户名
		if(Connect_DBTable_UP.have_User(user_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("4");
			return response;
		}
		//错误12：workgroup表中不存在此用户组
		if(Connect_DBTable_WG.have_NameInWG(group_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("12");
			return response;
		}
		
		String group_no=Connect_DBTable_WG.get_NoByName(group_name);
		//错误14：user_group表中已存在(user_name,group_no)项
		if(Connect_DBTable_UG.have_ItemInUG(user_name,group_no)==true)
		{
			response=Connect_DBTable_WS.wrong_Response("14"); 
			return response;
		}
		//错误40：group_no为"app"
		if(group_name.equals("app"))
		{
			response=Connect_DBTable_WS.wrong_Response("40"); 
			return response;
		}
		
		Connect_DBTable_UG.append_ItemInUG(user_name, group_no,"");
//		System.out.println(user_name+" "+group_no);
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

}
