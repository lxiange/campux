/*
 * 作用：用户组添加
 *precondition：某Document
 *postcondition: 若能从Document中识别出会话编号、组名， 则在表workgroup中删除相应的项，否则返回相应的错误响应的提示。
*/
package User_Authenticate;
import java.util.ArrayList;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Group_Delete 
{
	public static String user_Group_Delete(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String group_name_label="g";
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		//root不含有子节点"<si>"和"<g>"，或含有多余子节点的情况
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(group_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String group_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, group_name_label);
		
		//错误7：随机会话编号为空
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
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
		
		//错误12：用户组不存在
		if(Connect_DBTable_WG.have_NameInWG(group_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("12");
			return response;
		}
		else
		{
			String group_no=Connect_DBTable_WG.get_NoByName(group_name);
			Connect_DBTable_WG.delete_ItemInWG(group_no, group_name);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
