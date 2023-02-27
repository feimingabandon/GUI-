package com.zou;

import com.*;


public class Bob {
    GUIChat guiChat = null;                 // 获得图形化界面的对象，用来作为启动图形化界面的变量
    UDPChatSender sender = null;            // 发送消息所用到的变量
    RSAOperation rsaOperation = null;       // 数字签名，加密，解密，完整性认证，SHA摘要所用到的变量
    UDPChatReceiver receiver = null;        // 接收消息所用到的变量

    // 初始化、即启动图形化界面，启动接收线程。
    public void init(){

        // 开启图形化界面
        guiChat = new GUIChat();

        // 设置好发送所需要的信息
        sender = new UDPChatSender(9999);

        // new出其对象
        rsaOperation = new RSAOperation();

        // 初始化图形化界面
        guiChat.init("Bob",8881,sender,rsaOperation);


        // 先开启接收线程
        receiver = new UDPChatReceiver(9991,rsaOperation,guiChat,"Alice");
        new Thread(receiver).start();

        // 尝试连接用户
        initConnection();

    }

    // 尝试连接用户、即被动等待 A 发送过来的连接信息，接收到后再返回一个回执信息给A
    public void initConnection(){
        boolean flag=true;  // 连接成功后用来退出连接的变量
        int i=0;
        // 不主动发信息，先获得对方的连接信息然后再回复对方一个信息
        // 循环中内容是为了将尝试连接这个消息进行动态变化
        while (flag) {
            i=(++i)%3;

            try {
                Thread.sleep(1000); // 休眠一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guiChat.text_Ciphertext.setText("");  //清空文本域

            if (i==0)
                // 设置文本域显示连接标志
                guiChat.text_Ciphertext.setText("尝试连接中.");

            else if (i==1)
                // 设置文本域显示连接标志
                guiChat.text_Ciphertext.append("尝试连接中..");

            else if (i==2)
                // 设置文本域显示连接标志
                guiChat.text_Ciphertext.append("尝试连接中...");

            if (receiver.receiveData.equals("请求连接")){
                flag = false;

                // 发送回执信息
                sender.send("请求连接 B ",8881);

                guiChat.text_Ciphertext.setText("");  //清空文本域，即设置文本域的内容为空
                guiChat.text_Ciphertext.setText("连接成功！\n");
            }


        }
    }
    public static void main(String[] args) {
        new Bob().init();
    }
}
