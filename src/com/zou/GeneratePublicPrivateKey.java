package com.zou;

import com.PublicPrivateKeyAcquisition;

public class GeneratePublicPrivateKey {
    public static void main(String[] args) {
        // 生成公私钥，要生成两对密钥对。
        try {
            PublicPrivateKeyAcquisition acquisition = new PublicPrivateKeyAcquisition();
            acquisition.produceKeyPair();// 生成密钥对
            acquisition.serialize("Bob");// 序列化密钥对

            acquisition.produceKeyPair();// 生成密钥对
            acquisition.serialize("Alice");// 序列化密钥对

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
