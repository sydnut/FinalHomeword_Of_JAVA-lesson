package server;

import publicClass.User;

import java.io.*;
import java.util.HashMap;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/4
 */
public class ServerService {
    private static HashMap<String ,String >map=null;//单词表
    public ServerService(HashMap<String ,String >map){
        this.map=map;
    }
    //需要考虑去重
    public static String getRandomTest2(){
        String[] strings=map.keySet().toArray(new String[0]);
        StringBuilder res=new StringBuilder();
        String key=strings[(int)Math.floor(strings.length*Math.random())];
        res.append(key);res.append(" ");
        res.append(map.get(key));
        for(int i=1;i<4;i++){
            res.append(" ");
            res.append(map.get(strings[(int)Math.floor(strings.length*Math.random())]));
        }
        return res.toString();
    }
    public static String getRandomTest1(){
        String[]strings=map.keySet().toArray(new String[0]);
        StringBuilder res=new StringBuilder();
        String key=strings[(int)Math.floor(strings.length*Math.random())];
        res.append(key);res.append(" ");res.append(map.get(key));
        return res.toString();
    }
    public static HashMap<String ,String > getUserList(String path){
        HashMap<String, String> UserMap = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            while (reader.ready()){
                UserMap.put(reader.readLine(),reader.readLine());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return UserMap;
    }
    public static void writeUser(String path,HashMap<String ,String >map){
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(path))) {
            map.forEach((k,v)->{
                printWriter.println(k);
                printWriter.println(v);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
