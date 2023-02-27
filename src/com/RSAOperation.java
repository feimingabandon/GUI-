package com;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.MessageDigest;


//2、RSA包含：加密信息，解密信息，SHA散列值获取，完整性认证。
public class RSAOperation {

    // 参数1：明文，参数2：私钥，作用：加密原始消息
    public String encryption(String str, Key Key) throws Exception {
        //获取一个加密算法为RSA的加解密器对象cipher。
        Cipher cipher = Cipher.getInstance("RSA");
        //设置为加密模式,并将公钥给cipher。
        cipher.init(Cipher.ENCRYPT_MODE, Key);
        //获得密文
        byte[] secret = cipher.doFinal(str.getBytes());

        //进行Base64编码并返回
        // 注：使用Base64编码方式，是因为加密后的消息为二进制形式的数据，
        // 而我们传输的应该为字符串，所以要用到Base64的编码与解码。（Base64编码可将字节数组转换为字符串，解码为逆过程）
        return new BASE64Encoder().encode(secret);
    }

    // 参数1：密文，参数2：公钥、作用：解密密文
    public String decryption(String secret,Key Key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        //传递私钥，设置为解密模式。
        cipher.init(Cipher.DECRYPT_MODE, Key);
        //解密器解密由Base64解码后的密文,获得明文字节数组
        byte[] b = cipher.doFinal(new BASE64Decoder().decodeBuffer(secret));
        //转换成字符串
        return new String(b);
    }

    // 生成基于原始消息的 SHA 散列值
    public String encoderSHA(String str) throws Exception {
        MessageDigest sha=MessageDigest.getInstance("SHA");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        // 使用digest方法后的数据为二进制数组，需通过encode变成字符串。后面接收到后逆过程可获得相同的字节数组
        return base64en.encode(sha.digest(str.getBytes("utf-8")));
    }

    // 验证消息完整性
    // 比较接收到的SHA摘要和通过接收到的原文产生的SHA摘要是否相同进行判断
    public boolean checkIntegrity(String ReceivedSHA,String CalculatedSHA) throws Exception {

        return (encoderSHA(ReceivedSHA)).equals(CalculatedSHA);

    }


}
