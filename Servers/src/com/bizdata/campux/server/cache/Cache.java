/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * manage cache. NOTE: null should not stored in the cache.
 * @author yuy
 * @date 2011-11-30 02:38:31
 */
public class Cache<K,T> {
    protected HashMap<K, CacheItem<T>> m_cache;
    protected int m_cachesize;
    public Cache(int size){
        m_cache = new HashMap<K, CacheItem<T>>();
        m_cachesize = size;
    }
    /**
     * read an object from cache by key
     * @param key
     * @return 
     */
    synchronized public T findItem(K key){
        CacheItem<T> item = m_cache.get(key);
        if( item!=null )
            item.lastaccesstime = System.currentTimeMillis();
        return item==null ? null : item.object;
    }
    /**
     * read an object from cache by key. No set of last access time.
     * @param key
     * @return 
     */
    synchronized public T findItemQuietly(K key){
        CacheItem<T> item = m_cache.get(key);
        return item==null ? null : item.object;
    }
    /**
     * put an object in the cache
     * @param object 
     */
    synchronized public void cacheItem(K key, T object){
        if( m_cache.containsKey(key) )
            return; // if the key already cached.
        
        CacheItem<T> item = new CacheItem<T>();
        item.lastaccesstime = System.currentTimeMillis();
        item.object = object;
        m_cache.put(key, item);
        // remove the oldest cache
        if( m_cache.size() > m_cachesize){
            long oldesttime = Long.MAX_VALUE;
            K removekey = null;
            for(Entry<K, CacheItem<T>> entry : m_cache.entrySet()){
                if( oldesttime > entry.getValue().lastaccesstime ){
                    oldesttime = entry.getValue().lastaccesstime;
                    removekey = entry.getKey();
                }
            }
            CacheItem<T> toberemoved = m_cache.remove(removekey);
            if( toberemoved.object instanceof Room){
                ((Room)toberemoved.object).flush();
            }
        }
    }
    /** clean cache
     * 
     */
    synchronized public void clean(){
        m_cache.clear();
    }
    /**
     * clean a key
     * @param key 
     */
    synchronized public void clean(K key){
        m_cache.remove(key);
    }
}
