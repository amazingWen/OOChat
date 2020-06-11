package com.zw.net.chat.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public int login(User user){
        Connection con = null;
        StringBuffer sql = new StringBuffer("select * from qq_user where qq_user_id='"+user.getUser_id()+"'"+
                " and qq_user_password='"+user.getUser_password()+"'");	//sql语句
        PreparedStatement pst = null;
        int isTrue = 0;
        try {
            con = DaoFactory.getConnect();	//连接 数据库
            pst = con.prepareStatement(sql.toString());
            ResultSet rs=pst.executeQuery();	//得到结果集
            isTrue = rs.next()?1:0;


        } catch (
                SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //passWord = MD5Util.md5Encode("123456");
        return isTrue;

    }

    public int save(User user){
        Connection con = null;
        StringBuffer sql = new StringBuffer("insert into qq_user(qq_user_id,qq_user_password,qq_user_name) values(?,?,?)");	//sql语句
        PreparedStatement pst = null;
        int isTrue = 0;
        try {
            con = DaoFactory.getConnect();	//连接 数据库
            pst = con.prepareStatement(sql.toString());
            pst.setString(1, user.getUser_id());
            pst.setString(2, user.getUser_password());
            pst.setString(3, user.getUser_name());
            isTrue = pst.executeUpdate();

        } catch (
                SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isTrue;
    }

    public Boolean checkAvailability(String id){
        Connection con = null;
        StringBuffer sql = new StringBuffer("select count(*) from qq_user where qq_user_id = '"+id+"'");	//sql语句
        PreparedStatement pst = null;
        Boolean isTrue = false;
        try {
            con = DaoFactory.getConnect();	//连接 数据库
            pst = con.prepareStatement(sql.toString());

            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()){
                if (resultSet.getInt(1) ==0){
                    isTrue = true;
                }
            }
        } catch (
                SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isTrue;
    }

    public User findUserById(String id){
        Connection con = null;
        StringBuffer sql = new StringBuffer("select * from qq_user where qq_user_id='"+id+"'");	//sql语句
        PreparedStatement pst = null;
        User user=new User();
        try {
            con = DaoFactory.getConnect();	//连接 数据库
            pst = con.prepareStatement(sql.toString());
            ResultSet rs=pst.executeQuery();	//得到结果集
            while (rs.next()){
                user.setUser_id(rs.getString(1));
                user.setUser_password(rs.getString(2));
                user.setUser_name(rs.getString(3));
            }


        } catch (
                SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //passWord = MD5Util.md5Encode("123456");
        return user;

    }
}
