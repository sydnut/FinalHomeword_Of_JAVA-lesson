package server;

import publicClass.Message;
import publicClass.MessageType;
import publicClass.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class Server {
    //单词表
    private static String filename="C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\list.txt";
    private static HashMap<String ,String>EnCnMap=new HashMap<>();
    //管理登录了的用户线程集合
    private ManageServer manageServer;

    //服务器对应服务类
    private ServerService serverService;
    //已注册的用户集合
    private static HashMap<String ,String>UserMap=null;
    private static String  UserFileName="C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\UserList.txt";
    static {
        //读取用户列表
        UserMap=ServerService.getUserList(UserFileName);

        //读取单词表
        try{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        while (reader.ready()){
            EnCnMap.put(reader.readLine(), reader.readLine());
        }
        }catch (Exception e){e.printStackTrace();}

    }
    public Server(){
        serverService=new ServerService(EnCnMap);
        manageServer=new ManageServer();
        try(ServerSocket serverSocket=new ServerSocket(9999)){
            System.out.println("now is listening in 9999 port");
             while (true) {
                 try{
                     Socket socket = serverSocket.accept();//block
                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                     User user=(User)ois.readObject();
                     Message msg = new Message();
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                     if(UserMap.get(user.getId())!=null&&UserMap.get(user.getId()).equals(user.getPwd())&&user.getMessageType().equals(MessageType.MSG_LOGIN_SUCCESS)){
                         System.out.println(user.getId()+" login success!");
                         msg.setType(MessageType.MSG_LOGIN_SUCCESS);
                         oos.writeObject(msg);
                         ServerToClientThread thread = new ServerToClientThread(user.getId(), socket);
                         ManageServer.add(user.getId(), thread);
                         thread.start();
                         ServerService.writeUser(UserFileName,UserMap);
                     }else if(UserMap.get(user.getId())==null&&user.getMessageType().equals(MessageType.MSG_SIGN_UP)){
                         System.out.println(user.getId()+" is signing up!");
                         msg.setType(MessageType.MSG_SIGN_UP);
                         oos.writeObject(msg);
                         UserMap.put(user.getId(), user.getPwd());
                         ServerToClientThread thread=new ServerToClientThread(user.getId(), socket);
                         ManageServer.add(user.getId(),thread);
                         thread.start();
                         ServerService.writeUser(UserFileName,UserMap);
                     }
                     else {
                         System.out.println(user.getId()+" fails to login!");
                         msg.setType(MessageType.MSG_LOGIN_FAILURE);
                         oos.writeObject(msg);
                     }
                 }catch (Exception e){
                     e.getStackTrace();
                 }
             }

        }catch (Exception e){
            e.getStackTrace();
        }
        finally {

        }
    }

    public static void main(String[] args) {
        new Server();
    }



}
