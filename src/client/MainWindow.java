package client;

import function.Test1;
import function.Test2;
import function.Test3;
import function.Test4;
import publicClass.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/1
 */
public class MainWindow {
    private ClientService clientService;
    //单例，避免生成太多对话框浪费内存！
    private Dialog dialog1 = null;
    private Dialog dialog2 = null;
    private Test3 vocabulary;

    public MainWindow() {

        clientService = new ClientService();
        JFrame frame = new JFrame("English Application");
        frame.setBounds(500, 200, 600, 500);
        JPanel jPanel = new JPanel();
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(4);
        jPanel.setLayout(gridLayout);
        JLabel title = new JLabel("Welcome to login", SwingConstants.CENTER);
        jPanel.add(title);
        //设置用户名和密码输入框
        JTextField uid = new JTextField();
        uid.setText("input your uid: ");
        jPanel.add(uid);
        JPasswordField pwd = new JPasswordField();
        pwd.setText("password");
        jPanel.add(pwd);
        //设置密码不可见
        pwd.setEchoChar((char) 0);
        pwd.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                pwd.setText("");
                pwd.setEchoChar('*');
            }
        });
        //设置提交按钮并发送信息
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        JButton button1 = new JButton("SIGN_UP");
        JButton button2 = new JButton("LOGIN");
        panel.add(button1);
        panel.add(button2);
        jPanel.add(panel);
        button2.addActionListener(e -> {
            try {
                String userId=uid.getText().substring(16);
                if (clientService.isLogin(userId, pwd.getText(),MessageType.MSG_LOGIN_SUCCESS).equals(MessageType.MSG_LOGIN_SUCCESS)) {
                    jPanel.removeAll();
                    vocabulary = new Test3(userId);
                    JButton test1 = new JButton("看中文填英语");
                    JButton test2 = new JButton("看英语选中文");
                    JButton test3 = new JButton("查看单词");
                    //Test1实现
                    test1.addActionListener(e3 -> new Test1(clientService, vocabulary).show());
                    //TEST2实现
                    test2.addActionListener(e1 -> new Test2(clientService, vocabulary).test());
                    //test3实例
                    test3.addActionListener(e2 -> {
                        vocabulary.outputVocabulary();
                        vocabulary.checkVocabulary();
                    });

                    //TEST4实现
                    JButton checkFriends = new JButton("查看在线好友");
                    checkFriends.addActionListener(i ->
                            Test4.showOnlineFriends(clientService.getSocket())
                    );

                    //添加组件
                    jPanel.add(test1);
                    jPanel.add(test2);
                    jPanel.add(test3);
                    jPanel.add(checkFriends);
                    jPanel.revalidate();
                    jPanel.repaint();
                    setClosingAciton(frame);
                } else {
                    if (dialog1 == null) {
                        dialog1 = new Dialog(frame, "Error!");
                        dialog1.setBounds(600, 300, 300, 100);
                        dialog1.add(new JLabel("No UID OR WRONG PASSWORD!"));
                    }
                    dialog1.setVisible(true);
                    dialog1.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            dialog1.setVisible(false);
                            uid.setText("input your uid: ");
                            pwd.setText("");
                        }
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        button1.addActionListener(e -> {
            try {
                String userId=uid.getText().substring(16);
                if (clientService.isLogin(userId, pwd.getText(),MessageType.MSG_SIGN_UP).equals(MessageType.MSG_SIGN_UP)) {
                    if (dialog2 == null) {
                        dialog2 = new Dialog(frame);
                        dialog2.setBounds(600, 300, 300, 100);
                        dialog2.add(new JLabel("Welcome our new User!"));
                    }
                    dialog2.setVisible(true);
                    jPanel.removeAll();
                    vocabulary = new Test3(userId);
                    JButton test1 = new JButton("看中文填英语");
                    JButton test2 = new JButton("看英语选中文");
                    JButton test3 = new JButton("查看单词");
                    //Test1实现
                    test1.addActionListener(e3 -> new Test1(clientService, vocabulary).show());
                    //TEST2实现
                    test2.addActionListener(e1 -> new Test2(clientService, vocabulary).test());
                    //test3实例
                    test3.addActionListener(e2 -> {
                        vocabulary.outputVocabulary();
                        vocabulary.checkVocabulary();
                    });

                    //TEST4实现
                    JButton checkFriends = new JButton("查看在线好友");
                    checkFriends.addActionListener(i ->
                            Test4.showOnlineFriends(clientService.getSocket())
                    );


                    //添加组件
                    jPanel.add(test1);
                    jPanel.add(test2);
                    jPanel.add(test3);
                    jPanel.add(checkFriends);
                    jPanel.revalidate();
                    jPanel.repaint();
                    setClosingAciton(frame);
                } else {
                    if (dialog2 == null) {
                        dialog2 = new Dialog(frame);
                        dialog2.setBounds(600, 300, 300, 100);
                        dialog2.add(new JLabel("RepeatedUID!"));
                    }
                    dialog2.setVisible(true);
                    dialog2.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            dialog2.setVisible(false);
                            uid.setText("input your uid: ");
                            pwd.setText("");
                        }
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        //设置frame
        frame.add(jPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
    private void setClosingAciton(JFrame frame){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientService.setClosedAction();
            }
        });
    }

}