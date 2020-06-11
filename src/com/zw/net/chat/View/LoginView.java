package com.zw.net.chat.View;

import com.zw.net.chat.Control.Client;
import com.zw.net.chat.Dao.DaoFactory;
import com.zw.net.chat.Dao.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame{
    private JFrame jf;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JButton button1;
    private JButton button2;
    JTabbedPane tabbedPane;
    JTextField jtf1,jtf3,jtf5;
    JPasswordField jtf2,jtf4;
    JLabel jl1,jl2,jl3,jl4,jl5;
    public LoginView(){
        super("OO");
        jf = this;
        this.init();
    }
    //初始化GUI
    public void init(){
        try { // 使用Windows的界面风格
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout(2,1));
        setLocation(540,270);
        setSize(300,225);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        tabbedPane = new JTabbedPane();
        JPanel card1 = new JPanel();
        card1.setLayout(null);

        jtf1 = new JTextField("", 20);
        jtf1.setBounds(90,25,125,20);
        jtf2 = new JPasswordField("", 20);
        jtf2.setBounds(90,70,125,20);

        jtf3 = new JTextField("", 20);
        jtf3.setBounds(90,15,125,20);

        jtf4 = new JPasswordField("", 20);
        jtf4.setBounds(90,45,125,20);

        jtf5 = new JTextField("", 20);
        jtf5.setBounds(90,75,125,20);

        //添加一个回车事件，使得能够回车登陆
        jtf2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER){
                    String password = String.valueOf(jtf2.getPassword());
                    System.out.println(jtf1.getText()+"---"+password);
                    User user = new User();
                    user.setUser_id(jtf1.getText());
                    user.setUser_password(password);
                    int i = DaoFactory.login(user);
                    System.out.println(i);
                    if (1!=i){
                        JOptionPane.showMessageDialog(null, "账号不存在", "请求错误！",JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        user.setUser_name(DaoFactory.findUser(user.getUser_id()).getUser_name());
                        System.out.println(user.getUser_name());
                        jf.setVisible(false);
                        UserView userView = new UserView();
                        Client client = new Client(user, userView);
                        userView.setClient(client);
                        //设置标题
                        userView.setTitle("OO  欢迎用户:"+client.getUser().getUser_name());

                    }
                    jtf1.setText("");
                    jtf2.setText("");
                }
            }
        });
        //添加一个回车事件，使得能够回车注册
        jtf5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER){
                    String password = String.valueOf(jtf4.getPassword());
                    System.out.println(jtf3.getText()+"---"+password+"----"+jtf5.getText());
                    User user = new User();
                    user.setUser_id(jtf3.getText());
                    user.setUser_password(password);
                    user.setUser_name(jtf5.getText());
                    int i = DaoFactory.save(user);
                    if (i!=0){
                        JOptionPane.showMessageDialog(null, "注册成功", "提示",JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(null, "注册失败，用户已存在", "提示",JOptionPane.INFORMATION_MESSAGE);
                    }
                    jtf3.setText("");
                    jtf4.setText("");
                    jtf5.setText("");
                }
            }
        });
        jtf3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = jtf3.getText();
                if (!DaoFactory.checkAvailability(text)){
                    JOptionPane.showMessageDialog(null, "账号已存在", "提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        jl1 = new JLabel("账号");
        jl1.setBounds(50,25,38,20);
        jl2 = new JLabel("密码");
        jl2.setBounds(50,70,38,20);
        jl3 = new JLabel("账号");
        jl3.setBounds(50,15,38,20);
        jl4 = new JLabel("密码");
        jl4.setBounds(50,45,38,20);
        jl5 = new JLabel("昵称");
        jl5.setBounds(50,75,38,20);



        button1 = new JButton("登录");
        button1.setBounds(105,116,88,29);
        button2 = new JButton("注册");
        button2.setBounds(105,116,88,29);


        button1.setActionCommand("LOGIN");
        button2.setActionCommand("REGISTER");
        button1.addActionListener(new MyListener());
        button2.addActionListener(new MyListener());


        card1.add(jl1);
        card1.add(jtf1);
        card1.add(jl2);
        card1.add(jtf2);
        card1.add(button1);

        JPanel card2 = new JPanel();
        card2.setLayout(null);
        card2.add(jl3);
        card2.add(jtf3);
        card2.add(jl4);
        card2.add(jtf4);
        card2.add(jl5);
        card2.add(jtf5);
        card2.add(button2);

        card1.setBackground(Color.GREEN);
        card2.setBackground(Color.GREEN);
        tabbedPane.addTab("登陆", card1);
        tabbedPane.addTab("注册", card2);

        this.add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);  //必须放在最后写， 不然界面打开是空白的

    }
    //内部类，负责监听按钮事件
    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("LOGIN")){
                String password = String.valueOf(jtf2.getPassword());
                System.out.println(jtf1.getText()+"---"+password);
                User user = new User();
                user.setUser_id(jtf1.getText());
                user.setUser_password(password);
                int i = DaoFactory.login(user);
                System.out.println(i);
                if (1!=i){
                    JOptionPane.showMessageDialog(null, "账号不存在", "请求错误！",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    user.setUser_name(DaoFactory.findUser(user.getUser_id()).getUser_name());
                    System.out.println(user.getUser_name());
                    jf.setVisible(false);
                    UserView userView = new UserView();
                    Client client = new Client(user, userView);
                    userView.setClient(client);
                    //设置标题
                    userView.setTitle("OO  欢迎用户:"+client.getUser().getUser_name());

                }
                jtf1.setText("");
                jtf2.setText("");
            }else {
                String password = String.valueOf(jtf4.getPassword());
                System.out.println(jtf3.getText()+"---"+password+"----"+jtf5.getText());
                User user = new User();
                user.setUser_id(jtf3.getText());
                user.setUser_password(password);
                user.setUser_name(jtf5.getText());
                int i = DaoFactory.save(user);
                if (i!=0){
                    JOptionPane.showMessageDialog(null, "注册成功", "提示",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null, "注册失败，用户已存在", "提示",JOptionPane.INFORMATION_MESSAGE);
                }
                jtf3.setText("");
                jtf4.setText("");
                jtf5.setText("");

            }
        }
    }


}
