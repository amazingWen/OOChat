package com.zw.net.Demo.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        //连接服务器+端口
        Socket client = new Socket("localhost",8888);
        //接受服务器数据
        DataInputStream dis = new DataInputStream(client.getInputStream());
        String data =  dis.readUTF();
        System.out.println(data);
    }
}
