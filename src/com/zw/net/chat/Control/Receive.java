package com.zw.net.chat.Control;

import com.zw.net.chat.Dao.User;
import com.zw.net.chat.Util.CloseUtil;
import com.zw.net.chat.Util.DateStringUtil;
import com.zw.net.chat.View.UserView;

import java.io.*;
import java.net.Socket;

public class Receive implements Runnable {
    //输入流
    private DataInputStream dis;
    private FileOutputStream fos=null;
    private Socket socket;
    byte[] inputByte = new byte[1024];
    private boolean isrunning = true;
    private UserView userView;
    private InputStream ins;
    private User user;

    public Receive(){

    }
    public Receive(Socket socket, UserView userView){
        this();
        this.socket = socket;
        this.userView = userView;
        try {
            //接受欢迎信息
            ins = socket.getInputStream();
            dis = new DataInputStream(socket.getInputStream());
            String name = dis.readUTF();
            user = new User();
            user.setUser_name(name);
            String  str=dis.readUTF();
            if (!str.equals("")){

                UserView.FontAttrib fontAttrib = new UserView.FontAttrib(str);
                userView.insert(fontAttrib);
            }


        } catch (IOException e) {
            isrunning = false;
            CloseUtil.closeAll(dis);

        }
    }
    public void receive(){
        String data = null;
        try {
            String tem = dis.readUTF();
            System.out.println("接受命令"+tem);
            if (!tem.equals("") && tem!=null){      //消息不为空
                if (tem.equals(Server.CONSTANT_SERVER_MSG)){    //如果是系统消息
                    data = dis.readUTF();   //读数据
                    UserView.FontAttrib fontAttrib = new UserView.FontAttrib(data);
                    userView.insert(fontAttrib);
                }else if (tem.equals(Server.CONSTANT_UPDATE_FRIEND_LIST)){  //更新好友列表消息
                    String str = dis.readUTF(); //读字符串
                    if (str!=null && !str.equals("")){
                        //更新好友列表
                        String[] split = str.split(",");
                        for (String s :split){
                            if (userView.getListModel1().contains(s)){
                                continue;
                            }else {
                                userView.getListModel1().addElement(s);
                            }
                        }
                    }
                    //好友下线消息
                }else if (tem.equals(Server.CONSTANT_LOGIN_OUT)){
                    data = dis.readUTF();
                    UserView.FontAttrib fontAttrib = new UserView.FontAttrib(data);
                    userView.insert(fontAttrib);
                    data = dis.readUTF();
                    userView.getListModel1().removeElement(data);
                    //接受文字给所有人的消息
                }else if (tem.equals(Server.CONSTANT_MSG_ALL)){
                    data = dis.readUTF();
                    UserView.FontAttrib fontAttrib = new UserView.FontAttrib(data);
                    userView.insert(fontAttrib);
                    //私聊消息
                }else if (tem.equals(Server.CONSTANT_MSG)){
                    data = dis.readUTF();
                    UserView.FontAttrib fontAttrib = new UserView.FontAttrib(data);
                    userView.insert(fontAttrib);
                    //图片消息
                }else if (tem.equals(Server.CONSTANT_IMAGE_ONE)||tem.equals(Server.CONSTANT_IMAGE_ALL)){
                    String fileString = dis.readUTF();
                    System.out.println("此处有filename"+fileString);   //打印文件名+文件长度
                    String[] split = fileString.split("#");
                    ReceiveImage(tem, split[0],split[1]);
                } else {
                    System.out.println("未被捕获的"+tem);
                }
            }

        } catch (IOException e) {
            System.out.println("错误");
            isrunning = false;
            CloseUtil.closeAll(dis);
        }

    }
    private void ReceiveImage(String whoReceive, String fileName, String fileLength){
        try {
            System.out.println("创建用户本地文件夹");
            File dir = new File(System.getProperty("user.dir")+"\\src\\Image\\User\\"+user.getUser_name());
            if(!dir.exists()){
                dir.mkdir();    //文件夹不存在,创建文件夹
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            System.out.println("开始接收数据...");
            int length = 0;
            int Size_len =0;
            int i = Integer.parseInt(fileLength);   //约定读取长度等于文件长度时，跳出循环，防止阻塞！！！
            while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                System.out.println(length);
                fos.write(inputByte, 0, length);
                fos.flush();
                Size_len = Size_len+length;
                System.out.println(Size_len);
                if (Size_len == i){
                    break;
                }
            }

            System.out.println("完成接收");

            //显示图片
            userView.insertIcon(file, whoReceive);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //线程体
    @Override
    public void run() {

        while (isrunning){
            receive();
        }
    }
}
