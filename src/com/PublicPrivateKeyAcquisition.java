package com;

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

//1、公私钥获取：生成公私钥、序列化公私钥、获取公钥、获取私钥。
public class PublicPrivateKeyAcquisition {

    public KeyPair keyPair;

    // 产生公私钥。并初始化RSA算法。
    public void produceKeyPair() throws NoSuchAlgorithmException {
        //使用RSA算法获得密钥对生成器对象keyPairGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //设置密钥长度为1024
        keyPairGenerator.initialize(1024);
        //生成密钥对
        keyPair = keyPairGenerator.generateKeyPair();
    }

    // 即 将存储了公私钥的对象存储进文件中，方便下次使用
    // 序列化对象,name是作为序列化后文件的名字
    public void serialize(String name) throws Exception {
        String str = name+".txt";
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(str));
        oos.writeObject(keyPair);
        oos.close();
    }
    // 通过相应的文件取出对应的公私钥对象，可通过公私钥对象获得公私钥。
    // 反序列化
    public KeyPair deserialization(String route) throws IOException, ClassNotFoundException {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(route+".txt"));
        Object obj=ois.readObject();
        ois.close();
        return (KeyPair) obj;
    }

    // 获得公钥
    public Key getPublicKey(){
        return keyPair.getPublic();
    }

    // 获得私钥
    public Key getPrivateKey(){
        return keyPair.getPrivate();
    }

}
