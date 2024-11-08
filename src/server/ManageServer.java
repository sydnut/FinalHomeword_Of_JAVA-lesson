package server;

import java.util.stream.*;
import java.util.HashMap;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class ManageServer {
    private static HashMap<String ,Thread>ManageMap=null;
    public ManageServer(){
        if (ManageMap == null) {
            ManageMap=new HashMap<>();
        }
    }
    public synchronized static  void add(String id,Thread thread) {
        ManageMap.put(id, thread);
    }
    public synchronized static String getOnlineList(String id){
        StringBuilder s=new StringBuilder();
        ManageMap.keySet().stream().filter(a->!a.equals(id)).collect(Collectors.toSet()).forEach(a->{
            s.append(a);s.append(' ');
        });
        return s.toString();
    }

}
