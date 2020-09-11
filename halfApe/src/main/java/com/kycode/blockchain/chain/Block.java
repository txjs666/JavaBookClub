package com.kycode.blockchain.chain;


import com.kycode.blockchain.tx.Transaction;
import com.kycode.blockchain.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * @version: 1.0
 * @description: 区块对象
 * @author: halfApe
 * @create: 2020-09-09 16:49
 **/
public class Block {
    public String hash;
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

    private String  merkleRoot;

    public ArrayList<Transaction> transactions = new ArrayList<> ();

    public Block(String preHash, String data) {
        this.preHash = preHash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        //根据preHash和data以及timeStamp产生唯一的hash
        this.hash = calculateHash();
    }
    public Block(String preHash) {
        this.preHash = preHash;
        this.timeStamp = new Date().getTime();
        //根据preHash和data以及timeStamp产生唯一的hash
        this.hash = calculateHash();
    }
    /*
     * @Description: 挖矿
     * @Author: halfApe
     * @Date: 2020/9/9 19:01
     * @Parameter [difficulty]
     * @Return void
     **/
    public void mineBlock(int difficulty){
        merkleRoot =StringUtil.getMerkleRoot(transactions);
        //目标值 difficulty越大，下面计算量越大
        String target = StringUtil.getDifficultyString(difficulty);
        //如果difficulty为5，那么target则为00000

        while(!hash.substring(0,difficulty).equals(target)){
            //工作量证明
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined！！！"+hash);
    }

    /*
     * @Description: 基于上一个块计算新的hash值
     * @Author: halfApe
     * @Date: 2020/9/9 17:35
     * @Return java.lang.String
     **/
    public String calculateHash() {
        return StringUtil.applySha256(new StringBuffer().append(preHash).append(timeStamp).append(nonce).append(merkleRoot).toString());
    }
    
    /**
     * @Description: 为区块添加交易
     * @Author: halfApe
     * @Date: 2020/9/11 11:36
     * @Parameter * @param null: 
     * @Return * @return: null
     **/
    public boolean addTransaction(Transaction transaction){
        //process transaction and check if valid, unless block is genesis block then ignore
        if (transaction == null) {
            return false;
        }
        if (preHash != "0" && transaction.processTransaction() != true){
            System.out.println("Transaction failed to process.Discarded.");
            return false;
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }



}