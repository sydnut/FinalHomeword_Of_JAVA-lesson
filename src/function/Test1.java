package function;

import client.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/6
 */
public class Test1 {
    JButton itemLabel = new JButton();
    JLabel jLabel = new JLabel();
    JLabel jLabelGrades = new JLabel();
    private ClientService clientService;
    private Test3 vocabulary;
    private JFrame f;
    private int count = 10;
    private Timer timer;
    private Dialog dialog = null;
    private JLabel label = new JLabel();
    private int grades = 10;
    private String rightAnswer;
    private JTextField blank = new JTextField();


    public Test1(ClientService c, Test3 t) {
        clientService = c;
        vocabulary = t;

    }

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
        label.setText("Failure,the right answer is " + rightAnswer);
        if (count > 0)
            vocabulary.addUnknown(rightAnswer + "(ANSWER_WRONG)", itemLabel.getText());
        else vocabulary.addUnknown(rightAnswer + "(NO_ANSWER)", itemLabel.getText());
        handRightAnswer();
        dialog.setVisible(true);
        count = 10;
        f.revalidate();
        f.repaint();
    }

    private void handRightAnswer() {
        String[] strings = clientService.getTest_1();
        rightAnswer = strings[0];//English
        itemLabel.setText(strings[1]);
        blank.setText(rightAnswer.charAt(0) + "______" + rightAnswer.charAt(rightAnswer.length() - 1));
    }

    public void show() {
        handRightAnswer();
        f = new JFrame();
        f.setBounds(500, 200, 600, 500);
        f.setCursor(new Cursor(Cursor.HAND_CURSOR));
        f.setTitle("Test1");
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(3);
        f.setLayout(gridLayout);
        JPanel panel1 = new JPanel();
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.setRows(2);
        panel1.setLayout(gridLayout1);
        panel1.add(new JLabel("according to the first and last character,fill the whole word"));
        panel1.add(itemLabel);
        f.add(panel1);
        f.add(blank);
        blank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blank.getText().equals(rightAnswer)) {
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
                    vocabulary.addLearned(rightAnswer, itemLabel.getText());
                    handRightAnswer();
                    count = 10;
                    //答对
                    grades += 1;
                    jLabelGrades.setText("Grades: " + grades);
                    panel1.repaint();


                } else {
                    //答错
                    wrongShow();
                    grades -= 2;
                    if (grades < 0) {
                        //防止用户操作过快导致计时器无法反应从而产生负数分数
                        f.setVisible(false);
                        label.setText("LOSS!");
                        dialog.setVisible(true);
                    }
                    jLabelGrades.setText("Grades: " + grades);
                }
            }
        });
        //聚焦文本框时清空
        blank.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                blank.setText(rightAnswer.charAt(0) + "" + rightAnswer.charAt(rightAnswer.length() - 1));
            }
        });


        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((count--) > 0 && grades > 0) {
                    jLabel.setText("left " + count + " s");
                    f.repaint();
                } else if (grades > 0) {
                    wrongShow();
                    //未作答
                    grades -= 1;
                    jLabelGrades.setText("Grades: " + grades);
                } else {
                    //分数低于0
                    timer.stop();
                    vocabulary.outputVocabulary();
                    f.dispose();
                }
            }
        });
        JPanel panel = new JPanel();
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.setRows(2);
        panel.setLayout(gridLayout2);
        jLabel = new JLabel("left " + count + " s");
        jLabel.setSize(500, 125);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        jLabelGrades.setSize(500, 125);
        jLabelGrades.setFont(new Font("Arial", Font.PLAIN, 24));

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
                vocabulary.outputVocabulary();
                f.dispose();
            }
        });
        panel.add(jLabel);
        panel.add(jLabelGrades);
        f.add(panel);
        timer.start();
        f.setVisible(true);


    }

}
