package com.zw.net.chat.Dao;

public class User {
    private String user_id;
    private String user_password;
    private String user_name;
    private String msg = null;
    private int msgClass = -1;
    private String sendTo = null;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public int getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(int msgClass) {
        this.msgClass = msgClass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User() {
    }

    public User(String user_id, String user_password, String user_name) {
        this.user_id = user_id;
        this.user_password = user_password;
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
