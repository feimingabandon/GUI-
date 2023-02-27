package Test;

import com.PublicPrivateKeyAcquisition;
import com.RSAOperation;
import com.UDPChatReceiver;
import com.UDPChatSender;

import java.lang.reflect.Array;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class Test02 {
    public static void main(String[] args) throws Exception {
        PublicPrivateKeyAcquisition a = new PublicPrivateKeyAcquisition();
        RSAOperation b = new RSAOperation();
        String str = "你好，甄士隐";

        // 产生公私钥
      //  a.produceKeyPair();
        // 序列化公私钥对象
      //  a.serialize(1);
        // 反序列化获取公私钥对象
        KeyPair key1 = a.deserialization(".txt");
        KeyPair key2 = a.deserialization(".txt");
        // 获得私钥
        PrivateKey aPrivate = key1.getPrivate();
        // 获得公钥
        PublicKey aPublic = key1.getPublic();

        // 生成需要发送的东西
        str = b.encryption(str,aPrivate) +" "+ b.encoderSHA(str)+" "; //最后使用个空格，是为了切割接收时存在的字节数组默认值
        System.out.println(str);


        UDPChatReceiver ud = new UDPChatReceiver(9999,null,null,"");
        new Thread(ud).start();
        Thread.sleep(5000);


        if (!ud.receiveData.equals("")) {
            str = ud.receiveData;
            System.out.println(str);
        }


        // 模拟接收到了数据后的解密
        String[] s = str.split(" ");
        String str2 = b.decryption(s[0], aPublic);
        System.out.println(str2);
        System.out.println(Arrays.toString(s));

        // 比较完整性
        System.out.println("S1:"+s[1].length());
        System.out.println("S2:"+str2.length());
        System.out.println(b.encoderSHA(str2).equals(s[1]));
        System.out.println(b.checkIntegrity(str2,s[1]));
    }
}
