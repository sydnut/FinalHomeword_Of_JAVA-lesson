package server;

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
    public synchronized static String getOnlineList(){
        StringBuilder s=new StringBuilder();
        ManageMap.keySet().forEach(a->{
            s.append(a);s.append(' ');
        });
        return s.toString();
    }

}
