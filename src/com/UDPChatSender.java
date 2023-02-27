package com;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPChatSender  {
    public int myPort;          // 我自己发送的端口号
    DatagramSocket socket=null;

    public UDPChatSender(int myPort){
        this.myPort=myPort;
        //建立一个Socket
        try {
            socket=new DatagramSocket(myPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public void send(String str,int hePort){//str就是要发送的数据、hePort对方接收的端口号

        if (!str.equals("exit_0")) { // 这是一个关闭IO流的操作，一般是结束时关闭
            try {
                //获取本机地址
                InetAddress inetAddress = InetAddress.getByName("localhost");

                //参数：数据(Byte类型)，发送数据的长度，要发给谁
                DatagramPacket packet = new DatagramPacket(str.getBytes(), 0, str.getBytes().length, inetAddress, hePort);

                socket.send(packet);//发送包
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            //关闭流
            socket.close();
        }
    }
}
