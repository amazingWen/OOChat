package com.zw.net.chat.Control;

import com.zw.net.chat.Dao.User;
import com.zw.net.chat.View.UserView;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
*输入流和输出流应该彼此独立
* */
public class Client {
    private User user;
    private Socket socket;
    private Send send;
    private Receive receive;

    public Client(){

    }

    public Client(User user, UserView userView) {
        this();
        this.user = user;
        //连接服务器+端口
        Socket client = null;
        try {
            client = new Socket("localhost",9999);
            socket=client;
            send = new Send(client, this.user);
            receive = new Receive(client, userView);
            new Thread(send).start();  //一条路径
            new Thread(receive).start();  //一条路径
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException {
        System.out.println("请输入名称：");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        if (name.equals("")){
            return;
        }
        User us = new User("2eee","ddd","dw");
//        Client client = new Client(us);
    }

    public Send getSend() {
        return send;
    }

    public void setSend(Send send) {
        this.send = send;
    }

    public Receive getReceive() {
        return receive;
    }

    public void setReceive(Receive receive) {
        this.receive = receive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
