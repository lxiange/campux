package Main_Function;
import java.io.*; 
import java.net.*; 
import Auxiliary_Function.*;
import General_Function.General_Function_AboutXml.Convert_StringToXml;
import General_Function.General_Function_AboutXml.Get_ChildNodeOfRootInXml;

import java.util.*;

import org.dom4j.Document;


public class UA_Server_MultiThread 
{
	//新建空的ran_con表
	DynamicTable_RCL ran_con=Connect_DynamicTable_RCL.create__DynamicTable_RCL();	
	//新建空的psw_protection表
	DynamicTable_PP psw_pro=new DynamicTable_PP();
	 
	public static void main(String[] args)throws IOException
	{
		UA_Server_MultiThread server=new UA_Server_MultiThread();
		server.connection();
	}
	
	public void connection()throws IOException
	{
		int port=2233;
		String url="127.0.0.1";
		InetSocketAddress address=InetSocketAddress.createUnresolved(url, port);
		ServerSocket server=new ServerSocket(port);
		System.out.println("Server boot now!");
		System.out.println("Table ran_con have established!");
		//为ran_con新建计时器，每隔一段时间就扣除ran_con表中所有项的相应active_time值
		Create_TimerForRCL ran_con_timer=new Create_TimerForRCL(ran_con);
		//为psw_protection新建计时器，每隔一段时间就扣除psw_protection表中所有项的相应active_time值
		Create_TimerForPP psw_protection_timer=new Create_TimerForPP(psw_pro);
		ran_con_timer.start();
		psw_protection_timer.start();
		
		while(true)
		{
			Socket socket=server.accept();
			SocketThread connThread=new SocketThread(socket);
			connThread.start();
		}
	}

	class SocketThread extends Thread
	{
		private Socket socket;
		public SocketThread(Socket socket)
		{
			this.socket=socket;
		}
		
		public void run()
		{
			try
			{
				 BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));  
		         PrintWriter out=new PrintWriter(socket.getOutputStream()); 
		         String str=in.readLine(); 
//	  		     System.out.println(str); 
			     String response=User_Authenticate_Test.user_Authenticate_Test(str,ran_con);
//			     System.out.println(response);
			     //处理密码枚举保护
			     if(str.contains("<v>")&&response.contains("user dosen't match password"))
			     {
			    	 Document document=Convert_StringToXml.convert_StringToXml(str);
			    	 ArrayList<String> all_child=Get_ChildNodeOfRootInXml.get_AllNameOfChildNodeOfRoot(document);
			    	 String user_name_a=Get_ChildNodeOfRootInXml.get_TextOfElementInXml(document, "u");
			    	 if(psw_pro.have_User(user_name_a))
			    	 {
			    		 if(psw_pro.get_WrongPswTime(user_name_a)==1)
			    		 {
			    			 psw_pro.get_ItemByUser(user_name_a).set_WrongPswTime(2);
			    			 psw_pro.get_ItemByUser(user_name_a).set_ActiveTime("300000");
			    		 }
			    		 else if(psw_pro.get_WrongPswTime(user_name_a)==2)
			    		 {
			    			 psw_pro.get_ItemByUser(user_name_a).set_WrongPswTime(3);
			    			 psw_pro.get_ItemByUser(user_name_a).set_ActiveTime("300000");
			    			 Connect_DBTable_UP.set_Status(user_name_a, "1");
			    		 }
			    	 }
			    	 else
			    	 {
			    		 DynamicTable_PP_Item item=new DynamicTable_PP_Item(user_name_a,1,"300000");
			    		 psw_pro.insert_PP(item);
			    	 }
			         
			     }
			     
			     
			     System.out.println("Now the size of table ran_con is: "+ran_con.get_Size());
			     System.out.println(ran_con.toString());
			     System.out.println("*************************************************");
			     System.out.println();
		
			     out.println(response); 
			     out.flush(); 
			     socket.close();
		         
			}
			catch(IOException e){e.printStackTrace();}
		}
	}
}


