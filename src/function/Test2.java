package function;

import client.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/6
 */

public class Test2 {
    //相关组件

    private JButton[] jButtons = new JButton[5];
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();
    private JFrame f;
    JLabel itemLabel = new JLabel();
    private int count = 10;
    private Timer timer;
    private Dialog dialog = null;
    private JLabel label = new JLabel();


    private int grades=10;
    private ClientService clientService;
    private Test3 test3;
    private String rightAnswer;

    public Test2(ClientService clientService,Test3 test3) {
        this.clientService = clientService;
        this.test3=test3;
    }
    //设置答案
    private void handRightAnswer() {
        String[] strings = clientService.getStrings();
        String item = strings[0];
        itemLabel.setText(item);
        for (int i = 1; i <= 4; i++) {
            jButtons[i].setText(strings[i]);
        }
        int rightNum = (int) Math.ceil(Math.random() * 4);
        rightAnswer = strings[1];
        jButtons[rightNum].setText(strings[1]);
        jButtons[1].setText(strings[rightNum]);
    }
    //报错窗口
    private void wrongShow() {
        timer.stop();
        if (dialog == null) {
            dialog = new Dialog(f);
            dialog.setBounds(600, 300, 600, 200);
            dialog.add(label);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    timer.start();
                    dialog.setVisible(false);
                }
            });
        }
        label.setText("Failure,the right answer is "+rightAnswer);
        if(count>0)
        test3.addUnknown(itemLabel.getText()+"(ANSWER_WRONG)",rightAnswer);
        else test3.addUnknown(itemLabel.getText()+"(NO_ANSWER)",rightAnswer);
        handRightAnswer();
        dialog.setVisible(true);
        count = 10;
        f.revalidate();
        f.repaint();
    }

    public void test(){
        jButtons[1] = button1;
        jButtons[2] = button2;
        jButtons[3] = button3;
        jButtons[4] = button4;
        handRightAnswer();


        f = new JFrame();
        f.setBounds(500, 200, 600, 500);
        f.setCursor(new Cursor(Cursor.HAND_CURSOR));
        f.setTitle("Test2");
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(3);
        f.setLayout(gridLayout);
        JPanel panel = new JPanel();
        GridLayout gridLayout1 = new GridLayout();

        JLabel jLabelGrades = new JLabel("Grades: "+grades);

        panel.setBackground(Color.pink);
        //设置题目参数
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.gray);
        gridLayout1.setRows(2);panel1.setLayout(gridLayout1);
        panel1.add(new JLabel("choose a right answer!"));
        panel1.add(itemLabel);
        itemLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        //panel是按钮的面板
        panel.setLayout(new GridLayout());
        //设置按钮的行为
        for(int i=1;i<=4;i++){
        jButtons[i].addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
                                          if (((JButton) e.getSource()).getText().equals(rightAnswer)) {
                                              if (dialog == null) {
                                                  dialog = new Dialog(f);
                                                  dialog.add(label);
                                                  dialog.setBounds(600, 300, 600, 200);
                                                  dialog.addWindowListener(new WindowAdapter() {
                                                      @Override
                                                      public void windowClosing(WindowEvent e) {
                                                          timer.start();
                                                          dialog.setVisible(false);
                                                      }
                                                  });
                                              }
                                              dialog.setVisible(true);
                                              label.setText("Success");
                                              timer.stop();
                                              test3.addLearned(itemLabel.getText(),rightAnswer);
                                              handRightAnswer();
                                              count = 10;
                                              //答对
                                              grades+=1;
                                              jLabelGrades.setText("Grades: "+grades);
                                              panel.revalidate();
                                              panel1.repaint();
                                          } else {
                                              wrongShow();
                                              //答错
                                              grades-=2;
                                              if(grades<0) {
                                                  //防止用户操作过快导致计时器无法反应从而产生负数分数
                                                  f.setVisible(false);
                                                  label.setText("LOSS!");
                                                  dialog.setVisible(true);
                                              }
                                              jLabelGrades.setText("Grades: "+grades);
                                          }
                                      }
                                  }
        );}
        //添加按钮
        panel.add(button4);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        f.add(panel1);
        f.add(panel);


        //定时器
        JPanel p = new JPanel();
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.setRows(2);p.setLayout(gridLayout2);
        JLabel jLabel = new JLabel("left " + count + " s");
        jLabel.setSize(500, 125);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        jLabelGrades.setSize(500, 125);
        jLabelGrades.setFont(new Font("Arial", Font.PLAIN, 24));
        timer = new Timer(1000, e -> {
            if ((count--) > 0&&grades>0) {
                jLabel.setText("left " + count + " s");
                f.repaint();
            } else if(grades>0) {
                wrongShow();
                //未作答
                grades -= 1;
                jLabelGrades.setText("Grades: "+grades);
            }
            else{
                //分数低于0
                timer.stop();
                test3.outputVocabulary();
                f.dispose();
            }
        });
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
                test3.outputVocabulary();
                f.dispose();

            }
        });
        p.add(jLabel);
        p.add(jLabelGrades);
        f.add(p);
        f.setVisible(true);
        timer.start();
    }
}
