package server;

import publicClass.Message;
import publicClass.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class ServerToClientThread extends Thread{
    private String uid;
    private Socket socket;
    public ServerToClientThread(String name,Socket socket){
        uid=name;this.socket=socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public void run(){
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message msg=(Message) ois.readObject();
                if(msg.getType().equals(MessageType.MSG_ASK_ONLINE_LIST)){
                    System.out.println(uid+" is asking the online list!");
                    Message message = new Message();
                    message.setContent(ManageServer.getOnlineList(uid));
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message);
                    }catch (Exception e) {
                        e.getStackTrace();
                    }
                }else if (msg.getType().equals(MessageType.MSG_TEST_2)){
                    try {
                        System.out.println(uid +" is asking the test2");
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        Message message=new Message();
                        message.setType(MessageType.MSG_TEST_2);message.setContent(ServerService.getRandomTest2());
                        oos.writeObject(message);
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                }else if(msg.getType().equals(MessageType.MSG_TEST_1)){
                    System.out.println(uid+" is asking the test1");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    Message message=new Message();
                    message.setType(MessageType.MSG_TEST_1);
                    message.setContent(ServerService.getRandomTest1());
                    oos.writeObject(message);
                }

            }catch (Exception e){
                e.getStackTrace();
            }
        }
    }
}
