package client;

import publicClass.Message;
import publicClass.MessageType;
import publicClass.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class ClientService {
    private Socket socket;
    private  boolean ok=false;
    public MessageType isLogin(String id,String pwd,MessageType messageType) throws IOException {
        User user=new User();user.setId(id);user.setPwd(pwd);user.setMessageType(messageType);
        try{
            socket=new Socket(InetAddress.getLocalHost(),9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg=(Message)ois.readObject();
            return msg.getType();

        }catch (Exception e){
            e.printStackTrace();
        }
        if(!ok)socket.close();
        return null;
    }
    public Socket getSocket(){return socket;}
    //实现test2的网络通信
    public String[] getStrings() {
        try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message msg = new Message();
                msg.setType(MessageType.MSG_TEST_2);
                oos.writeObject(msg);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getType().equals(MessageType.MSG_TEST_2)){
                    String[] strings=message.getContent().split(" ");
                    return strings;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //实现test1的网络通信
    public String [] getTest_1(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Message msg=new Message();msg.setType(MessageType.MSG_TEST_1);
            oos.writeObject(msg);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
            if (message.getType().equals(MessageType.MSG_TEST_1)){
                String[] res=message.getContent().split(" ");
                return res;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void setClosedAction(){
        try{ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message msg = new Message();
                msg.setType(MessageType.MSG_QUIT);
                oos.writeObject(msg);
    }catch (Exception e){
            e.printStackTrace();
        }
    }
}