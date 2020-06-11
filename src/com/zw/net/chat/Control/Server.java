package com.zw.net.chat.Control;

import com.zw.net.chat.Dao.User;
import com.zw.net.chat.Util.CloseUtil;
import com.zw.net.chat.Util.DateStringUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
* 创建服务器端
* */
public class Server {
    private List<MyChannel> all = new ArrayList<MyChannel>();
    private List<String> allUser = new ArrayList<String>();
    public static String CONSTANT_SERVER_MSG = "10000";
    public static String CONSTANT_UPDATE_FRIEND_LIST = "10001";
    public static String CONSTANT_MSG_ALL = "10002";
    public static String CONSTANT_MSG = "10003";
    public static String CONSTANT_LOGIN_OUT = "10004";
    public static String CONSTANT_IMAGE_ONE = "10005";
    public static String CONSTANT_IMAGE_ALL = "10006";


    public static void main(String[] args) throws IOException {
        //创建服务器+端口
        new Server().start();
    }
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        //接受客户端连接 阻塞式
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端建立连接");
            MyChannel Channel = new MyChannel(socket);
            all.add(Channel);
            new Thread(Channel).start();
        }
    }

    private class MyChannel implements Runnable{
        private DataOutputStream dos;
        private DataInputStream dis;
        private FileOutputStream fos = null;
        private FileInputStream fis = null;

        private byte[] inputByte = new byte[1024];
        private byte[] sendBytes = new byte[1024];

        private boolean isrunning=true;
        private InputStream ins;
        private OutputStream out;
        private User user;
        private Socket server;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public MyChannel(Socket socket) {
            try {
                server = socket;
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                ins = socket.getInputStream();
                out = socket.getOutputStream();
                user = new User();
                user.setUser_name(dis.readUTF());
                allUser.add(user.getUser_name());
                this.send(user.getUser_name());
                this.send(String.format("欢迎您(%s)进入群聊！【系统消息】", user.getUser_name()));
                this.sendOthers(CONSTANT_SERVER_MSG);
                this.sendOthers("您的好友("+user.getUser_name()+")已上线【系统消息】");
                //发送已上线好友信息
                StringBuilder sb = new StringBuilder();
                if (allUser!=null && allUser.size()>0){
                    boolean flag = false;
                    for (String string : allUser) {
                        if (flag) {
                            sb.append(",");
                        } else {
                            flag = true;
                        }
                        sb.append(string);
                    }
                    System.out.println(sb.toString()); //01,02,03
                }
                this.send(CONSTANT_UPDATE_FRIEND_LIST);   //发送更新标识
                send(sb.toString());
                this.sendOthers(CONSTANT_UPDATE_FRIEND_LIST);   //发送更新标识
                sendOthers(sb.toString());

            } catch (IOException e) {
                isrunning = false;
                CloseUtil.closeAll(dos,dis);
            }
        }
        private String receive(){
            String msg = null;
            try {
                msg = dis.readUTF();
                if (msg!=null && !msg.equals("")){
                    System.out.println("服务器收到"+user.getUser_name()+msg);
                    //表示即将收到的是图片文件
                    if (msg.startsWith("#image_content#")){
                        //保存文件
                        System.out.println("保存文件");

//                        BufferedInputStream bin = new BufferedInputStream(ins);

                        File dir = new File(System.getProperty("user.dir")+"\\src\\Image\\Server\\"+user.getUser_name());

                        if(!dir.exists()){
                            dir.mkdir();    //文件夹不存在,创建文件夹
                        }
                        //解析字符串
                        String [] strArr = msg.split("#");
                        String  fileType = strArr[2];   //文件类型
                        String receiveUser = strArr[3];    //接收方
                        int fileLength = Integer.parseInt(strArr[4]);  //文件长度
                        String imageName = new String(DateStringUtil.getTimeStr2() +"." + fileType);    //保存文件名
                        File file = new File(dir, imageName);

//                        FileOutputStream fout = new FileOutputStream(file);

                        fos = new FileOutputStream(file);
                        System.out.println("开始接收数据...");
                        int length = 0;
                        int Size_len=0; //约定读取长度等于文件长度时，跳出循环，防止阻塞！！！
                        while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                            System.out.println(length);
                            fos.write(inputByte, 0, length);
                            fos.flush();
                            Size_len = Size_len+length;
                            System.out.println(Size_len);
                            if (Size_len == fileLength){
                                break;
                            }
                        }
//                        ins.close();
//                        ins.reset();
//                        ins = server.getInputStream();
//                        dis = new DataInputStream(ins);
//                        inputByte.notifyAll();
                        System.out.println("完成接收");



                        //发送文件

                        System.out.println("发送文件");
                        String s = String.valueOf(fileLength); //文件长度
                        sendImageToUser(file, receiveUser, imageName, s);


                    }else {
                        //私聊消息
                        if (msg.startsWith("@")){
                            String[] split = msg.split(":", 2);
                            String name = split[0].substring(1);
                            String message = split[1];
                            System.out.println(name);
                            sendOne(name, message);     //发送

                        }else {
                            //群聊消息
                            sendOthers(CONSTANT_MSG_ALL);
                            sendOthers(user.getUser_name()+":"+msg);
                            send(CONSTANT_MSG_ALL);
                            send(user.getUser_name()+":"+msg);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("异常！" + user.getUser_name()+"已下线【系统消息】");

                sendOthers(CONSTANT_LOGIN_OUT);
                sendOthers("您的好友("+user.getUser_name()+")已下线【系统消息】");
                sendOthers(user.getUser_name());
                allUser.remove(user.getUser_name());
                all.remove(this);
                isrunning = false;
                CloseUtil.closeAll(dis);

            }
            return msg;
        }

        private void sendImage(File file){
            if (file!=null){
                try {
                    fis = new FileInputStream(file);
                    int length =0;
                    while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                        dos.write(sendBytes, 0, length);
                        dos.flush();
                    }
//                    server.shutdownOutput();
//                    Thread.currentThread().sleep(2000);
//                    out = server.getOutputStream();
//                    dos = new DataOutputStream(out);
                } catch (IOException e) {
                    System.out.println("发送image失败");

                    isrunning = false;

                    CloseUtil.closeAll(dos);
                    all.remove(this);

                }
            }
        }

        private void sendImageToUser(File file, String receiveUser, String imageName, String fileLength){
            if (receiveUser.startsWith("@")){
                String name = receiveUser.substring(1,receiveUser.length()-1);
                sendOne(name," ");
                for (MyChannel tem:all){
                    if (tem.getUser().getUser_name().equals(user.getUser_name())){

                        tem.send(CONSTANT_IMAGE_ONE);
                        tem.send(imageName+"#"+fileLength);   //发送文件名+文件长度
                        tem.sendImage(file);
//                            tem.server.shutdownOutput();    //通知客户端文件已发送完
                    }
                    if (all.size() == 1){   //如果只有一个用户则认为是自己给自己发送图片，需要等待一段时间，避免接受失败！
                        try {
                            Thread.currentThread().sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (tem.getUser().getUser_name().equals(name)){
                        tem.send(CONSTANT_IMAGE_ONE);
                        tem.send(imageName+"#"+fileLength);   //发送文件名+文件长度
                        tem.sendImage(file);
//                            tem.server.shutdownOutput();


                    }
                }
            }else if (receiveUser.equals("all")){
                System.out.println("image send to all");
                for (MyChannel tem:all){
                    tem.send(CONSTANT_MSG_ALL);
                    tem.send(user.getUser_name()+":");
                    tem.send(CONSTANT_IMAGE_ALL);
                    tem.send(imageName+"#"+fileLength);   //发送文件名+文件长度
                    //发送文件
                    tem.sendImage(file);
//                        tem.server.shutdownOutput();


                }
            }else {
                System.out.println(receiveUser);
            }


        }

        private void sendOne(String name, String msg){
            for (MyChannel tem:all){
                if (tem.getUser().getUser_name().equals(user.getUser_name())){
                    tem.send(CONSTANT_MSG);
                    tem.send("@我("+user.getUser_name()+")对("+name+")说:"+msg);

                }
                if (tem.getUser().getUser_name().equals(name)){
                    tem.send(CONSTANT_MSG);
                    tem.send("@("+user.getUser_name()+")对我("+name+")说:"+msg);
                }
            }
        }
        private void send(String msg){
            if (msg!=null&&!msg.equals("")){
                try {
                    dos.writeUTF(msg);
                    dos.flush();
                } catch (IOException e) {
                    System.out.println("发送失败100");

                    isrunning = false;
                    CloseUtil.closeAll(dos);
                    all.remove(this);
                }
            }
        }
        private void sendOthers(String msg){

            for (MyChannel other:all){
                if (other == this){
                    continue;
                }
                other.send(msg);
            }
        }


        @Override
        public void run() {
            while (isrunning){
                receive();
            }
        }
    }
}
