/*作用：用户删除
 *precondition：某段Document
 *postcondition: 若能从Document中识别出随机编号、用户名，并且表ran_con中有此用户名，则从表ran_con和表user_authen中删去此用户名，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package User_Authenticate;
import General_Function.*;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;
import java.util.*;

public class User_Delete 
{
	public static String user_Delete(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String user_name_label="u";
		
		//root不含有子节点"<si>"和"<u>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=2||!all_child.contains(user_ran_con_label)||!all_child.contains(user_name_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String user_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_name_label);
		
		//错误7：随机用户编号为空
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
		
/*		//错误9：表中编号和用户名不匹配(删除用户只能是admin的操作，无需匹配)
		if(ran_con.have_Item(user_ran_con_num, user_name)==false)
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
		
		//从表ran_con和表user_authen中删去user_name项
		else
		{
			ran_con.delete_ItemFromRCL(ran_con.get_RCNByUSer(user_name));
			Connect_DBTable_UP.delete_ItemInUP(user_name);
			response=Create_RightResponseXml.create_RightResponseXml("");
			return response;
		}
	}

}
