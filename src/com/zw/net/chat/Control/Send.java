package com.zw.net.chat.Control;

import com.zw.net.chat.Dao.User;
import com.zw.net.chat.Util.CloseUtil;

import java.io.*;
import java.net.Socket;

public class Send implements Runnable{

    private DataOutputStream dos;
    private FileInputStream fis=null;
    byte[] sendBytes = new byte[1024];

    private boolean isrunning = true;
    private User user;
    private OutputStream out;
    private Socket client;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Send(){

    }
    public Send(Socket socket, User user){
        this();
        try {
            this.user = user;
            this.client = socket;
            out = socket.getOutputStream();
            dos = new DataOutputStream(out);

            sendUserName();
        } catch (IOException e) {
            System.out.println("Send构造异常");

            CloseUtil.closeAll(dos);
            isrunning = false;
        }
    }

    private void sendUserName(){
        try {
            if (user.getUser_name() != null &&! user.getUser_name().equals("")){
                dos.writeUTF(user.getUser_name());

                dos.flush();
            }
        } catch (IOException e) {
            System.out.println("发送失败000");

            isrunning = false;
            CloseUtil.closeAll(dos);
        }
    }
    private String getMsgFromUser(){

        String info = null;
        if (user.getMsg()!=null&& !user.getMsg().equals("")){
            System.out.println("msg not null");
            info = user.getMsg();
        }
        if (info!=null){
            System.out.println(info);
        }

        return info;
    }
    public void send(String info){
        try {
            if (info != null &&!info.equals("")){
                dos.writeUTF(info);
                dos.flush();
                user.setMsg("");    //清空待发送消息
            }
        } catch (IOException e) {
            System.out.println("发送失败001");

            isrunning = false;
            CloseUtil.closeAll(dos);
        }

    }
    public void sendImage(File file){
        try {

            int length = 0;
            fis = new FileInputStream(file);

            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                dos.write(sendBytes, 0, length);
                dos.flush();
            }

            }catch(IOException e){
                System.out.println(e.getMessage());
                System.out.println("发送失败002");
                isrunning = false;
                CloseUtil.closeAll(dos, fis);
            }
        }

    @Override
    public void run() {

    }
}
