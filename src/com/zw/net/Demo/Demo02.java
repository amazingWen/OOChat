package com.zw.net.Demo;

public class Demo02 {
    public static void main(String[] args) {
        String msg = new String("@dafwfaf:");
        String[] split = msg.split(":", 2);
        String s = split[0].substring(1);
        System.out.println(s);
        for (String a:split){
            System.out.println(a);
        }
        System.out.println("dadw");
    }
}
