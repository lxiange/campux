/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.docanalysis;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.cache.Cache;
import com.bizdata.intellix.docanalysis.Bag;
import com.bizdata.intellix.docanalysis.DenseInstance;
import com.bizdata.intellix.docanalysis.MIClassifier;
import com.bizdata.intellix.docanalysis.Featurizor;
import com.bizdata.intellix.docanalysis.Instance;
import com.bizdata.intellix.docanalysis.MIFeaturior;
import com.bizdata.intellix.docanalysis.MIRF;
import com.bizdata.intellix.docanalysis.Model;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2011-11-29 01:38:24
 */
public class Models {
    private static Models s_instance = null;
    /**
     * get instance of this singleton
     * @return 
     */
    static public Models getInstance(){
        if( s_instance == null )
            s_instance = new Models();
        return s_instance;
    }
    /**
     * constructor, initialization.
     */
    private Models(){
        m_path = Config.getValue("DocAnalysisTaskPath");
        if( !m_path.endsWith("\\") && !m_path.endsWith("/"))
            m_path += "/";
        File file = new File(m_path);
        if( !file.exists() || !file.isDirectory() ){
            if( !file.mkdirs() )
                Log.log("DocAnalysisModels", Type.FATAL, "directory can not be created:" + m_path);
        }
        // init cache
        m_cachesize = Integer.parseInt(Config.getValue("ModelCatchSize"));
        m_cacheModel = new Cache<String, Model>(m_cachesize);
    }
    
    // path to store files
    protected String m_path;
    // model cache size
    protected int m_cachesize = 1;
    // model cache
    protected Cache<String, Model> m_cacheModel;
    
    public void storeData(String task, String data, boolean append) throws Exception{
        File directory = new File(m_path + task);
        if( directory.exists()==false || directory.isDirectory()==false )
            directory.mkdirs();
        
        String filename = m_path + task + "/train.data";
        File file = new File(filename);
        //backup
        if( file.exists() ){
            boolean backupexists = true;
            int backupno=0;
            File backupfile = null;
            // find backup filename
            while( backupexists ){
                backupfile = new File(filename + task + "/train.bak." + backupno);
                backupexists = backupfile.exists();
                backupno++;
            }
            FileChannel input = new FileInputStream(file).getChannel();
            FileChannel output = new FileOutputStream(backupfile).getChannel();
            output.transferFrom(input, 0, input.size());
            input.close();
            output.close();       
        }
        // store data
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file, append));
        output.write(data.getBytes(Config.getCharset()));
        output.close();
    }
    
    public boolean trainModel(String task){
        // clean the cache item
        m_cacheModel.clean(task);
        
        String filename = m_path + task + "/train.data";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String numclassstr = reader.readLine();
            String numattstr = reader.readLine();
            int numclass = Integer.parseInt(numclassstr);
            
            Model model = new Model();
            model.featurizor = new MIFeaturior();
            model.featurizor.setNumClasses(numclass);
            model.setNumClasses(numclass);
            String line=null;
            LinkedList<Bag> traindata = new LinkedList<Bag>();
            while((line=reader.readLine())!=null){
                Bag bag = extractBag(line, model);
                traindata.add(bag);
            }
            
            MIRF mirf = new MIRF();
            mirf.setFeatureSize(5);
            mirf.setNumClasses(2);
            mirf.setMaxTreeLayer(-1);
            mirf.setUseMetaClassifier(false);
            mirf.setSeed(100);
            mirf.setNumTrees(3);
            model.classifier = mirf;

            //mirf.inputTrainingData(data);

            mirf.buildClassifier();
            
            String modelpath = m_path + task + "/.model";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(modelpath));
            output.writeObject(model);
            output.close();
        }catch(Exception exc){
            Log.log("docanalysis", Type.NOTICE, exc);
            return false;
        }
        return true;
    }
    public int predictInstance(String task, String line) throws Exception{
        Model model = m_cacheModel.findItem(task);
        
        if( model==null ){ // load model
            String modelpath = m_path + task + "/.model";
            try{
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(modelpath));
                model = (Model)input.readObject();
                input.close();
            }catch(Exception exc){
                // model load fail
                model = null;
            }
        }
        
        if( model==null )
            throw new Exception("Model is not available yet.");
        
        m_cacheModel.cacheItem(task, model);
        
        Bag bag = extractBag(line, model);
        int c = model.classifier.classifyBag(bag);
        
        return c;
    }
    
    protected Bag extractBag(String line, Model model){
        String [] sep = line.split(",");
        int c = Integer.parseInt(sep[0]);
        
        DenseInstance instance = new DenseInstance(sep.length-2);
        instance.setLabel(c);
        for(int i=1; i<sep.length-2; i++){
            int v = Integer.parseInt(sep[i]);
            instance.setValue(i-1, v);
        }
        
        model.featurizor.input(sep[sep.length-1], c);
        model.featurizor.generateFeature();
        Bag bag = (Bag)model.featurizor.getInstance();
        bag.setBagInstance(instance);
        instance.setBag(bag);
        
        return bag;
    }
}
