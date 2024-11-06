package function;

import publicClass.Message;
import publicClass.MessageType;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/2
 */
public class Test4 {
    public static void showOnlineFriends(Socket socket){
        JFrame frame = new JFrame("online friends");
        frame.setBounds(500,200,600,500);
        JScrollPane jScrollPane = new JScrollPane();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Message message = new Message();message.setType(MessageType.MSG_ASK_ONLINE_LIST);
            oos.writeObject(message);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String[]strings=((Message)ois.readObject()).getContent().split(" ");
            Arrays.stream(strings).forEach(s->{
                jPanel.add(new JLabel(s));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        jScrollPane.setViewportView(jPanel);frame.add(jScrollPane);

        frame.setVisible(true);

    }
}
