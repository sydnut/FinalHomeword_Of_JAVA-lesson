package server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.*;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/19
 */
public class ManageServer {
    private static Set<String >ManageSet=null;
    private static ReentrantReadWriteLock lock=null;
    public ManageServer(){
        if (ManageSet == null) {
            ManageSet=new HashSet<>();
        }
        if(lock==null){
            lock=new ReentrantReadWriteLock();
        }
    }
    //加写锁
    public  static  void add(String id) {
        lock.writeLock().lock();
        ManageSet.add(id);
        lock.writeLock().unlock();
    }
    //加读锁
    public static String getOnlineList(String id){
        StringBuilder s=new StringBuilder();
        lock.readLock().lock();
        ManageSet.stream().filter(a->!a.equals(id)).collect(Collectors.toSet()).forEach(a->{
            s.append(a);s.append(' ');
        });
        lock.readLock().unlock();
        return s.toString();
    }
    public static void removeThread(String id){
        lock.writeLock().lock();
        ManageSet.remove(id);
        lock.writeLock().unlock();
    }

}
