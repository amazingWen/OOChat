package com.zw.net.Demo.udp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MyServer {
    public static void main(String[] args) throws IOException {
        //创建服务器
        DatagramSocket server = new DatagramSocket(8888);
        //创建容器
        byte [] container = new byte[1024];
        //封装成包
        DatagramPacket packet = new DatagramPacket(container,container.length);
        //接受数据
        server.receive(packet);
        //分析数据
//        byte [] data = packet.getData();
//        int len = packet.getLength();
//        System.out.println(new String(data,0,len));
        int data = convert(packet.getData());
        System.out.println(data);
        //释放
        server.close();
    }
    public static int convert(byte [] data) throws IOException {
        //字节数组转为整数
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        double num = dis.readDouble();
        int number = (int)(num);
        dis.close();
        return number;
    }
}
