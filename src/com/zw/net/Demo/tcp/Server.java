package com.zw.net.Demo.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
* 创建服务器端
* */
public class Server {
    public static void main(String[] args) throws IOException {
        //创建服务器+端口
        ServerSocket serverSocket = new ServerSocket(8888);
        //接受客户端连接 阻塞式
        Socket socket = serverSocket.accept();
        System.out.println("一个客户端建立连接");
        //发送数据
        String msg = "hello";
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(msg);
        dos.flush();

    }

}
