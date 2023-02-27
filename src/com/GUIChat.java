package com;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;

public class GUIChat extends JFrame {

    public JTextArea text_Ciphertext; // 展示密文的文本框
    public JTextArea text_Plaintext;  // 展示明文的文本框
    public JTextField text_Input;     // 输入文本框
    public JButton button_send;       // 发送按钮
    public JScrollPane jScrollPane_C;   // 滚动面板有两个、且放在面板中
    public JScrollPane jScrollPane_P;   // 滚动面板有两个、且放在面板中
    public JPanel jPanel;             // 面板只有一个、且放在滚动面板中中
    Key privateKey=null;                // 自己的私钥

    // name是聊天者身份，hePort，对方是端口号，send，是发送按钮点击时需要用的此类进行发送消息
    // 此方法是生成图形化界面
    public void init(String myName, int hePort, UDPChatSender send,RSAOperation rsa){

        // 设置生成窗体的位置，宽高
        this.setBounds(600,200,800,600);
        //获得一个容器，只有这样设置颜色才会生效
        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.yellow);

        //绝对布局，依照设定的x，y坐标定位布局组件
        contentPane.setLayout(null);

        // 生成面板
        jPanel = new JPanel();

        jPanel.setBackground(Color.black);// 设置面板颜色
        jPanel.setLayout(null);//设置面板的布局类型

        // 设置面板大小
        jPanel.setBounds(0,0,800,600);


        // 生成两个文本域、一个文本框、一个按钮
        text_Ciphertext=new JTextArea();   // 20为超过20个字则换行
        text_Plaintext=new JTextArea(20,10);
        text_Input = new JTextField(20);             // 20为文本框的高度
        button_send = new JButton("发送");

        // 设置文本域中字体的属性，行楷，字体类型（加粗等，这里是标准），大小
        text_Ciphertext.setFont(new Font("行楷",Font.PLAIN,20));
        text_Plaintext.setFont(new Font("楷书",Font.PLAIN,20));

        // 设置文本框和按钮的位置，以生成的（即生成的窗体）JFrame的左上角为（0，0）开始设置的位置
         text_Input.setBounds(400,535,300,30);
         button_send.setBounds(700,535,100,30);


        // 生成滚动面板、并将对应的文本域放入
        jScrollPane_C = new JScrollPane(text_Ciphertext);
        jScrollPane_P = new JScrollPane(text_Plaintext);

        // 设置滚动面板在面板中的位置
        jScrollPane_C.setBounds(0,0,400,565);
        jScrollPane_P.setBounds(400,0,400,535);


        // 将滚动面板、按钮放入面板
        jPanel.add(jScrollPane_C);
        jPanel.add(jScrollPane_P);
        jPanel.add(text_Input);
        jPanel.add(button_send);

        //将面板放入JFarm中
        contentPane.add(jPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//点 x 后直接退出

        // 设置窗口大小固定
        setResizable(false);
        // 设置窗口名称
        setTitle(myName);
        // 设置可见性
        setVisible(true);

        // 从本地的序列化文件中获得自己的私钥
        PublicPrivateKeyAcquisition acquisition = new PublicPrivateKeyAcquisition();
        try {
            // 获取私钥
            privateKey = acquisition.deserialization(myName).getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 按钮事件监听器，即点击按钮后会发生文本框中的内容
        button_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text_encryption="";
                // 先获取文本框内容
                String text = text_Input.getText();

                // 判断文本框是否存在内容，如果存在则发送
                if (!text.equals("")){

                    try {

                        // 将要发送的消息显示在右边文本域中
                        text_Plaintext.append("\n" + myName + ": " + text + "\n");
                        // 加密原消息
                        text_encryption = rsa.encryption(text, privateKey);

                        // 给加密后的消息添加SHA摘要
                        text_encryption = text_encryption+" "+rsa.encoderSHA(text)+" ";
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    // 利用UDP发生内容给指定对象
                    send.send(text_encryption,hePort);

                    // 最后清空文本框
                    text_Input.setText("");
                }
            }
        });
    }
}










