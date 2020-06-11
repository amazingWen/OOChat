package com.zw.net.chat.Dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DaoFactory {
    private static String userName;
    private static String password;
    private static String id;

    private static String connectString = "";
    static {
        try {
            connectString = "jdbc:mysql://localhost:3306/qqpro";
            password = "123456";
            userName = "root";
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnect() {
        try {
            return DriverManager.getConnection(connectString, userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int login(User user){
        return new UserDao().login(user);
    }
    public static int save(User user){
        return new UserDao().save(user);
    }
    public static Boolean checkAvailability(String id){
        return new UserDao().checkAvailability(id);
    }
    public static User findUser(String id){return new UserDao().findUserById(id);}
}
