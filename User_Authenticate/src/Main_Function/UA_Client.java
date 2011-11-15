package Main_Function;
import java.io.*; 
import java.net.*; 
import General_Function.*;
public class UA_Client
{
	static Socket server;    
	public static void authen_Client_1()
	{
		try
		{
			server=new Socket(InetAddress.getLocalHost(),2233);             //把通道连接到某个目的地
	        BufferedReader in=new BufferedReader(new InputStreamReader(server.getInputStream()));  //定义一个Sockte类的输入流
	  	    PrintWriter out=new PrintWriter(server.getOutputStream());                             //定义一个Sockte类的输出流
    	    BufferedReader wt=new BufferedReader(new InputStreamReader(System.in));                //定义一个System类的输入流
		    

//           String source="<a><u>gl</u><p>778999</p></a>";//用户添加
//	String source="<v><u>app_1</u><p>123456</p></v>";//用户认证2

//	        String source="<v><u>app_a</u><p>111</p></v>";//用户认证1
//	        String source="<c><s>713</s></c>";//用户会话认证
//        String source="<m><si>713</si><p>888</p></m>";//用户密码修改
//		String source="<d><si>713</si><u>gl</u></d>";//用户删除
//       String source="<ga><si>713</si><g>group12</g></ga>";//用户组添加	
//        String source="<gd><si>713</si><g>group12</g></gd>";//用户组删除
//		  String source="<gl><si>713</si></gl>";//用户组枚举
//        String source="<ug><si>713</si><u>music</u></ug>";//用户的组属性枚举
//        String source="<gul><si>713</si><g>app</g></gul>";//用户组内的用户名枚举
//       String source="<gua><si>713</si><u>music</u><g>app</g></gua>";//用户关联到用户组
//		 String source="<gur><si>713</si><u>music</u><g>admin</g></gur>";//用户取消到用户组的关联

         
         
         String absolute_path_prefix="D:\\all_app\\";
		String sub_dir="jack";
		String sub_file="jack\\box.txt";
		
		String st1="admin_a";
		String psw1="123";
		String admin_rcn="713";
		
		String st2="app_1";
		String psw2="111";
		String app_rcn="465";
		String app_absolute_path=absolute_path_prefix+st2;
		String group9="group10";
		
		
//		String source="<mul><si>"+admin_rcn+"</si></mul>";//枚举app用户
//		String source="<mua><si>"+admin_rcn+"</si><u>"+st2+"</u></mua>";//添加app用户
//		String source="<mud><si>"+admin_rcn+"</si><u>"+st2+"</u></mud>";//删除app用户
//		String source="<mur><si>"+admin_rcn+"</si><u>"+st2+"</u></mur>";//读取app空间配额
//		String source="<mus><si>"+admin_rcn+"</si><u>"+st2+"</u><q>500</q></mus>";//设置app空间配额
//         String source="<l><si>"+app_rcn+"</si><p>"+st2+"</p></l>";//枚举app目录
//		String source="<md><si>"+app_rcn+"</si><p>"+sub_dir+"</p></md>";//添加app子目录
//		String source="<r><si>"+app_rcn+"</si><p>"+sub_dir+"</p></r>";//删除app子目录
//		String source="<e><si>"+app_rcn+"</si><p>"+sub_file+"</p></e>";//测试app中某文件是否存在
//		String source="<df><si>"+app_rcn+"</si><p>"+sub_file+"</p></df>";//删除app子文件
//		String source="<fr><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>0</b><e>150</e></fr>";//读取app子文件
//		String source="<cf><si>"+app_rcn+"</si><p>"+sub_file+"</p></cf>";//新建app子文件
//  	    String source="<fw><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>15</b><d><![CDATA[xxxabcdefghlaallalalalalala]]></d></fw>";//写入app子文件
//		String source="<fa><si>"+app_rcn+"</si><p>"+sub_dir+"</p></fa>";//获取文件或目录属性
		String source="<fa><si>"+app_rcn+"</si><p>"+sub_file+"</p></fa>";//获取文件或目录属性

		    System.out.println(source);
		   	out.println(source); 
		    out.flush(); 
			   
		    System.out.println(in.readLine()); 
		    while(true){}
          
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public static void main(String[] args)
	{
		authen_Client_1();
	}

}
