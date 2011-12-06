package Main_Function;

import General_Function.*;
import Store_Admin.*;
import Store_Use.Directory_Add;
import Store_Use.Directory_Delete;
import Store_Use.Directory_Enum;
import Store_Use.File_Delete;
import Store_Use.File_Exist;
import Store_Use.File_GetProperty;
import Store_Use.File_Read;
import Store_Use.File_Write;
import User_Authenticate.*;
import General_Function.General_Function_AboutXml.*;
import Auxiliary_Function.*;

import org.dom4j.*;

public class User_Authenticate_Test 
{
	public static String user_Authenticate_Test(String source,DynamicTable_RCL ran_con)
	{
		//某app用户的默认root
		String absolute_path_prefix="D:\\all_app\\";
		String response="";
		//source不能正确解析为xml
		if(Convert_StringToXml.judge_StringToXml(source)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		
		Document document=Convert_StringToXml.convert_StringToXml(source);
		String root_name=Get_RootOfXml.get_RootNameOfXml(document);
		if(root_name.equals("v"))
			response=User_Authen.user_Authen(document,ran_con);
		else if(root_name.equals("o"))
			response=User_Logout.user_Logout(document,ran_con);
		else if(root_name.equals("a"))
			response=User_Append.user_Append(document);
		else if(root_name.equals("c"))
			response=User_Conv_Authen.user_Conv_Authen(document,ran_con);
		else if(root_name.equals("d"))
			response=User_Delete.user_Delete(document,ran_con);
		else if(root_name.equals("m"))
			response=User_Psw_Change.user_Psw_Change(document,ran_con);
		else if(root_name.equals("ga"))
			response=User_Group_Append.user_Group_Append(document,ran_con);
		else if(root_name.equals("gd"))
			response=User_Group_Delete.user_Group_Delete(document,ran_con);
		else if(root_name.equals("gl"))
			response=User_Group_Enum.user_Group_Enum(document,ran_con);
		else if(root_name.equals("ug"))
			response=User_Group_Enum_ForSingleUser.user_Group_Enum_ForSingleUser(document,ran_con);
		else if(root_name.equals("gul"))
			response=User_Enum_ForSingleGroup.user_Enum_ForSingleGroup(document,ran_con);
		else if(root_name.equals("gua"))
			response=User_Connect_Group.user_Connect_Group(document,ran_con, absolute_path_prefix);
		else if(root_name.equals("gur"))
			response=User_Disconnect_Group.user_Disconnect_Group(document,ran_con, absolute_path_prefix);
		else if(root_name.equals("mul"))
			response=App_Enum.app_Enum(document, ran_con);
		else if(root_name.equals("mua"))
			response=App_Add.app_Add(document, ran_con,absolute_path_prefix);
		else if(root_name.equals("mud"))
			response=App_Delete.app_Delete(document, ran_con,absolute_path_prefix);
		else if(root_name.equals("mur"))
			response=App_GetSpace.app_GetSpace(document, ran_con);
		else if(root_name.equals("mus"))
			response=App_SetSpace.app_SetSpace(document, ran_con);
		else if(root_name.equals("l"))
			response=Directory_Enum.directory_Enum(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("md"))
			response=Directory_Add.directory_Add(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("r"))
			response=Directory_Delete.directory_Delete(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("e"))
			response=File_Exist.file_Exist(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("df"))
			response=File_Delete.file_Delete(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("fr"))
			response=File_Read.file_Read(document, ran_con, absolute_path_prefix);
	
		else if(root_name.equals("fw"))
			response=File_Write.file_Write(document, ran_con, absolute_path_prefix);
		else if(root_name.equals("fa"))
			response=File_GetProperty.file_GetProperty(document, ran_con, absolute_path_prefix);
		return response;
	}
	
	public static void main(String[] args)
	{
		String absolute_path_prefix="D:\\all_app\\";
		String sub_dir="jack";
		String sub_file="jack\\box.txt";
		
		String st1="admin_a";
		String psw1="123";
		String admin_rcn="713";
		
		String st2="app_a";
		String psw2="111";
		String app_rcn="513";
		String app_absolute_path=absolute_path_prefix+st2;
		String group9="group10";
//		String source="<a><u>"+st1+"</u><p>"+psw1+"</p></a>";//用户添加(admin用户)
//		String source="<a><u>"+st2+"</u><p>"+psw2+"</p></a>";//用户添加(app用户)
		
//	    String source="<v><u>"+st2+"</u><p>"+psw2+"</p></v>";//用户认证
//		String source="<c><s>"+app_rcn+"</s></c>";//用户会话认证
		

//        String source="<m><si>713</si><p>555</p></m>";//用户密码修改
//		String source="<d><si>713</si><u>gla</u></d>";//用户删除
		
 //       String source="<ga><si>"+admin_rcn+"</si><g>"+group9+"</g></ga>";//用户组添加	
//       String source="<gd><si>"+admin_rcn+"</si><g>"+group9+"</g></gd>";//用户组删除
//		  String source="<gl><si>"+admin_rcn+"</si></gl>";//用户组枚举
//        String source="<ug><si>"+admin_rcn+"</si><u>admin</u></ug>";//用户的组属性枚举
//       String source="<gul><si>"+admin_rcn+"</si><g>admin</g></gul>";//用户组内的用户名枚举
//        String source="<gua><si>"+admin_rcn+"</si><u>app_1</u><g>group9</g></gua>";//用户关联到用户组(限于除app用户之外的用户)
//		 String source="<gur><si>"+admin_rcn+"</si><u>app_1</u><g>group9</g></gur>";//用户取消到用户组的关联(限于除app用户之外的用户)
//		String source="<mul><si>"+admin_rcn+"</si></mul>";//枚举app用户
//		String source="<mua><si>"+admin_rcn+"</si><u>"+st2+"</u></mua>";//添加app用户
//		String source="<mud><si>"+admin_rcn+"</si><u>"+st2+"</u></mud>";//删除app用户
//		String source="<mur><si>"+admin_rcn+"</si><u>"+st2+"</u></mur>";//读取app空间配额
//		String source="<mus><si>"+admin_rcn+"</si><u>"+st2+"</u><q>500</q></mus>";//设置app空间配额
//		String source="<l><si>"+app_rcn+"</si><p>"+st2+"</p></l>";//枚举app目录
//		String source="<md><si>"+app_rcn+"</si><p>"+sub_dir+"</p></md>";//添加app子目录
//		String source="<r><si>"+app_rcn+"</si><p>"+sub_dir+"</p></r>";//删除app子目录
//		String source="<e><si>"+app_rcn+"</si><p>"+sub_file+"</p></e>";//测试app中某文件是否存在
//		String source="<df><si>"+app_rcn+"</si><p>"+sub_file+"</p></df>";//删除app子文件
//		String source="<fr><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>88</b><e>150</e></fr>";//读取app子文件
//		String source="<cf><si>"+app_rcn+"</si><p>"+sub_file+"</p></cf>";//新建app子文件
//  	    String source="<fw><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>15</b><d><![CDATA[xxxabcdefghlaallalalalalala]]></d></fw>";//写入app子文件
//		String source="<fa><si>"+app_rcn+"</si><p>"+sub_dir+"</p></fa>";//获取文件或目录属性
		String source="<fa><si>"+app_rcn+"</si><p>"+sub_file+"</p></fa>";//获取文件或目录属性
		
		DynamicTable_RCL ran_con=new DynamicTable_RCL();
//		System.out.println(ran_con.get_Size());
		ran_con.insert_RCL(new DynamicTable_RCL_Item("713","admin_a","3600000"));
		ran_con.insert_RCL(new DynamicTable_RCL_Item("513","app_a","3600000"));
//		System.out.println(ran_con.get_Size());
		System.out.println(user_Authenticate_Test(source,ran_con));		 
		
	}

}
