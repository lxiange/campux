/*
 *作用： 获取某讨论区内所有subforum的url
 * 
 */

package Get_All_Subforum_Name;
import java.util.*;
import java.io.*;


public class Lily_Get_AllSubForumName 
{
	//根据输入的讨论区名称，得到其内部的各子论坛名称
	public static ArrayList<String> lily_Get_SubForumName(String classified_subforum)
	{
		ArrayList<String> all_subforum=new ArrayList<String>();
		String prefix="http://bbs.nju.edu.cn/bbsdoc?board=";
		
		if(classified_subforum=="benzhanxitong")
			all_subforum=lily_Get_OneSubForumName_benzhanxitong();
		else if(classified_subforum=="nanjingdaxue")
			all_subforum=lily_Get_OneSubForumName_nanjingdaxue();
		else if(classified_subforum=="xiangqingxiaoyi")
			all_subforum=lily_Get_OneSubForumName_xiangqingxiaoyi();
		else if(classified_subforum=="diannaojishu")
			all_subforum=lily_Get_OneSubForumName_diannaojishu();
		else if(classified_subforum=="xueshukexue")
			all_subforum=lily_Get_OneSubForumName_xueshukexue();
		else if(classified_subforum=="wenhuayishu")
			all_subforum=lily_Get_OneSubForumName_wenhuayishu();
		else if(classified_subforum=="tiyuyule")
			all_subforum=lily_Get_OneSubForumName_tiyuyule();
		else if(classified_subforum=="ganxingxiuxian")
			all_subforum=lily_Get_OneSubForumName_ganxingxiuxian();
		else if(classified_subforum=="xinwenxinxi")
			all_subforum=lily_Get_OneSubForumName_xinwenxinxi();
		else if(classified_subforum=="baiheguangjiao")
			all_subforum=lily_Get_OneSubForumName_baiheguangjiao();
		else if(classified_subforum=="xiaowuxinxiang")
			all_subforum=lily_Get_OneSubForumName_xiaowuxinxiang();
		else if(classified_subforum=="sheququnti")
			all_subforum=lily_Get_OneSubForumName_sheququnti();
		else if(classified_subforum=="lenmentaolunqu")
			all_subforum=lily_Get_OneSubForumName_lenmentaolunqu();
		else if(classified_subforum=="all")
		{
			ArrayList<String> temp_arr=new ArrayList<String>();
			all_subforum=lily_Get_OneSubForumName_benzhanxitong();
			temp_arr=lily_Get_OneSubForumName_nanjingdaxue();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_xiangqingxiaoyi();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_diannaojishu();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_xueshukexue();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_wenhuayishu();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_tiyuyule();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_ganxingxiuxian();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_xinwenxinxi();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_baiheguangjiao();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_xiaowuxinxiang();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_sheququnti();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
			temp_arr=lily_Get_OneSubForumName_lenmentaolunqu();
			for(int i=0;i<temp_arr.size();i++) all_subforum.add(temp_arr.get(i));
		}
		else 
		{
			System.out.println("No such a discussion, wrong input!");
			System.exit(0);
		}
		for(int i=0;i<all_subforum.size();i++)
			all_subforum.set(i,prefix+all_subforum.get(i));
		return all_subforum;
	}
	
	
	
	//取得13个讨论区的字符串序列
	public static ArrayList<String> lily_Get_ClassifiedSubForumName()
	{
		ArrayList<String> classified_subforum=new ArrayList<String>();
		classified_subforum.add("benzhanxitong");
		classified_subforum.add("nanjingdaxue");
		classified_subforum.add("xiangqingxiaoyi");
		classified_subforum.add("diannaojishu");
		classified_subforum.add("xueshukexue");
		classified_subforum.add("wenhuayishu");
		classified_subforum.add("tiyuyule");
		classified_subforum.add("ganxingxiuxian");
		classified_subforum.add("xinwenxinxi");
		classified_subforum.add("baiheguangjiao");
		classified_subforum.add("xiaowuxinxiang");
		classified_subforum.add("sheququnti");
		classified_subforum.add("lenmentaolunqu");
				
		return classified_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_lenmentaolunqu()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"NJUPT","NJU_TIC","S_SPM","Mediastudy","Sudoku","HotZone","Magic","LilyFestival","SJTU","THU"};
		String[] arr_b_2={"PKU","XJTU","NKU","Radio","DiscZone","Human","Marketing_Zone","NJUMUN","Nirvana","CFD"};
		String[] arr_b_3={"SiGuo","MSTClub","ZSU","Olympics","YangtzeDelta","F_Literature","Microwave","Esperanto","SIFE_NJU","Boxing_Fight"};
		String[] arr_b_4={"LSCMA","Bowling","SportsNews","NUAA","OurCustom","NJUT","Sculpture","HaiNan","LZU","E_Business"};
		String[] arr_b_5={"NJAU","PersonalCorpus","Aerospace","LifeLeague","Borland","Cross_Strait","Education","US_JP_Research","NJMU","NZY"};		
	
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);			
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_sheququnti()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"Association_Union","CCP","Chorus","FEA","Folk_Music","GAFA","GEC","GreenEarth","GuQin","LifeLeague"};
		String[] arr_b_2={"LSCMA","Marketing_Zone","MSTClub","NJU_Graduate","NJU_TIC","NJU_Youth","NJU_zhixing","NJUMUN","Orchestra","ReadyForJob"};
		String[] arr_b_3={"RedCross","SCDA","SIFE_NJU","SiYuan","SPA","StoneCity","TianJian","Volunteer","xinhongji","YangTaiChi"};
		
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);						
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_xiaowuxinxiang()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"bulletin","M_Academic","M_CMHER","M_Gonghui","M_Graduate","M_GraduateUnion","M_Guard","M_Hospital","M_Job","M_League"};
		String[] arr_b_2={"M_Library","M_Logistic","M_NIC","M_Student","M_StudentUnion","V_Suggestions"};		
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);					
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_baiheguangjiao()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"AntiMalfeasant","AntiRumor","BodilyForm","Guilt","HomoSky","Nature","Peer_Edu","Smoking","Vegetarian","West_Volunteer"};		
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);							
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_xinwenxinxi()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"Abroad","Agent","Britain","ChinaNews","Civil_Servant","DigiMusic","DigitalWorld","DiscZone","ExchangeStudent","FleaMarket"};
		String[] arr_b_2={"GoToUniversity","GRE_TOEFL","IELTS","Intern","ITExam","JobAndWork","JobExpress","KaoYan","LostToFind","NetResources"};
		String[] arr_b_3={"NJ_HOUSE","PartTimeJob","RealEstate","SportsNews","Stock","SuperGirls","Traffic_Info","Train","WorldNews","Zjl_Online"};
		
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);						
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_ganxingxiuxian()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"AlbumShow","Astrology","Bless","Boys","Collections","Dream","Drink","Esquire","FamilyLife","Fashion"};
		String[] arr_b_2={"Feelings","FOOD","Friendship","Girls","GreatTurn","HandiCraft","Hometown","ID","Joke","KaraOK"};
		String[] arr_b_3={"Korea","Life","Love","Memory","Model_Space","NanJing","OfficeStaff","Party_of_Killer","PetsEden","Pictures"};
		String[] arr_b_4={"Radio","Riddle","RoomChating","Shopping","ShortMessage","Single","Travel","WarAndPeace"};			
	
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);					
		return one_subforum;
	}
	public static ArrayList<String> lily_Get_OneSubForumName_tiyuyule()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"AutoSpeed","Badminton","Basketball","Billiards","BNGames","BoardGame","Bowling","Boxing_Fight","Bridge","Chess"};
		String[] arr_b_2={"ChinaFootball","Cube","Dance","DotaAllstars","F1","Fishing","Fitness"};//,"Cycling","E_Sports","GJ"
		String[] arr_b_3={"MaJiang","OLGames","PCGames","Renju","RunForEver","SiGuo","SJ"};//"JSSports",(数据库中数据重复但是无法删除),"MudLife","Olympics"
		String[] arr_b_4={"Skating","Sudoku","Swimming","TableTennis","Taekwondo","Tennis","TVGames","Volleyball","WebGames","WeiQi"};
		String[] arr_b_5={"WesternstyleChess","WorldFootball","WuShu","YOGA"};		
	
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);			
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_wenhuayishu()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"7th_Art","AD_Art","Archaism","Archeology","ASCIIArt","Budaixi","Calligraphy","ChunQiu_ZhanGuo","Classical_Poem","ClassicalCulture"};
		String[] arr_b_2={"ClassicalMusic","Comic","Couplet","Cross_Strait","CrossShow","Debate","Detective","Discovery","Drama","Drawing"};
		String[] arr_b_3={"DV_Studio","ElectronicMusic","F_Literature","FairyTale",/*"Emprise",,"Fiction"（此子论坛residue有问题）*/"Fantasy","Flowers","Folk_Country","Guitar"};
		String[] arr_b_4={"HiFi","J_Ent","Jazz_Blues","Magic","Marvel","Modern_Poem","Movies","Musical","Mythlegend","Names"};
		String[] arr_b_5={"NewAge","Novel","OurCustom","Photography","Piano","QuYi","Reading","RockMusic","SanGuo"};//,"PopMusic"
		String[] arr_b_6={"Sculpture","Seasons","Shows","StoneStory","Story","TV","ZhuangXiu"};
	
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);
		for(int i=0;i<arr_b_6.length;i++)
			one_subforum.add(arr_b_6[i]);		
		return one_subforum;
	}
	
	
	public static ArrayList<String> lily_Get_OneSubForumName_xueshukexue()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"1937_12_13","Actuary","Aerospace","America","AtmosphereSci","CFD","Chemistry","Chrematistics","Christianity","Consultant"};
		String[] arr_b_2={"CPA","Deutsch","E_Business","EarthSciences","Economics","Education","EEtechnology","English","Esperanto","Finance"};
		String[] arr_b_3={"Forum","French","Geography","GIS","GreeceRome","History","HotZone","Human","Info_Manage","IR"};
		String[] arr_b_4={"Japanese","Journalism","Law","LectureHall","LifeScience","Linguistics","Management","Mathematics","MathTools","Mediastudy"};
		String[] arr_b_5={"Medicine","Microwave","Military","NanoST","People","Philosophy","Physics","Politics","Psychology","Russia"};
		String[] arr_b_6={"Spanish","Taiwan","TCM","Theoretical_CS","Thesis","Tibet","UrbanPlan","US_JP_Research","Wisdom","YangtzeDelta"};
	
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);
		for(int i=0;i<arr_b_6.length;i++)
			one_subforum.add(arr_b_6[i]);		
		return one_subforum;
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_diannaojishu()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();		
		String[] arr_b_1={"AI","Algorithm","Apple","BBSDev","BitTorrent","Borland","Computer_ABC","CPlusPlus","Database","DotNet"};
		String[] arr_b_2={"Embedded","Flash","Fortran","Graphics","Hacker","Hardware","HPC","Image","ITClub","Java"};
		String[] arr_b_3={"LilyStudio","LinuxUnix","Mobile","MSWindows","Network","NoteBook","Program","Python","SoftEng","Software"};
		String[] arr_b_4={"TeX","VC","Virus","WebDesign"};
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);	
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		return one_subforum;		
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_xiangqingxiaoyi()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		String[] arr_b_1={"AnHui","BeiJing","CAS","ChangZhou","CPU","CUG","CUMT","DongBei","FDU","FuJian"};
		String[] arr_b_2={"GuangDong","GuangXi","HaiNan","HeBei","HeNan","HHU","HKU","HuaiAn","HuBei","HuNan"};
		String[] arr_b_3={"Inner_Mongolia","JiangXi","JLU","LianYunGang","LZU","NanTong","NJAU","NJMU","NJNU","NJUPT"};
		String[] arr_b_4={"NJUT","NKU","NUAA","NUST","NZY","OUC","Overseas","PKU","SE_Association","SEU"};
		String[] arr_b_5={"ShanDong","ShangHai","ShanXi","SJTU","SuQian","SuZhou","TaiZhou","THU","TianJin","USTC"};
		String[] arr_b_6={"WHU","WuXi","XiBei","XiNan","XinJiang","XJTU","XMU","XuZhou","YanCheng","YangZhou"};
		String[] arr_b_7={"ZheJiang","ZhenJiang","ZJU","ZSU"};
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);
		for(int i=0;i<arr_b_6.length;i++)
			one_subforum.add(arr_b_6[i]);
		for(int i=0;i<arr_b_7.length;i++)
			one_subforum.add(arr_b_7[i]);
		return one_subforum;
	}
	public static ArrayList<String> lily_Get_OneSubForumName_benzhanxitong()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();		
		String[] arr_b_1={"Advice","Announce","BBSHelp","bbslists","Blog","BMManager","Board","BoardManage","Chat","Complain"};
		String[] arr_b_2={"EnglishCorner","ExcellentBM","LilyDigest","LilyFestival","LilyLinks","newcomers","Nirvana","notepad","Ourselves","Paint"};
		String[] arr_b_3={"PersonalCorpus","sysop","test","vote","VoteBoard"};		
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);			
		return one_subforum;		
	}
	
	public static ArrayList<String> lily_Get_OneSubForumName_nanjingdaxue()
	{
		ArrayList<String> one_subforum=new ArrayList<String>();
		
		String[] arr_b_1={"AcademicReport","C_Inter","CCAS","Contest","D_Astronomy","D_Chinese","D_Computer","D_EarthScience","D_EE","D_History",};
		String[] arr_b_2={"D_Information","D_Materials","D_Maths","D_Philosophy","D_Physics","D_SocialSec","DII","IFA_IS","IFIS","MARC"};
		String[] arr_b_3={"NJU_HOME","NJUExpress","Postdoc","PuKouCampus","S_Atmosphere","S_Business","S_Chemistry","S_Education","S_Environment","S_ForeignLang"};
		String[] arr_b_4={"S_Geography","S_GOV","S_Graduate","S_Journalism","S_Law","S_LifeScience","S_Medicine","S_MSE","S_Sociology","S_Software"};
		String[] arr_b_5={"S_SPM","SAU"};
		for(int i=0;i<arr_b_1.length;i++)
			one_subforum.add(arr_b_1[i]);
		for(int i=0;i<arr_b_2.length;i++)
			one_subforum.add(arr_b_2[i]);
		for(int i=0;i<arr_b_3.length;i++)
			one_subforum.add(arr_b_3[i]);
		for(int i=0;i<arr_b_4.length;i++)
			one_subforum.add(arr_b_4[i]);
		for(int i=0;i<arr_b_5.length;i++)
			one_subforum.add(arr_b_5[i]);
		return one_subforum;		
	}
		
	public static void main(String[] args)
	{
		ArrayList<String> arr_a=lily_Get_ClassifiedSubForumName();
//		System.out.println(arr_a.size());
//		ArrayList<String> arr_b=lily_Get_SubForumName("aall");
		ArrayList<String> arr_b=lily_Get_SubForumName("all");
		System.out.println(arr_b.size());
		
		for(int i=0;i<arr_b.size();i++)
			System.out.println(arr_b.get(i));
		System.out.println(arr_b.get(arr_b.size()-1));
	}

}
