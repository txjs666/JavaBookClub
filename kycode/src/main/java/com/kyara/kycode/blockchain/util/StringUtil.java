package com.kyara.kycode.blockchain.util;

import com.google.gson.GsonBuilder;

import java.security.MessageDigest;


/**
 * @version: 1.0
 * @description: 工具类
 * @author: weitong@zccp.com
 * @date: 2020/09/09 17:38
 **/
public class StringUtil {
    /*
     * @Description: 将Sha256应用到一个字符串并返回结果
     * @Author: weitong@zccp.com
     * @Date: 2020/9/9 18:01
     * @Parameter [input]
     * @Return java.lang.String
     **/
    public static String applySha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    /*
     * @Description: 获取json
     * @Author: weitong@zccp.com
     * @Date: 2020/9/9 18:42
     * @Parameter [o]
     * @Return java.lang.String
     **/
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
    /*
     * @Description: 返回难度字符串目标，与散列比较。难度5则返回“00000”
     * @Author: weitong@zccp.com
     * @Date: 2020/9/9 18:41
     * @Parameter [difficulty]
     * @Return java.lang.String
     **/
    public static String getDifficultyString(int difficulty){
        return new String(new char[difficulty]).replace('\0','0');
    }


}