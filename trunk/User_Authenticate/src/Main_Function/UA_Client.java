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
			server=new Socket(InetAddress.getLocalHost(),2233);             //��ͨ�����ӵ�ĳ��Ŀ�ĵ�
	        BufferedReader in=new BufferedReader(new InputStreamReader(server.getInputStream()));  //����һ��Sockte���������
	  	    PrintWriter out=new PrintWriter(server.getOutputStream());                             //����һ��Sockte��������
    	    BufferedReader wt=new BufferedReader(new InputStreamReader(System.in));                //����һ��System���������
		    

//           String source="<a><u>gl</u><p>778999</p></a>";//�û����
//	String source="<v><u>app_1</u><p>123456</p></v>";//�û���֤2

//	        String source="<v><u>app_a</u><p>111</p></v>";//�û���֤1
//	        String source="<c><s>713</s></c>";//�û��Ự��֤
//        String source="<m><si>713</si><p>888</p></m>";//�û������޸�
//		String source="<d><si>713</si><u>gl</u></d>";//�û�ɾ��
//       String source="<ga><si>713</si><g>group12</g></ga>";//�û������	
//        String source="<gd><si>713</si><g>group12</g></gd>";//�û���ɾ��
//		  String source="<gl><si>713</si></gl>";//�û���ö��
//        String source="<ug><si>713</si><u>music</u></ug>";//�û���������ö��
//        String source="<gul><si>713</si><g>app</g></gul>";//�û����ڵ��û���ö��
//       String source="<gua><si>713</si><u>music</u><g>app</g></gua>";//�û��������û���
//		 String source="<gur><si>713</si><u>music</u><g>admin</g></gur>";//�û�ȡ�����û���Ĺ���

         
         
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
		
		
//		String source="<mul><si>"+admin_rcn+"</si></mul>";//ö��app�û�
//		String source="<mua><si>"+admin_rcn+"</si><u>"+st2+"</u></mua>";//���app�û�
//		String source="<mud><si>"+admin_rcn+"</si><u>"+st2+"</u></mud>";//ɾ��app�û�
//		String source="<mur><si>"+admin_rcn+"</si><u>"+st2+"</u></mur>";//��ȡapp�ռ����
//		String source="<mus><si>"+admin_rcn+"</si><u>"+st2+"</u><q>500</q></mus>";//����app�ռ����
//         String source="<l><si>"+app_rcn+"</si><p>"+st2+"</p></l>";//ö��appĿ¼
//		String source="<md><si>"+app_rcn+"</si><p>"+sub_dir+"</p></md>";//���app��Ŀ¼
//		String source="<r><si>"+app_rcn+"</si><p>"+sub_dir+"</p></r>";//ɾ��app��Ŀ¼
//		String source="<e><si>"+app_rcn+"</si><p>"+sub_file+"</p></e>";//����app��ĳ�ļ��Ƿ����
//		String source="<df><si>"+app_rcn+"</si><p>"+sub_file+"</p></df>";//ɾ��app���ļ�
//		String source="<fr><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>0</b><e>150</e></fr>";//��ȡapp���ļ�
//		String source="<cf><si>"+app_rcn+"</si><p>"+sub_file+"</p></cf>";//�½�app���ļ�
//  	    String source="<fw><si>"+app_rcn+"</si><p>"+sub_file+"</p><b>15</b><d><![CDATA[xxxabcdefghlaallalalalalala]]></d></fw>";//д��app���ļ�
//		String source="<fa><si>"+app_rcn+"</si><p>"+sub_dir+"</p></fa>";//��ȡ�ļ���Ŀ¼����
		String source="<fa><si>"+app_rcn+"</si><p>"+sub_file+"</p></fa>";//��ȡ�ļ���Ŀ¼����

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
