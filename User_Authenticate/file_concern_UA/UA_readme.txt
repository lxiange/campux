�������ƣ�User_Authenticate
�������ã����ա��û���֤��洢����
���룺ĳ�����Կͻ��˵�xml
�����������������Ľ��xml���������������ݶ����ݿ�������ռ��е��ļ�������Ӧ�޸�



����ṹ��
���������6������
General_Function     Ϊͨ�ú����������ڱ�����
Auxiliary_Function   Ϊ���������������������ݿ⣬������̬����
User_Authenticate    ������xmlΪ��ͨ�û�����ʱ��ʹ�ô˰��д��룬��Ҫʵ���ˡ��û���֤��洢���񡷵ڶ����ֵĹ���
Store_Admin          ������xmlΪadmin����ʱ��ʹ�ô˰��д��룬��Ҫʵ���ˡ��û���֤��洢���񡷵�������ǰ��Ĺ���
Store_Use            ������xmlΪapp����ʱ��ʹ�ô˰��д��룬��Ҫʵ���ˡ��û���֤��洢���񡷵������ֺ��Ĺ��� 
Main_Function        Ϊ��������Ϊ�������еķ���������ʱ��ֱ������"UA_Server_MultiThread.java"���ɡ�
                     "UA_Client.java"Ϊ�ͻ��˳���ʾ�����˿ںſ����ڴ������ļ����޸ģ�����һ�¼��ɡ�


���DB table��
DB��ά��5����(ǰ4������Լ����ϵ����"UA_DB.jpg")��
user_psw      ����(user_name,  user_psw,  user_status)���洢�û��������롢״̬�������û�(admin/app/nomal user)�������ڴ˱�������Ӧ��˴��������û����
              user_status���������뱣����̬����ȡ"0"��"1"��"0"��ʾ���Է��ʣ�"1"��ʾ�ܾ����ʡ�һ����"0"�������������������3�κ���"1"��5min��ָ�Ϊ"0"��

workgourp     ����(group_no, group_name)���洢�û���š��û���������Ŵ�"1"��ʼ�������Լ����ã�Ҳ�����Զ����ɡ��й��û������Ĳ���һ�㶼����Ŵ��档

user_group    ����(user_group_no, user_name, group_no)���洢 (�û�����)�ţ��û�������š���ĳ�û�u���뵽ĳ��gʱ�����ڴ˱�������һ��(u_g_no, u, g)������u_g_no����
              �Լ����ã�Ҳ�����Զ����ɡ�
              ��group_no��"app"ʱ����Ҫ����������ڱ�app_store������(u_g_no, u, space_allocated, 0)��(�����������ܱ�app_store��)�����������ռ��Ŀ¼��(��"D:\all_app\")
              �н�����Ϊ"u"�Ŀ��ļ��У���Ϊ��app������ռ䡣
              ���ӱ���ɾ��ĳ��ʱ��ɾ������ͬ�ϡ�

app_store     ����(user_group_no, app_name, space_allocated, space_used)����ʾapp�š�app���ơ�Ԥ����Ŀռ�(Byte)�����ÿռ�(Byte)��ÿ��app��Ӧһ�����ͬʱ��Ӧ
              һ�������ռ��Ŀ¼��(��"D:\all_app\")����Ϊapp_name���ļ��У���app�û������ڴ��ļ����ڽ�����ز�����

wrong_set     ����(wrong_no, wrong_reason)����ʾ������롢����ԭ�����ڳ���ʱ�����Ӧ���ݡ�

(ע�⣺����workgourp����(1��app)���user_group���е���(8, zhao, 1)��app_store���е���(8,zhao,100000, 0)���ļ���"D:\all_app\zhao"��ͬʱ���ɡ�ͬʱɾ���ġ�)



�������к��ڴ���ά��������̬����
ran_con      ����(ran_con_num, user_name, active_time)����ʾ��ͨ���Ự��֤���û�����active_time�����ż�ʱ���ݼ���60min���Ϊ0���Զ��ӱ���ɾ������û���Ҫ���µ�¼��
psw_pro      ����(user_name, wrong_psw_time, active_time)����ʾ�����������������û���wrong_psw_time��ʾ�����������������Ϊ"3"ʱ����user_psw��user_name������user_stausΪ"1"��
             ��active_time�����ż�ʱ���ݼ���5min���Ϊ0���Զ��ӱ���ɾ���������user_psw��user_name������user_stausΪ"0"���û��ɼ������Ե�¼��