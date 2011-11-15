/*作用：读取app的空间配额
 *precondition：某段xml document
 *postcondition: 若能从document中识别出随机会话编号、用户名，并且表ran_con中有此编号，且其属于admin组，则读取用户名对应的space_allocated和space_used，返回正确响应的提示，
 *               否则返回相应的错误响应的提示。
 */
package Store_Admin;
import java.util.ArrayList;

import org.dom4j.Document;

import General_Function.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

public class App_SetSpace 
{
	public static String app_SetSpace(Document document,DynamicTable_RCL ran_con)
	{
		String response="";
		String user_ran_con_label="si";
		String app_name_label="u";
		String app_space_label="q";
		//root不含有子节点"<si>","<u>"和"<q>"，或含有多余子节点的情况
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=3||!all_child.contains(user_ran_con_label)||!all_child.contains(app_name_label)||!all_child.contains(app_space_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String app_name=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_name_label);
		String app_space=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, app_space_label);
//		System.out.println(app_space);
		if(app_space.contains("(MB)"))
			app_space=Fragment_Searcher.fragment_Eraser(app_space, "(", "MB)");
//		System.out.println(app_space);
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
		//错误19：app_store表中无此用户
		if(Connect_DBTable_AS.have_App(app_name)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("19");
			return response;
		}
		//错误20：设置的app空间为空
		if(app_space=="")
		{
			response=Connect_DBTable_WS.wrong_Response("20");
			return response;
		}
		
		long l = Long.valueOf(app_space).longValue()*1024*1024;
		app_space=Long.toString(l);
		Connect_DBTable_AS.set_AllocatedSpace(app_name, app_space);
		Connect_DBTable_AS.set_UsedSpace(app_name, "0");
		response=Create_RightResponseXml.create_RightResponseXml("");
		return response;
	}

/*	public static void main(String[] args)
	{
		String str="<mus></mus>";
		Document doc=Convert_StringToXml.convert_StringToXml(str);
		DynamicTable_RCL ran_con=new DynamicTable_RCL();
		app_SetSpace(doc,ran_con);
	}
*/
}
