package com.bizdata.campux.server.userinfo;

import com.bizdata.campux.server.*;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.cache.Cache;

import java.io.*;
import java.util.*;

public class UserMessage {
	private static UserMessage s_instance = null;
    /**
     * get instance of this singleton
     * @return 
     */
    static public UserMessage getInstance(){
        if( s_instance == null )
            s_instance = new UserMessage();
        return s_instance;
    }
    private UserMessage(){
        m_path = Config.getValue("UserMessageBoxPath");
        if( !m_path.endsWith("\\") && !m_path.endsWith("/"))
            m_path += "/";
        m_maxBlocksize = (int)(Double.parseDouble( Config.getValue("UserMessageMaximumBlockSize")) *1024*1024);
        m_maxMsgBox = Integer.parseInt( Config.getValue("UserMessageMaximumMessages"));
        // init cache
        m_cachesizeMsgBox = Integer.parseInt(Config.getValue("MessageBoxCacheSize"));
        m_cachesizeMsgItem = Integer.parseInt(Config.getValue("MessageItemCacheSize"));
        m_cacheMsgBox = new Cache<String, MsgBox>(m_cachesizeMsgBox);
        m_cacheMsgItem= new Cache<String, Message>(m_cachesizeMsgItem);
        m_block = findMsgBlock();
    }
    // path to store files
    protected String m_path;
    // current block
    protected String m_block;
    // maximum block size
    protected int m_maxBlocksize;
    // maximum message box size
    protected int m_maxMsgBox;
    // message box cache size
    protected int m_cachesizeMsgBox = 100;
    // user message box catch
    protected Cache<String, MsgBox> m_cacheMsgBox;
    // message item catch size
    protected int m_cachesizeMsgItem = 100;
    // message item catch
    protected Cache<String, Message> m_cacheMsgItem;
    
    // message index preview size (of message'content)
    protected int i_previewsize=100;
    
    public void putMessage(Message msg,List<String> users){
		MsgIndex index=storeMessage(msg);
		index.i_id=-1;
		for(String user:users){
			// find quietly from cache
            MsgBox msgbox = m_cacheMsgBox.findItemQuietly(user);
            if( msgbox==null ){
                //load from file
                msgbox = new MsgBox(user);
                msgbox.loadMsgBox();
            }
            // add index
            msgbox.addIndex(index);
            // save to disk
            msgbox.saveMsgBox();
		}
	}
    /** read a message from a block according to a index.
    * The function first finds the index in cache.
    * @param im
    * @return a message
    */
   synchronized protected Message readMessage(MsgIndex im){
       Message msg = null;
       // find in cache
       String key = im.i_block + im.i_start;
       msg = m_cacheMsgItem.findItem(key);
       if( msg == null ){
           msg = new Message();
           // not found, load from file
           File blockfile = new File(m_path + im.i_block + ".msgblock");
           try{
               RandomAccessFile rafile = new RandomAccessFile(blockfile, "r");
               rafile.seek(im.i_start);
   			   msg.m_title = rafile.readUTF();
   			   msg.m_date = rafile.readUTF();
   			   msg.m_link = rafile.readUTF();
   			   msg.m_publisher = rafile.readUTF();
   			   msg.m_pubcategory = rafile.readUTF();
   			   msg.m_content = rafile.readUTF();
   			   msg.e_size=Integer.parseInt(rafile.readUTF());
   			   for(int i=0;i<msg.e_size;i++){
   				   String e_title=rafile.readUTF();
   				   String e_date=rafile.readUTF();
   				   String e_address=rafile.readUTF();
   				   Event event=new Event(e_title,e_date,e_address);
   				   msg.events.add(event);
   			   }
           }catch(Exception exc){
               Log.log("UserInfo", Type.FATAL, exc);
           }
           
           m_cacheMsgItem.cacheItem(key, msg);
       }
       //copy the value
       return msg;
   }
   /**
    * store a message into a message block, and return its index. 
    * The function will create a new block if the current block is full.
    * @param msg
    * @return the index
    */
   synchronized protected MsgIndex storeMessage(Message msg){
       // find a blockfile
       File blockfile = new File(m_path + m_block+".msgblock");
       if( blockfile.length() >= m_maxBlocksize ){
           m_block = findMsgBlock();
           blockfile = new File(m_path + m_block+".msgblock");
       }
       
       MsgIndex im = new MsgIndex();
       im.i_block = m_block;
       im.i_start = blockfile.length();
   	   im.i_id=msg.m_id;
	   im.i_title=msg.m_title;
	   im.i_date=msg.m_date;
	   im.i_publisher=msg.m_publisher;
	   im.i_pubcategory=msg.m_pubcategory;
	   im.i_preview=msg.m_content.length()>=i_previewsize?msg.m_content.substring(0,i_previewsize):msg.m_content;
       
       try{
           RandomAccessFile rafile = new RandomAccessFile(blockfile,"rwd");
           rafile.seek(rafile.length());
   		   rafile.writeUTF(msg.m_title);
   		   rafile.writeUTF(msg.m_date);
   		   rafile.writeUTF(msg.m_link);
   		   rafile.writeUTF(msg.m_publisher);
   		   rafile.writeUTF(msg.m_pubcategory);    		
   		   rafile.writeUTF(msg.m_content);
   		   rafile.writeUTF(Integer.toString(msg.e_size));
   		   if(msg.e_size!=0){
   			   for(int i=0;i<msg.events.size();i++){
       			   rafile.writeUTF(msg.events.get(i).event_title);
       			   rafile.writeUTF(msg.events.get(i).event_date);
       			   rafile.writeUTF(msg.events.get(i).event_place);
       		}
   		}
   		
       }catch(Exception exc){
           Log.log("UserInfo", Type.FATAL, exc);
       }
       
       blockfile = new File(m_path + m_block+".msgblock");
       
       return im;
   }    
   /**
    * find a message block. If all blocks are full, create a new one.
    * @return block name
    */
   protected String findMsgBlock(){
       String block = null;
       File dir = new File(m_path);
       File[] files = dir.listFiles();
       for(File f : files){
           String name = f.getName();
           if( name.endsWith(".msgblock") && f.length()<m_maxBlocksize ){
               block = name.substring(0, name.lastIndexOf('.'));
           }
       }
       if( block==null){
           Random rnd = new Random(System.currentTimeMillis());
           boolean exists = true;
           do{
               char[] c = new char[20];
               for(int i=0; i<20; i++){
                   c[i] = (char)(rnd.nextInt(26)+'a');
               }
               block = new String(c,0,20);
               exists = (new File(m_path + block+".msgblock").exists());
           }while(exists);
           try{
               new File(m_path + block+".msgblock").createNewFile();
           }catch(Exception exc){Log.log("UserStatus", Type.FATAL, exc);}
       }
       
       return block;
   }    
   public MsgIndex getIndex(String user,int id){
	   MsgBox msgbox = m_cacheMsgBox.findItem(user);
       if( msgbox==null ){
           msgbox = new MsgBox(user);
           msgbox.loadMsgBox();
       }
       MsgIndex im = new MsgIndex();
       for( MsgIndex index : msgbox.indeces ){
           if( index.i_id ==id ){
               im=index.clone();
               break;
           }
       }       
       return im;
   }
   public MsgIndex[] getNewIndex(String user,int lastindexid){
	   MsgBox msgbox = m_cacheMsgBox.findItem(user);
       if( msgbox==null ){
           msgbox = new MsgBox(user);
           msgbox.loadMsgBox();
       }
       LinkedList<MsgIndex> list = new LinkedList<MsgIndex>();
       for( MsgIndex index : msgbox.indeces ){
           if( index.i_id > lastindexid ){
               list.addLast(index);
           }
       }
       MsgIndex[] indices = new MsgIndex[list.size()];
       int i=0;
       for(MsgIndex index : list){
    	   indices[i++] = index;
       }
       return indices;
   }
   public Message[] getNewMessage(String user, int lastid){
       // load the message box
       MsgBox msgbox = m_cacheMsgBox.findItem(user);
       if( msgbox==null ){
           msgbox = new MsgBox(user);
           msgbox.loadMsgBox();
       }
       // find messages with id larger than lastid
       LinkedList<Message> list = new LinkedList<Message>();
       for( MsgIndex index : msgbox.indeces ){
           if( index.i_id > lastid ){
               Message msg = readMessage(index);
               msg.m_id = index.i_id;
               list.addLast(msg);
           }
       }
       //
       Message[] msgs = new Message[list.size()];
       int i=0;
       for(Message msg : list){
           msgs[i++] = msg;
       }
       return msgs;
   }
   public boolean deleteMessage(String user, int id){
       // load the message box
       MsgBox msgbox = m_cacheMsgBox.findItem(user);
       if( msgbox==null ){
           msgbox = new MsgBox(user);
           msgbox.loadMsgBox();
       }
       boolean success = msgbox.removeIndex(id);
       if( success )
           msgbox.saveMsgBox();
       return success;
   }
   /**
    * load a messagebox
    * @param user
    * @return 
    */
   protected MsgBox readMsgBox(String user){
       MsgBox msgbox = m_cacheMsgBox.findItem(user);
       if( msgbox==null ){
           //load from file
           msgbox = new MsgBox(user);
           msgbox.loadMsgBox();
           m_cacheMsgBox.cacheItem(user, msgbox);
       }
       return msgbox;
   }
   
   protected boolean initialNewUser(String user){
	   //copy all MsgIndex items from guest.msgbox
	   MsgIndex[] indices=getNewIndex("guest",-1);
	   File file=new File(m_path+user+".msgbox");
	   try{
		   if(file.exists())
			   return false;
		   else file.createNewFile();
	   }catch(Exception exc){
           Log.log("UserInfo", Type.FATAL, exc);
       }		  
	   MsgBox box=new MsgBox(user);
	   for(MsgIndex index: indices){
		   box.addIndex(index);
	   }
	   box.saveMsgBox();
	   return true;
   }
   
   class MsgBox{
        LinkedList<MsgIndex> indeces = new LinkedList<MsgIndex>();
        String m_user;
        int m_nextID = -1;
        
        public MsgBox(String user){
            m_user = user;
        }
        /**
         * assign a index ID. The assigned ID will be regarded as taken. Don't waste.
         * @return 
         */
        public int assignID(){
            // find the next id
            if( m_nextID==-1 ){
                for(MsgIndex index:indeces){
                    if( m_nextID <= index.i_id )
                        m_nextID = index.i_id+1;
                }
                if( m_nextID==-1 )
                    m_nextID=0;
            }
            int ret = m_nextID;
            m_nextID++;
            return ret;
        }
        /**
         * add an index. If index.id is -1, assign a new id to it.
         * @param index 
         */
        public void addIndex(MsgIndex index){
            index = index.clone();
            if( index.i_id==-1 )
                index.i_id = assignID();
            indeces.addFirst(index);
        }
        /**
         * remove a index by ID
         * @param id 
         */
        public boolean removeIndex(int id){
            for(Iterator<MsgIndex> itr = indeces.iterator(); itr.hasNext();){
                if( itr.next().i_id== id ){
                    itr.remove();
                    return true;
                }
            }
            return false;
        }
        /**
         * load message box from file
         * @param user 
         */
        synchronized protected void loadMsgBox(){
            // msgbox file
            File msgboxfile = new File(m_path + m_user + ".msgbox");
            if( !msgboxfile.exists() ){
                return;
            }
            try{
                ObjectInputStream input = new ObjectInputStream(
                        new BufferedInputStream(new FileInputStream(msgboxfile)));
                while(true){
                    // read an object
                    Object obj = null;
                    try{
                        obj = input.readObject();
                    }catch(Exception exc){break;}
                    // end of file
                    if( obj==null )
                        break;
                    // parse data
                    MsgIndex im = (MsgIndex)obj;
                    indeces.add(im);
                }
                input.close();
            }catch(Exception exc){
                Log.log("UserInfo", Type.FATAL, exc);
            }
        }
        /**
         * save message box to file
         */
        synchronized protected void saveMsgBox(){
            // only save the messages with id larger than idthreshold
            int idthreshold = -1;
            // if the message box have too many items, only save the ones with the largest id by setting the idthreshold
            if( indeces.size() > m_maxMsgBox ){
                int[] ids = new int[indeces.size()];
                int i=0;
                for(MsgIndex index : indeces)
                    ids[i++] = index.i_id;
                Arrays.sort(ids);
                idthreshold = ids[ ids.length - m_maxMsgBox];
            }
            // msgfile
            File msgboxfile = new File(m_path + m_user + ".msgbox");
            // save
            try{
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(msgboxfile,false));
                for(MsgIndex index : indeces){
                    if( index.i_id >= idthreshold ){
                        output.writeObject(index);
                    }
                }
                output.close();
            }catch(Exception exc){
                Log.log("UserInfo", Type.FATAL, exc);
            }
        }
    }
    
}
