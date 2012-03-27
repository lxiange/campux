/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bizdata.infosource;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class InfoSource {
    protected static WebClient s_webClient = null;
    protected static String s_titlesep = "标 题: ";
    protected static String s_endsep="※ 来源:．南京大学小百合站";
    protected static String s_bodysep = " 发信站: 南京大学小百合站";
    protected static String s_topicStarts = "bbstcon?board=";
    protected static String s_listStarts = "bbstdoc?board=";
    protected static String s_listUrlPrefix="http://bbs.nju.edu.cn/bbstdoc?board=";
    
    synchronized static public String getGeneralContent(String url){
        initWebClient();
        try{
            HtmlPage page = s_webClient.getPage(url);
            return page.asText();
        }catch(Exception exc){
            s_webClient.closeAllWindows();
            return null;
        }
    }
    
    synchronized static public String[] getLilyList(String url, int startnum){
        initWebClient();
        
        if(!url.startsWith("http"))
            url = s_listUrlPrefix + url;
        HashMap<Integer, String> listurl = new HashMap<Integer, String>();
        LinkedList<Integer> listnum = new LinkedList<Integer>();
        
        try{
            do{
                boolean meetnewposts = false;
                int prepagenum = Integer.MAX_VALUE;
                HtmlPage page = getPage(url);
                if( page==null )
                    return null;
                
                Iterable<DomNode> nodes = page.getDescendants();
                for(DomNode node:nodes){
                    if( "a".equals(node.getNodeName()) ){
                        //parse link
                        String href = node.getAttributes().getNamedItem("href").getNodeValue();
                        if( href.startsWith(s_topicStarts) ){
                            int postnum = getLilyPostNumFromUrl(href);
                            if( postnum==-1)
                                continue;
                            if( postnum > startnum){
                                href = "http://bbs.nju.edu.cn/" + href;
                                if( !listurl.containsKey(postnum) ){
                                    listurl.put(postnum, href);
                                    listnum.add(postnum);
                                }
                                meetnewposts = true;
                            }
                        }else if( href.startsWith(s_listStarts) ){
                            String[] segs = href.split("[&=]");
                            int thisprepagenum=Integer.MAX_VALUE;
                            for(int i=0; i<segs.length-1; i++)
                                if( "start".equalsIgnoreCase(segs[i].trim())){
                                    thisprepagenum = Integer.parseInt(segs[i+1]);
                                    break;
                                }
                            if(thisprepagenum<prepagenum){
                                prepagenum = thisprepagenum;
                                url = "http://bbs.nju.edu.cn/" + href;
                            }
                        }
                    }
                }
                if( !meetnewposts )
                    break;
                Thread.sleep(3000);
            }while(true);
            
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
        
        s_webClient.closeAllWindows();
        
        Collections.sort(listnum);
        
        String[] sortedurl = new String[listnum.size()];
        int i=0;
        for(Iterator<Integer> itr = listnum.iterator(); itr.hasNext(); i++){
            sortedurl[i] = listurl.get(itr.next());
        }
        return sortedurl;
    }
    
    synchronized static public String[] getLilyContent(String url){
        initWebClient();      
        try{
            HtmlPage page = getPage(url);
            if( page==null ) return null;
            
            String encode = page.getPageEncoding();
            //System.out.println(page.asXml());
            Iterable<DomNode> nodes = page.getDescendants();
            DomNode prenode = null;
            for(DomNode node:nodes){
                if( "pre".equals(node.getNodeName()) ){
                    prenode = node;
                    break;
                }
            }
            //System.out.println(prenode.asText());
            s_webClient.closeAllWindows();
            
            if( prenode==null )
                return null;
            
            String text = prenode.asText();
            
            String[] strs=new String[2];
            
            int tindex = text.indexOf(s_titlesep);
            int tbegin = tindex + s_titlesep.length();
            int tend = text.indexOf(s_bodysep);
            int bbegin = text.indexOf(")", tend);
            int bend = text.indexOf(s_endsep);
            if( tindex==-1 || tend==-1 || bbegin==-1) return null;
            if( bend==-1 ) bend = text.length();
            if( bbegin+2>bend ) bbegin=bend-1;
            
            strs[0] = text.substring(tbegin, tend);
            strs[1] = text.substring(bbegin+2, bend);
            
            return strs;
        }catch(Exception exc){
            s_webClient.closeAllWindows();
            return null;
        }
    }
    
    protected static void initWebClient(){
        if(s_webClient==null){
            s_webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7);
            s_webClient.setJavaScriptEnabled(true);
            s_webClient.setThrowExceptionOnFailingStatusCode(false);
            s_webClient.setThrowExceptionOnScriptError(false);
            s_webClient.setCssEnabled(false);
        }
    }
    
    protected static String convertUTF8(String str, String encode){
        Charset charset = Charset.forName(encode);
        CharBuffer cb = charset.decode(ByteBuffer.wrap(str.getBytes(charset)));
        return cb.toString();
    }
    
    protected static HtmlPage getPage(String url){
        HtmlPage page = null;
        int maxtry = 3;
        boolean success = false;
        if( url.startsWith("http://bbs.nju.edu.cn") ){
            try{Thread.sleep(1000);}catch(Exception exc){}
            while(maxtry>0 && !success){
                maxtry--;
                try{
                    page = s_webClient.getPage(url);
                    success = true;
                }catch(Exception exc){
                    try{Thread.sleep(5000);}catch(Exception e){}
                }
            }
        }
        return page;
    }
    
    public static int getLilyPostNumFromUrl(String href){
         int numstartindex = href.indexOf("M.");
         int numendindex = href.indexOf(".A");
         if( numstartindex==-1 || numendindex==-1)
            return -1;
         return Integer.parseInt(href.substring(numstartindex+2, numendindex));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        LilyLooper loop = new LilyLooper();
        loop.loadModels();
        loop.executeOnce();
    }
}
