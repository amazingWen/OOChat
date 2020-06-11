package com.zw.net.chat.View;

import com.zw.net.chat.Control.Client;
import com.zw.net.chat.Control.Server;
import com.zw.net.chat.Dao.User;
import com.zw.net.chat.Util.StrSplit;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import java.io.File;
import java.net.Socket;
import java.net.URL;

public class UserView extends JFrame {
    private JPanel panel;
//    private JTextArea jta1;
//    private JTextArea jta2;
    private JTextArea jta3; //输入框
    private JTextPane jtp1;
    private JTextPane jtp2;
    private JButton jb1;    //发送按钮
    private JButton jb2;    //图片按钮
    private JList list;
    JScrollPane jsp1;
    JScrollPane jsp2;
    JScrollPane jsp3;
    private DefaultListModel listModel1;
    private StyledDocument doc1 = null;
    private StyledDocument doc2 = null;

    private Client client;

    public UserView(){
        super("OO");
        this.init();
    }
    public UserView(Client client){
        super("OO");
        this.client = client;
        this.init();
    }
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;

    }


    public DefaultListModel getListModel1() {
        return listModel1;
    }

    public void setListModel1(DefaultListModel listModel1) {
        this.listModel1 = listModel1;
    }

    public JTextPane getJtp1() {
        return jtp1;
    }

    public void setJtp1(JTextPane jtp1) {
        this.jtp1 = jtp1;
    }

    public JTextPane getJtp2() {
        return jtp2;
    }

    public void setJtp2(JTextPane jtp2) {
        this.jtp2 = jtp2;
    }

    public void init(){
        try { // 使用Windows的界面风格
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLocation(380,95);
        setSize(650,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(null);

//        jta1=new JTextArea(12,35);
//        jta2=new JTextArea(12,35);
//        jta1.setEditable(false);
        //输入框
        jta3=new JTextArea(12,35);
        jta3.setBounds(10,450,430,79);
        jta3.setBorder(new TitledBorder(" "));
        jta3.setToolTipText("双击好友私聊");
        jta3.setText("双击好友私聊");
        jta3.setForeground(Color.GRAY);
        jta3.setFont(Font.getFont("宋体"));
        jta3.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jta3.getText().equals("双击好友私聊")){
                    jta3.setText("");

                    jta3.setFont(Font.getFont("宋体"));
                    jta3.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

                if (jta3.getText().equals("")){
                    jta3.setForeground(Color.GRAY);
                    jta3.setText("双击好友私聊");
                }
            }
        });

        jtp1 = new JTextPane();     //群聊面板
        jtp2 = new JTextPane();     //私聊面板
        jtp1.setEditable(false);
        jtp2.setEditable(false);
        doc1 = jtp1.getStyledDocument(); // 获得JTextPane的Document
        doc2 = jtp2.getStyledDocument(); // 获得JTextPane的Document



        listModel1= new DefaultListModel();
        list = new JList(listModel1);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list.getSelectedIndex()!=-1){
                    if (e.getClickCount() == 2){
                        jta3.setText("@"+(String) list.getSelectedValue()+":");
                        jta3.requestFocus();
                    }
                }
            }
        });


        jsp1=new JScrollPane(list);
        jsp1.setBorder(new TitledBorder("好友列表"));
        jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsp1.setBounds(450,10,160,400);

        jsp2 = new JScrollPane(jtp1);
        jsp2.setBorder(new TitledBorder("多人聊天"));
        jsp2.setBounds(10,10,430,195);
        jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jsp3 = new JScrollPane(jtp2);
        jsp3.setBorder(new TitledBorder("私聊"));
        jsp3.setBounds(10,210,430,200);
        jsp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        URL resource = UserView.class.getResource("fasong.png");
//        System.out.println(resource);
        Icon icon = new ImageIcon(resource);
        jb1 = new JButton("");
        jb1.setIcon(icon);
        jb1.setBounds(450,500,65,27);
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jta3.getText()!=null&&!jta3.getText().equals("")){
                    client.getSend().send(jta3.getText());

                    System.out.println(jta3.getText()+"***"+client.getSend().getUser().getMsg());
                    jta3.setText("");
                }
            }
        });

        URL resource1 = UserView.class.getResource("3223.png");
//        System.out.println(resource1);
        Icon icon1 = new ImageIcon(resource1);
        jb2 = new JButton("");
        jb2.setIcon(icon1);
        jb2.setBounds(11,427,32,23);
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser(); // 查找文件
                f.showOpenDialog(null);
                //获取文件长度，以便传输
                File file = f.getSelectedFile();
                long len = file.length();
                String length = String.valueOf(len);
                //文件类型标识
                String fileType = StrSplit.stringSplit(file.getName());
                //接收人标识
                String receiveUser = new String("");
                if (!jta3.getText().equals("") &&jta3!=null){
                    if (jta3.getText().startsWith("@")){
                        receiveUser = jta3.getText();
                    }else {
                        receiveUser = "all";
                    }
                }else {
                    receiveUser = "all";
                }
                //通知服务器准备接受图片
                client.getSend().send("#image_content#"+fileType+"#"+receiveUser+"#"+length);
                client.getSend().sendImage(file);
//                insertIcon(f.getSelectedFile()); // 插入图片
            }
        });

//        jsp2=new JScrollPane(jta2);
//        jsp3=new JScrollPane(lst1);

        panel.add(jsp1);    //加入好友栏
        panel.add(jsp2);    //加入群聊框
        panel.add(jsp3);    //加入私聊框
        panel.add(jb1);     //发送按钮
        panel.add(jb2);     //图片按钮
        panel.add(jta3);    //输入框
        panel.setBackground(Color.WHITE);
        this.add(panel);

        this.setBackground(Color.BLUE);

        setResizable(false);
        setVisible(true);
    }

    public void insertIcon(File file,String whereInsert) {
        if (whereInsert.equals(Server.CONSTANT_IMAGE_ALL)){
            jtp1.setCaretPosition(doc1.getLength()); // 设置插入位置
            jtp1.insertIcon(new ImageIcon(file.getPath())); // 插入图片
            insertBlankLine("群聊插入一行");  //换行
        }else {
            jtp2.setCaretPosition(doc2.getLength()); // 设置插入位置
            jtp2.insertIcon(new ImageIcon(file.getPath())); // 插入图片
            insertBlankLine("私聊插入一行");  //换行
        }

    }
    public void insertBlankLine(String tem){
        FontAttrib attrib = new FontAttrib("");
        try{
            if (tem.equals("群聊插入一行")){
                doc1.insertString(doc1.getLength(), attrib.getText() + "\n",
                        attrib.getAttrSet());
            }else if (tem.equals("私聊插入一行")){
                doc2.insertString(doc2.getLength(), attrib.getText() + "\n",
                        attrib.getAttrSet());
            }else {
                System.out.println("未知命令");
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public void insert(FontAttrib attrib) {
        try { // 插入文本
//            if (attrib.getText()!=null && !attrib.getText().equals("")){
                if (attrib.getText().contains("@")){
                    doc2.insertString(doc2.getLength(), attrib.getText().substring(1) + "\n",
                            attrib.getAttrSet());
                }else {
                    doc1.insertString(doc1.getLength(), attrib.getText() + "\n",
                            attrib.getAttrSet());
                }
//            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    public static class FontAttrib {

        private SimpleAttributeSet attrSet = null; // 属性集
        private String text = null; // 要输入的文本

        public FontAttrib() {

        }
        public FontAttrib(String msg) {
            this.text=msg;
        }

        public SimpleAttributeSet getAttrSet() {
            attrSet = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrSet, "黑体");
            StyleConstants.setFontSize(attrSet,14);
            return attrSet;
        }

        public void setAttrSet(SimpleAttributeSet attrSet) {
            this.attrSet = attrSet;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

}
