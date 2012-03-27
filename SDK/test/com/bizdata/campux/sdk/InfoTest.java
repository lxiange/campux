package com.bizdata.campux.sdk;

/*
 * Copyright (C) 2012 Nanjing Bizdata-infotech co., ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.List;
import java.io.File;
import org.junit.Ignore;
import com.bizdata.campux.sdk.InfoMessage;
import com.bizdata.campux.sdk.Info;
import com.bizdata.campux.sdk.User;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.FileInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuy
 */
public class InfoTest {
    
    public InfoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    //@Ignore
    @Test
    public void testFriendAddListDel() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("001221154", "123456");
        assertTrue(suc);
        
        Info f = new Info(usr);
        //f.infoRoomAdd("D_Computer");
        List<String> list = f.infoRoomRead();  
        for(String room:list)
            System.out.println(room);
        
        //suc = f.__infoPublish("testroom", "testcontent");
        //assertTrue(suc);
        
        InfoMessage[] msgs = f.infoUpdate();
        for(InfoMessage msg:msgs){
            System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
        }
    }
    @Ignore
    @Test
    public void testInfo() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("system_service", "sysuser_bizdata");
        assertTrue(suc);
        
        Info f = new Info(usr);
        
        suc = usr.setUserVariable("UserName", "系统发布");
        assertTrue(suc);
        
        File iconfile =  new File("c:/usr/campux/icon.jpg");
        FileInputStream is = new FileInputStream(iconfile);
        byte[] jpg = new byte[(int)iconfile.length()];
        is.read(jpg);
        is.close();
        String jpgstr = DatatypeConverter.printBase64Binary(jpg);
        suc = usr.setUserVariable("UserPhoto", jpgstr);
        assertTrue(suc);
        //f.infoRoomAdd("testroom");
        
        String testcontent1="[转载] 代发：中国太平洋保险集团招收IT培训生\n" + 
                "【 以下文字转载自 JobAndWork 讨论区 】\n【 原文由 beyoself 所发表 】\n\n\n中国太平洋保险公司招收IT培训生啦，请符合条件的同学们到官网上投递简历，同时将简\n历投递至liuzh@cpic.com.cn，注明来自南大BBS（说不定有师兄师姐们举荐哦^_^）\n\nhttp://job.cpic.com.cn/gapportal/#\n\n详情如下：\n\n-----------------------------------------------------------------------------\n\n根据公司IT战略发展，中国太平洋保险(集团)股份有限公司面向2012年应届毕业生开展校\n园招聘，现将有关事项公告如下：\n\n一、  招聘岗位及人数\n\n      IT培训生岗：分为开发，测试，运维及技术支持，信息安全，项目管理，需求管理\n六个方向，本次计划招录60人；\n\n二、  招聘对象\n\n    全日制普通高等院校2012届本科及以上毕业生。\n\n三、  招聘流程\n\n    1.  简历投递时间：2012年2月－3月\n    2.  简历筛选阶段：2012年2月－3月\n    3.  测评笔试阶段：2012年3月\n    4.  专业面试阶段：2012年3月中旬\n    5.  体检录用阶段：2012年3月下旬\n    （招聘进程时间以具体通知为准）\n\n四、  简历投递渠道\n\n    本次简历投递通过公司官网、前程无忧进行在线投递。\n\n     官网地址：http://job.cpic.com.cn/gapportal/#\n\n     同时请将简历发给liuzh@cpic.com.cn，注明南大BBS。\n\n五、  联系方式（有任何疑问请联系）\n\n      联系人：黄老师 联系电话：021-33961718 邮箱：huangxiaowei-002@cpic.com.cn\n      \n\n岗位、工作职责：\n\n1、应用维护岗\n   协助应用系统日常运维管理，故障诊断和问题响应；\n   参与用户应用情况分析，运行监控等。\n\n2、需求分析岗\n   参与业务流程的整合，协助上级岗位从IT的角度来影响业务规划；\n   参与IT部门与业务部门的沟通过程，建立IT和业务之间的信任关系；\n   为所服务的业务部门提供基础咨询。\n\n3、安全管理岗\n   在系统开发周期中承担信息安全基础工作；\n   参与执行信息安全审查、风险评估、推行信息安全规范；\n   协助进行信息安全审计并提供改善方案。\n\n4、项目管理岗\n   协助审核所负责项目的立项和验收（包括阶段验收）工作；\n   协助协调所负责项目开发所需的技术和人力资源，参与外部资源、技术服务等采购相关\n工作，协助组织完成所负责项目的技术方案评审；\n   协助项目风险审查，对规避方案和应急方案提出建议，参与监督项目规避方案或应急方\n案的执行；\n   参与监督所负责项目的实施过程，(并)协助撰写实施情况报告。\n\n5、软件测试岗\n   协助制定软件开发质量计划、质量保证、质量控制的管理规定；\n   根据软件设计要求制定项目质量计划和质量标准，参与软件测试工作，编写测试报告；\n   关注系统开发中的质量问题，及时与项目经理沟通，协助开展质量管理培训。\n\n6、软件开发岗\n   协助业务系统开发，包括需求分析、系统设计、编程和测试等；\n   协助制定与业务系统相关的数据接口标准并参与开发。\n\n任职资格：\n1、2012届计算机、软件、电子、通信、自动化等相关专业全日本科及以上毕业生；\n2、扎实的专业基础能力，掌握计算机编程语言，对面向过程或面向对象软件开发有一定认\n识；\n3、工作有热情、积极，学习能力强，具有一定的创造力，具有较好的沟通及协作能力，能\n承受一定的压力；\n4、英语四级及以上，能熟练阅读及翻译相关技术文档；\n5、具有金融行业IT实习经验者优先考虑。\n\n\n欢迎更多的同学加入我们CPIC，在这里期待着大家的到来！";
        String testcontent2 = "招聘！南京大学创新创业学院学生助理\n" + "  南京大学纽约理工大创新创业学院招聘学生助理：4名\n职位基本要求： \n1：有创新创业的思想追求，对创新创业有激情。\n2：研究生在读。头脑灵活优秀的本科生欢迎。\n3：能够承受较强的工作压力，做事是稳重有条理。\n4、表达能力强，思路敏捷，有团队合作精神。\n5、应届毕业生实习合格后可留用。\n6：工作认真，按时出勤，能够协助本院举办各类活动。\n7：负责向学校或系传达里各种教学活动的通知，完成系里临时交办的其它工作。\n薪水按照南大勤工俭学的标准。\n联系人：罗老师\n联系方式：18691885506\n邮箱：nju_susanl@yahoo.cn";
        String testcontent3 = "活动经费筹集情况新进展【2012/02/20新】\n" + "在08级各班班长的协助下，目前已筹集毕业活动经费共：41300元\n\n具体各班情况如下：\n   1班： 9700元               负责人： 丛浩\n   2班： 11400元              负责人： 李强强\n   3班： 8600元               负责人： 杨名阳\n   4班： 11600元              负责人： 张超\n\n由于上学期各种原因钱未能及时到位，现在基本上到位了，只剩一下一些因为各种原因还\n未能筹到。\n\n现在离毕业只剩下短短几个月的时间了，各小组可以开始进入工作状态了，祝大家有个充\n满快乐回忆的一学期。\n\n                                                        毕业活动小组\n                                                        \n                                                        2012-2-20\n--";
                
        suc = f.__infoPublish("testroom", testcontent1);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent2);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent3);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent1);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent2);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent3);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent1);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent2);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent3);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent1);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent2);
        assertTrue(suc);
        suc = f.__infoPublish("testroom", testcontent3);
        assertTrue(suc);
        
        InfoMessage[] msgs = f.infoUpdate();
        for(InfoMessage msg:msgs){
            System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
        }
    }
}
