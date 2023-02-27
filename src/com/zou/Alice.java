package com.zou;


import com.*;

/*
    目标：编写基于RSA进行数字签名和图形化界面的本地通讯程序。
    要求：1、发送的信息包括原始消息和签名，原始消息通过RSA的私钥加密，签名则是利用SHA散列函数对原始消息进行摘要。
         2、接收到消息后通过SHA的值检测原始消息的完整性。
         3、利用图形化界面完成。
    架构设计：
    整个程序应分为四部分：1、基于RSA的公私钥生成。2、RSA。3、本地聊天。4、图形化界面。
    1、公私钥获取：生成公私钥、序列化公私钥、获取公钥、获取私钥。（PublicPrivateKeyAcquisition类）
    2、RSA包含：加密信息，解密信息，SHA散列值获取，完整性认证。（RSAOperation类）
    3、UDP。
    4、GUI。

*/

// 开启本地聊天
/*
准备工作：
        1、先生成自己的公私钥并序列化保存（这一步没有在下面完成，而是要手动运行GeneratePublicPrivateKey类）
        2、开启聊天软件，等待与对方进行连接
        3、连接成功，进行加密通信
 */
public class Alice {
    GUIChat guiChat = null;
    UDPChatSender sender = null;
    RSAOperation rsaOperation = null;
    UDPChatReceiver receiver = null;
    public void init(){

        // 开启图形化界面
        guiChat = new GUIChat();
        sender = new UDPChatSender(8888);
        rsaOperation = new RSAOperation();

        guiChat.init("Alice",9991,sender,rsaOperation);

        // 先开启接收线程
        receiver = new UDPChatReceiver(8881,rsaOperation,guiChat,"Bob");
        new Thread(receiver).start();

        // 尝试连接用户
        initConnection();

    }

    // 尝试连接用户
    public void initConnection(){
        boolean flag=true;
        int i=0;


        // 隔 3 秒发送一次信息、等待回执信息、获得回执后接束发送。
        while (flag) {
                i=(++i)%3;

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    guiChat.text_Ciphertext.setText("");  //清空文本域
                if (i==1)
                    // 设置文本域显示连接标志
                    guiChat.text_Ciphertext.setText("尝试连接中.");

                else if (i==2)
                    // 设置文本域显示连接标志
                    guiChat.text_Ciphertext.append("尝试连接中..");

                else if (i==0) {
                    // 设置文本域显示连接标志
                    guiChat.text_Ciphertext.append("尝试连接中...");
                    // 发送信息
                    sender.send("请求连接 A ",9991);
                }
            if (receiver.receiveData.equals("请求连接")){
                flag = false;
                guiChat.text_Ciphertext.setText("");  //清空文本域
                guiChat.text_Ciphertext.setText("连接成功！\n");

            }
        }
    }

    public static void main(String[] args) {
        new Alice().init();
    }
}
