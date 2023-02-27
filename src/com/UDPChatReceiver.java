package com;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.Key;


// 继承此接口，表示另外开启一个线程，线程会执行run（）方法中的代码。
public class UDPChatReceiver implements Runnable{

    DatagramSocket socket =null;  // 用来创建接收端口号的变量
    public int myPort;       // 端口号
    RSAOperation rsa = null;   // 获得数据后用此类中的方法进行解密
    GUIChat gui = null;      // 获得数据且解密后，用此类显示在图形化界面中
    String heName = "";      // 对方的名字
    Key publicKey = null; // 对方公钥

    public String receiveData="";       // 存储接收到的字符串

    // 生成接收端口的进程
    public UDPChatReceiver(int myPort,RSAOperation rsa,GUIChat gui,String heName){
        this.myPort=myPort;
        this.rsa = rsa;
        this.gui = gui;
        this.heName = heName;

        //在本机地址上建立端口，以接收
        try {
            socket=new DatagramSocket(myPort);

            // 获取对方公钥
            publicKey = new PublicPrivateKeyAcquisition().deserialization(heName).getPublic();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        // 通过循环能够一直保持接收，因为下方接收消息是阻塞式，即只有接收到了才会执行下一步，所以才重新开启一个线程去作为接收消息的程序。
        while (true) {
            try {

                //接收数据包
                byte[] container = new byte[1024];
                //构造一个 DatagramPacket用于接收长度的数据包 length 。
                DatagramPacket packet = new DatagramPacket(container, 0, container.length);
                //接收来自DatagramPacket的数据包,阻塞式
                socket.receive(packet);

                //获得包裹中的数据
                receiveData = new String(packet.getData(), 0, packet.getData().length);

                // 获取到数据后，解密数据，清空GUI中的文本域，然后重新赋值文本域。
                decryptionAndDisplay(receiveData);

                // 释放资源的判定
                if(receiveData.equals("exit_0"))break;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //关闭流
        socket.close();

    }

    // 解密数据并显示数据
    public void decryptionAndDisplay(String str){

        try {
            String[] s = str.split(" "); // 分割接收到的信息，方便解读

            if (s[0].equals("请求连接")) {
                receiveData = s[0];
            }else {

                // 解密密文
                String str2 = rsa.decryption(s[0], publicKey);

                // 将获得的密文与SHA显示在左边的文本域中，记得之前的文本不能删除了。
                // 可直接在原基础上插入内容
                gui.text_Ciphertext.append("密文：\n" + s[0] + "\n\n" + "SHA摘要：" + s[1] + "\n\n" + "完整性认证：" + rsa.checkIntegrity(str2, s[1]) + "\n" + "------------------------------------------------" + "\n");

                // 将解密后的明文显示在右边文本域中
                gui.text_Plaintext.append("\n" + heName + ": " + str2 + "\n");
                }
            } catch(Exception e){
                e.printStackTrace();
        }

    }
}
