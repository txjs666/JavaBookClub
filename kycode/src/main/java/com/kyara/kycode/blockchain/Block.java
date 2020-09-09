package com.kyara.kycode.blockchain;

import com.kyara.kycode.blockchain.util.StringUtil;

import java.util.Date;

/**
 * @version: 1.0
 * @description: 区块对象
 * @author: weitong@zccp.com
 * @create: 2020-09-09 16:49
 **/
public class Block {
    private String hash;
    /**
     * 上一个区块的hash值
     */
    private String preHash;
    /**
     * 每个区块存放的信息
     */
    private String data;
    /**
     * 时间戳
     */
    private Long timeStamp;
    /**
     * 挖矿者的工作量证明
     */
    private int nonce;

    public Block(String preHash, String data) {
        this.preHash = preHash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        //根据preHash和data以及timeStamp产生唯一的hash
        this.hash = calculateHash();
    }

    /*
     * @Description: 基于上一个块计算新的hash值
     * @Author: weitong@zccp.com
     * @Date: 2020/9/9 17:35
     * @Return java.lang.String
     **/
    private String calculateHash() {
        return StringUtil.applySha256(new StringBuffer().append(preHash).append(timeStamp).append(nonce).append(data).toString());
    }
    /*
     * @Description: 挖矿
     * @Author: weitong@zccp.com
     * @Date: 2020/9/9 19:01
     * @Parameter [difficulty]
     * @Return void
     **/
    private void mineBlock(int difficulty){
        //目标值 difficulty越大，下面计算量越大
        String target = StringUtil.getDifficultyString(difficulty);
        //如果difficulty为5，那么target则为00000
        while(!hash.substring(0,difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("创建区块"+hash);
    }
}