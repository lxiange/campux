/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bizdata.infosource;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.Info;
import com.bizdata.campux.sdk.User;
import com.bizdata.intellix.docanalysis.Bag;
import com.bizdata.intellix.docanalysis.Model;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author yuy
 * @date 2012-03-11 08:14:58
 */
public class LilyLooper extends Thread{
    protected final String f_lilyprefix = "http://bbs.nju.edu.cn/bbstdoc?board=";
    
    protected LinkedList<String[]> m_profile;
    protected LinkedList<InfoModel> m_infomodels;
    
    protected void loadModels() throws Exception{
        try{
            m_infomodels = new LinkedList<InfoModel>();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                InfoModel infomodel;
                String content;
                boolean root = true;
                @Override
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    if( root ){
                        root = false;
                    }else{
                        if( "model".equalsIgnoreCase(qName))
                            infomodel=new InfoModel();
                    }
                }
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    content = new String(ch, start, length).trim();
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if( "model".equalsIgnoreCase(qName)){
                        m_infomodels.add(infomodel);
                        infomodel = null;
                    }else if( "path".equalsIgnoreCase(qName)){
                        infomodel.m_path = content;
                    }else if( "user".equalsIgnoreCase(qName)){
                        infomodel.m_user = content;
                    }else if( "psw".equalsIgnoreCase(qName)){
                        infomodel.m_password = content;
                    }
                }
             };

            BufferedInputStream input = new BufferedInputStream(new FileInputStream("models.config"));
            saxParser.parse(input, handler);
            input.close();
            
            for(InfoModel infomodel : m_infomodels){
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(infomodel.m_path)));
                infomodel.m_model = (Model)ois.readObject();
                ois.close();
            }
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(-1);
        }
    }
    
    public void loadProfile() throws Exception{
        m_profile=new LinkedList<String[]>();
        BufferedReader reader = new BufferedReader(new FileReader("watchlist.txt"));
        String line=null;
        while((line=reader.readLine())!=null){
            if(line.isEmpty())
                continue;
            String[] segs = line.split(" ");
            m_profile.add(segs);
        }
        reader.close();
    }
    public void saveProfile() throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter("watchlist.txt"));
        for(String[] segs:m_profile){
            writer.write(segs[0]);
            writer.write(" ");
            writer.write(segs[1]);
            writer.write("\n");
        }
        writer.close();
    }
    
    public void executeOnce() throws Exception{
        loadProfile();
        // for each watch forum
        for(String[] segs:m_profile){
            if( m_stop )
                break;
            System.out.println("Scan for " + segs[0]);
            String boardname = segs[0];
            int startnum = Integer.parseInt(segs[1]);
            String[] newposturls = InfoSource.getLilyList(boardname, startnum);
            if( newposturls==null )
                continue;
            // for each new post
            for(String newpost:newposturls){
                if( m_stop )
                    break;
                String[] content = InfoSource.getLilyContent(newpost);
                if( content==null )
                    continue;
                docanalysis(content[0], content[1], boardname, newpost);
                // update last access post record
                segs[1] = Integer.toString( InfoSource.getLilyPostNumFromUrl(newpost) );
                saveProfile();
            }
        }
    }
    
    protected void docanalysis(String title, String content, String boardname, String url){
        String fullcontent = title + "\n" + content;
        for(InfoModel infomodel: m_infomodels){
            Model model = infomodel.m_model;
            model.featurizor.input(fullcontent, -1);
            Bag bag = (Bag)model.featurizor.getInstance();
            int predict = model.classifier.classifyBag(bag);
            if( predict == 1 )
                publish(infomodel.m_user, infomodel.m_password, title, content, boardname, url);
        }
    }
    
    protected void publish(String user, String psw, String title, String content, String boardname, String url){
        StringBuilder str = new StringBuilder();
        str.append(url);
        str.append("\n");
        str.append(title);
        str.append("\n");
        str.append(content.substring(0, Math.min(content.length(),40)));
        
        System.out.println("publish as "+user+":"+title);
        
        try{
            User usr = new User();
            usr.login(user, psw);
            Info info = new Info(usr);
            info.__infoPublish(boardname, str.toString());
        }catch(Exception exc){
            System.out.println("Exception when publishing: " + exc.getMessage());
        }
    }
    
    public void run(){
        try{
            loadModels();
            System.out.println("models loaded");
        }catch(Exception exc){
            exc.printStackTrace();
            return;
        }
        // infinite loop
        while(!m_stop){
            try{
                executeOnce();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }
    boolean m_stop=false;
    
    static LilyLooper looper=null;
    public static void main(String[] args) throws Exception{
        if( looper==null )
            looper = new LilyLooper();
        String mode = args[0];
        if ("start".equals(mode)){
            looper.m_stop=false;
            looper.start();
        }else if("stop".equals(mode)){
            looper.m_stop=true;
            looper.stop();
        }
    }
}
