package com.zw.net.chat.Util;

public class StrSplit {
    public static String stringSplit(String string){
        String result = "";
        if (string!=null&&!string.equals("")){
            String [] sdd = string.split("\\.");
            result = sdd[1];
        }


        return result;
    }
}
