package server;

import publicClass.Message;
import publicClass.MessageType;
import publicClass.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/19
 */
public class Server {
    //单词表
    private static String filename = "C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\list.txt";
    private static HashMap<String, String> EnCnMap = new HashMap<>();
    //已注册的用户集合
    private static HashMap<String, String> UserMap;
    private static String UserFileName = "C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\UserList.txt";

    static {
        //读取用户列表
        UserMap = ServerService.getUserList(UserFileName);

        //读取单词表
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            while (reader.ready()) {
                EnCnMap.put(reader.readLine(), reader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //采用线程池实现多线程，避免无限的发起线程浪费资源
    private ExecutorService executor = Executors.newFixedThreadPool(100);
    //管理登录了的用户线程集合
    private ManageServer manageServer;
    //服务器对应服务类
    private ServerService serverService;

    public Server() {
        serverService = new ServerService(EnCnMap);
        manageServer = new ManageServer();
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("now is listening in 9999 port");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();//block
                    executor.execute(new ServerToClient(socket));

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    //服务器与客户端通信的线程
    private class ServerToClient implements Runnable {
        private String uid;
        private Socket socket;
        private boolean loop = true;

        public ServerToClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                Message msg = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                if (UserMap.get(user.getId()) != null && UserMap.get(user.getId()).equals(user.getPwd()) && user.getMessageType().equals(MessageType.MSG_LOGIN_SUCCESS)) {
                    System.out.println(user.getId() + " login success!");
                    uid = user.getId();
                    msg.setType(MessageType.MSG_LOGIN_SUCCESS);
                    oos.writeObject(msg);
                    ManageServer.add(uid);
                    ServerService.writeUser(UserFileName, UserMap);
                } else if (UserMap.get(user.getId()) == null && user.getMessageType().equals(MessageType.MSG_SIGN_UP)) {
                    System.out.println(user.getId() + " is signing up!");
                    uid = user.getId();
                    msg.setType(MessageType.MSG_SIGN_UP);
                    oos.writeObject(msg);
                    UserMap.put(uid, user.getPwd());
                    ManageServer.add(uid);
                    ServerService.writeUser(UserFileName, UserMap);
                } else {
                    System.out.println(user.getId() + " fails to login!");
                    msg.setType(MessageType.MSG_LOGIN_FAILURE);
                    oos.writeObject(msg);
                    return;
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            while (loop) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Message msg = (Message) ois.readObject();
                    if (msg.getType().equals(MessageType.MSG_ASK_ONLINE_LIST)) {
                        System.out.println(uid + " is asking the online list!");
                        Message message = new Message();
                        message.setContent(ManageServer.getOnlineList(uid));
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(message);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    } else if (msg.getType().equals(MessageType.MSG_TEST_2)) {
                        try {
                            System.out.println(uid + " is asking the test2");
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            Message message = new Message();
                            message.setType(MessageType.MSG_TEST_2);
                            message.setContent(ServerService.getRandomTest2());
                            oos.writeObject(message);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    } else if (msg.getType().equals(MessageType.MSG_TEST_1)) {
                        System.out.println(uid + " is asking the test1");
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        Message message = new Message();
                        message.setType(MessageType.MSG_TEST_1);
                        message.setContent(ServerService.getRandomTest1());
                        oos.writeObject(message);
                    } else if (msg.getType().equals(MessageType.MSG_QUIT)) {
                        ManageServer.removeThread(uid);
                        loop = false;
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            System.out.println(uid + " has quit!");
        }
    }


}
