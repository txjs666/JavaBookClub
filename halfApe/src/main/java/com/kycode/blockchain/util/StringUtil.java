package com.kycode.blockchain.util;


import com.google.gson.GsonBuilder;

import java.security.*;
import java.util.Base64;


/**
 * @version: 1.0
 * @description: 工具类
 * @author:halfApe
 * @date: 2020/09/09 17:38
 **/
public class StringUtil {
    /**
     * @Description: 将Sha256应用到一个字符串并返回结果
     * @Author: halfApe
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

    /**
     * @Description: 获取json
     * @Author: halfApe
     * @Date: 2020/9/9 18:42
     * @Parameter [o]
     * @Return java.lang.String
     **/
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    /**
     * @Description: 返回难度字符串目标，与散列比较。难度5则返回“00000”
     * @Author: halfApe
     * @Date: 2020/9/9 18:41
     * @Parameter [difficulty]
     * @Return java.lang.String
     **/
    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
    /**
     * @Description: 应用签名
     * @Author: halfApe
     * @Date: 2020/9/10 15:03
     * @Parameter [privateKey, input]
     * @Return byte[]
     **/
    public static byte[] applyECDSASignature(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSign = dsa.sign();
            output = realSign;
        } catch (Exception e) {
           throw new RuntimeException();
        }
        return output;
    }

    public static boolean verifyECDSASig(PublicKey publicKey,String data,byte[] signature){
        try {
            Signature ecdsaVerify  = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:
     * @Author: halfApe
     * @Date: 2020/9/10 14:23
     * @Parameter [reciepient]
     * @Return java.lang.String
     **/
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


}