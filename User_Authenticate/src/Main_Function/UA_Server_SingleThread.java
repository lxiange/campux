package Main_Function;
import java.io.*; 
import java.net.*; 
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import Auxiliary_Function.Connect_DynamicTable_RCL;
import Auxiliary_Function.DynamicTable_RCL;

public class UA_Server_SingleThread  
{
	
	public static void ua_Server()
	{
		 try
		 {
			 DynamicTable_RCL ran_con=Connect_DynamicTable_RCL.create__DynamicTable_RCL();
			 /**
			 * Use SSL socket -- by yuy
			 */
			 int port = 2501;
				SSLServerSocket server = null;
				try{
					String keyFile = "serverkey-campuxssl.jks";
			        String keyFilePass = "_bizdata";
			        String keyPass = "_bizdata";
			        KeyStore ks = KeyStore.getInstance("JKS");
			        ks.load(new FileInputStream(keyFile), keyFilePass.toCharArray()); 
			        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); 
			        kmf.init(ks,keyPass.toCharArray());
			        SSLContext sslc = SSLContext.getInstance("SSLv3"); 
			        sslc.init(kmf.getKeyManagers(), null, null); 
			        SSLServerSocketFactory sslserversocketfactory = sslc.getServerSocketFactory(); 
			
			        
			        server = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
				}catch(Exception exc){
					exc.printStackTrace();
					return;
				}
			 
			 /*ServerSocket server=new ServerSocket(2233);                //����һ��������(ServerSocket��)
			  *    
			  */
			 Socket client=server.accept();                             //����һ����ϵͨ��(Socket��) 
		     BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));  //����һ��Socket��������� 
	         PrintWriter out=new PrintWriter(client.getOutputStream());   //����һ��Socket�������� 
	         while(true)
			  {                                               //��������
			    String str=in.readLine(); 
			    System.out.println(str); 
			    String response=User_Authenticate_Test.user_Authenticate_Test(str,ran_con);
			    System.out.println(response);
			    out.println(response); 
			    out.flush(); 
			  } 
//	         client.close(); 
		 }
		 catch(IOException e){e.printStackTrace();}
		  
		 
		  
	}
	public static void main(String[] args)
	{
		ua_Server();
	}

}
