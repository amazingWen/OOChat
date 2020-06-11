package com.zw.net.Demo.udp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MyClient {
    public static void main(String[] args) throws IOException {
        //创建服务端 + 端口
        DatagramSocket client = new DatagramSocket(6666);
        //准备发送的数据
//        String msg = "udp";
//        byte [] data = msg.getBytes();
        double num = 99.99;
        byte [] data = convert(num);
        //打包
        DatagramPacket packet = new DatagramPacket(data,data.length, new InetSocketAddress("localhost",8888));
        //发送
        client.send(packet);
        //释放
        client.close();
    }
    public static byte[] convert(double num) throws IOException {

        byte [] data = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeDouble(num);
        dos.flush();
        data = bos.toByteArray();
        dos.close();
        return data;
    }
}
