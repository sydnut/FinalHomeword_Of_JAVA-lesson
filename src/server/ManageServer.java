package server;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.*;
import java.util.HashMap;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class ManageServer {
    private static HashMap<String ,Thread>ManageMap=null;
    private static ReentrantReadWriteLock lock=null;
    public ManageServer(){
        if (ManageMap == null) {
            ManageMap=new HashMap<>();
        }
        if(lock==null){
            lock=new ReentrantReadWriteLock();
        }
    }
    //加写锁
    public  static  void add(String id,Thread thread) {
        lock.writeLock().lock();
        ManageMap.put(id, thread);
        lock.writeLock().unlock();
    }
    //加读锁
    public static String getOnlineList(String id){
        StringBuilder s=new StringBuilder();
        lock.readLock().lock();
        ManageMap.keySet().stream().filter(a->!a.equals(id)).collect(Collectors.toSet()).forEach(a->{
            s.append(a);s.append(' ');
        });
        lock.readLock().unlock();
        return s.toString();
    }
    public static void removeThread(String id){
        lock.writeLock().lock();
        ManageMap.remove(id);
        lock.writeLock().unlock();
    }

}
