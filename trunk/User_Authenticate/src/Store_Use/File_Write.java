/*
 * ���ã�app�û����ļ�д��
 *precondition��ĳ��xml document
 *postcondition: ���ܴ�document��ʶ�������Ự��š��ļ�·������ʼ�ֽڡ����ݣ����ұ�ran_con���д˱�ţ���������app�飬���ȡ��Ӧ���ļ�Ƭ�Σ�������ȷ��Ӧ����ʾ��
 *               ���򷵻���Ӧ�Ĵ�����Ӧ����ʾ��
 */
package Store_Use;

import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;

import Auxiliary_Function.Connect_DBTable_AS;
import Auxiliary_Function.Connect_DBTable_UG;
import Auxiliary_Function.Connect_DBTable_WG;
import Auxiliary_Function.Connect_DBTable_WS;
import Auxiliary_Function.Create_RightResponseXml;
import Auxiliary_Function.DynamicTable_RCL;
import General_Function.Search_In_Arr;
import General_Function.General_Function_AboutFile.Connect_File;
import General_Function.General_Function_AboutFile.File_Function;
import General_Function.General_Function_AboutXml.Get_ChildNodeOfRootInXml;

public class File_Write 
{
	public static String file_Write(Document document,DynamicTable_RCL ran_con,String absolute_path_prefix)
	{
		String response="";
		String user_ran_con_label="si";
		String sub_file_label="p";
		String begin_file_label="b";
		String date_label="d";
		//root�������ӽڵ�"<si>","<p>","<b>","<d>"�����ж����ӽڵ�����
		ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
		if(all_child.size()!=4||!all_child.contains(user_ran_con_label)||!all_child.contains(sub_file_label)||!all_child.contains(begin_file_label)||!all_child.contains(date_label))
		{
			response=Connect_DBTable_WS.wrong_Response("1");
			return response;
		}
		String user_ran_con_num=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, user_ran_con_label);
		String sub_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, sub_file_label);
		String begin_file=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, begin_file_label);
		String date=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, date_label);
		
/*		//����35�����ݸ�ʽ����
		if(!date.startsWith("<![CDATA[")||!date.endsWith("]]>"))
		{
			response=Connect_DBTable_WS.wrong_Response("35");
			return response;
		}
*/		//����7������Ự���Ϊ��
		if(user_ran_con_num=="")
		{
			response=Connect_DBTable_WS.wrong_Response("7");
			return response;
		}
		//����8��ran_con�����޴�������
		if(ran_con.have_RCN(user_ran_con_num)==false)
		{
			response=Connect_DBTable_WS.wrong_Response("8");
			return response;
		}
		//����21�������Ŷ�Ӧ��user_name����app��ĳ�Ա
		String user_name_a=ran_con.get_UserByRCN(user_ran_con_num);
		ArrayList<String> groups=Connect_DBTable_UG.get_GroupsNoByUser(user_name_a);
		String group_no_a=Connect_DBTable_WG.get_NoByName("app");	
		if(Search_In_Arr.search_In_Arr(group_no_a, groups)<0)
		{
			response=Connect_DBTable_WS.wrong_Response("21");
			return response;
		}
		//����31���ļ���Ϊ��
		if(sub_file=="")
		{
			response=Connect_DBTable_WS.wrong_Response("31");
			return response;
		}
		//���ɾ���·����
		String absolute_path_of_sub_file=absolute_path_prefix+user_name_a+"\\"+sub_file;	
		File file=new File(absolute_path_of_sub_file);
	
		//����ļ������ڣ������ļ�
		if(!file.isFile())
		{
			//����34���޷��������ļ�(����ϵͳ������ԭ��)
			File apr=new File(absolute_path_prefix+user_name_a);
			if(!File_Function.create_SubFile(apr, sub_file))
			{
				response=Connect_DBTable_WS.wrong_Response("34");
				return response;
			}
		}
		String content=Connect_File.read_FromFile(absolute_path_of_sub_file);
		long len=content.length();
		int begin_index=Integer.valueOf(begin_file).intValue();  
		
		String app_user=ran_con.get_UserByRCN(user_ran_con_num);
		String space_allocated=Connect_DBTable_AS.get_AllocatedSpace(app_user);
		String space_used=Connect_DBTable_AS.get_UsedSpace(app_user);
		long space_allocated_long=Long.valueOf(space_allocated).longValue();//Byte
		long space_used_long=Long.valueOf(space_used).longValue();//Byte
		
//		System.out.println(ran_con.get_UserByRCN(user_ran_con_num));
//		System.out.println(absolute_path_of_sub_file);
//		System.out.println(len);
//		System.out.println(space_allocated);
//		System.out.println(space_used);
		//begin_index>=lengthʱ����begin_index����������׷�ӽ��ļ�
		if(begin_index>len)
		{
			long new_file_size=date.length();
//			System.out.println(new_file_size);
			//����37��app�û��ռ�����
			if(space_used_long+new_file_size>=space_allocated_long)
			{
				response=Connect_DBTable_WS.wrong_Response("37");
				return response;
			}
			//����36���޷�д���ļ�(����ϵͳ������ԭ��)
			if(!Connect_File.write_ToFileByAppend(absolute_path_of_sub_file, date))
			{
				response=Connect_DBTable_WS.wrong_Response("36");
				return response;
			}
			//д�����ݳɹ�������app_store ���еĶ�Ӧ���space_used��
//			System.out.println(Long.toString(space_used_long+new_file_size));
			Connect_DBTable_AS.set_UsedSpace(app_user, Long.toString(space_used_long+new_file_size));
		}
		//begin_index<lengthʱ����begin_index���������ݸ��ǽ��ļ�
		else
		{
			content=content.substring(0, begin_index);
			date=content+date;
			long new_file_size=date.length()-len;
			//����37��app�û��ռ�����
			if(space_used_long+new_file_size>=space_allocated_long)
			{
				response=Connect_DBTable_WS.wrong_Response("37");
				return response;
			}
			//����36���޷�д���ļ�(����ϵͳ������ԭ��)
			if(!Connect_File.write_ToFile(absolute_path_of_sub_file, date))
			{
				response=Connect_DBTable_WS.wrong_Response("36");
				return response;
			}
			//д�����ݳɹ�������app_store ���еĶ�Ӧ���space_used��
			Connect_DBTable_AS.set_UsedSpace(app_user, Long.toString(space_used_long+new_file_size));
		}
		
		response=Create_RightResponseXml.create_RightResponseXml(response);
		return response;
	
	}

}
