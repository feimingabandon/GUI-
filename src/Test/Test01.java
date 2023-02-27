package Test;


import com.PublicPrivateKeyAcquisition;
import com.RSAOperation;
import com.UDPChatReceiver;
import com.UDPChatSender;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/*
    目标：编写基于RSA进行数字签名和图形化界面的本地通讯程序。
    要求：1、发送的信息包括原始消息和签名，原始消息通过RSA的私钥加密，签名则是利用SHA散列函数对原始消息进行摘要。
         2、接收到消息后通过SHA的值检测原始消息的完整性。
         3、利用图形化界面完成。
    架构设计：
    整个程序应分为四部分：1、基于RSA的公私钥生成。2、RSA。3、本地聊天。4、图形化界面。
    1、公私钥获取：生成公私钥、序列化公私钥、获取公钥、获取私钥。
    2、RSA包含：加密信息，解密信息，SHA散列值获取，完整性认证。
    3、UDP或者TCP。
    4、GUI。

*/
public class Test01 {
    public static void main(String[] args) throws Exception {
        PublicPrivateKeyAcquisition a = new PublicPrivateKeyAcquisition();
        RSAOperation b = new RSAOperation();
        String str = "你好，邹飞鸣";

        // 产生公私钥
      //  a.produceKeyPair();
        // 序列化公私钥对象
      //  a.serialize(2);
        // 反序列化获取公私钥对象
        KeyPair key1 = a.deserialization(".txt");
        KeyPair key2 = a.deserialization(".txt");
        // 获得私钥
        PrivateKey aPrivate = key1.getPrivate();
        // 获得公钥
        PublicKey aPublic = key1.getPublic();

        // 生成需要发送的东西
        str = b.encryption(str,aPrivate) +" "+ b.encoderSHA(str)+" ";;
        System.out.println(str);


        UDPChatSender udpChatSender = new UDPChatSender(8081);
        udpChatSender.send(str,9999);



        // 模拟接收到了数据后的解密
        String[] s = str.split(" ");
        String str2 = b.decryption(s[0], aPublic);
        System.out.println(str2);

        // 比较完整性
        System.out.println(b.checkIntegrity(str2,s[1]));
    }

}
