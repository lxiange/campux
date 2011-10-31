package Main_Function;
/*
 * 主程序2: 更新一个discussion，或全部discussion(即全部subforum)
 * 
 * 
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Get_All_Subforum_Name.Lily_Get_AllSubForumName;

public class Update_OneDiscussion
{
	public static void update_OneDiscussion(String discussion_name)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		ArrayList<String> all_subforum=Lily_Get_AllSubForumName.lily_Get_SubForumName(discussion_name);
		String subforum_name="";
		int new_page=0;
		int page_increment=0;
		for(int i=0;i<all_subforum.size();i++)
		{
			subforum_name=all_subforum.get(i);
			Update_OneSubforum.update_OneSubforum(subforum_name);
			Thread.sleep(1000);
		}
	}

	public static void main(String[] args)throws IOException,ClassNotFoundException,SQLException,InterruptedException
	{
		String discussion="nanjingdaxue";	
//		String discussion="all";
		ArrayList<String> all_subforum=Lily_Get_AllSubForumName.lily_Get_SubForumName(discussion);


		update_OneDiscussion(discussion);
	}
}
