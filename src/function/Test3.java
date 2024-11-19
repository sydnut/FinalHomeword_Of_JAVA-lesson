package function;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/1
 */
public class Test3 {
    public String learned_Vocabulury = "C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\vocabulary\\";
    private String unknown_Vocabulury = "C:\\Users\\31374\\IdeaProjects\\FinalHomework\\src\\vocabulary\\";
    private HashMap<String, String> BufferedMap_1 = new HashMap<>();
    private HashMap<String, String> BufferedMap_2 = new HashMap<>();
    //构造函数中读入单词表
    public Test3(String id) throws IOException {
        learned_Vocabulury += (id + "_learned.txt");
        unknown_Vocabulury += (id + "_unknown.txt");
    }
    public void addLearned(String en, String cn) {
        BufferedMap_1.put(en, cn);
    }

    public void addUnknown(String en, String cn) {
        BufferedMap_2.put(en, cn);
    }

    //将缓冲区（本次使用软件产生的词汇）输出到文件中
    public void outputVocabulary() {
        //rw，如果不存在文件则创建文件
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(learned_Vocabulury, "rw")) {
            randomAccessFile.seek(randomAccessFile.length());
            BufferedMap_1.forEach((a, b) -> {
                try {
                    randomAccessFile.write(a.getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write("\r\n".getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write(b.getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write("\r\n".getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(unknown_Vocabulury, "rw")) {
            randomAccessFile.seek(randomAccessFile.length());
            BufferedMap_2.forEach((a, b) -> {
                try {
                    randomAccessFile.write(a.getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write("\r\n".getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write(b.getBytes(StandardCharsets.UTF_8));
                    randomAccessFile.write("\r\n".getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        //清空缓冲区
        BufferedMap_1.clear();
        BufferedMap_2.clear();
    }

    //从文件中读出单词
    public void checkVocabulary() {
        JFrame frame = new JFrame("vocabulary");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frame.setBounds(500, 200, 600, 500);
        JScrollPane jScrollPane = new JScrollPane();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(new FileInputStream(learned_Vocabulury)))) {
            JPanel rowPanel1 = new JPanel();
            rowPanel1.setLayout(new FlowLayout(FlowLayout.LEFT)); // 水平排列
            rowPanel1.add((new JLabel("Learned vocabulary:")));
            jPanel.add(rowPanel1);
            while (scanner.ready()) {
                String a = scanner.readLine();
                String b = scanner.readLine();
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 水平排列
                rowPanel.add(new JLabel(a));
                rowPanel.add(new JLabel(b));
                jPanel.add(rowPanel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(new FileInputStream(unknown_Vocabulury)))) {
            JPanel rowPanel1 = new JPanel();
            rowPanel1.setLayout(new FlowLayout(FlowLayout.LEFT)); // 水平排列
            rowPanel1.add((new JLabel("Unknown vocabulary:")));
            jPanel.add(rowPanel1);
            while (scanner.ready()) {
                String a = scanner.readLine();
                String b = scanner.readLine();
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 水平排列
                rowPanel.add(new JLabel(a));
                rowPanel.add(new JLabel(b));
                jPanel.add(rowPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jScrollPane.setViewportView(jPanel);
        frame.add(jScrollPane);

        frame.setVisible(true);
    }
}