package com.kycode.blockchain;


import com.kycode.blockchain.util.StringUtil;

import java.util.ArrayList;

/**
 * @version: 1.0
 * @description: 创建区块链
 * @author: halfApe
 * @date: 2020/09/09 18:50
 **/
public class BlockChain {
    /**
     * 存放所有的区块集合
     */
    private static ArrayList<Block> blockChain = new ArrayList<>();
    /**
     * 挖矿难度 数字越大越难
     */
    public static int difficulty = 5;

    /*
     * @Description: 增加新区块
     * @Author: halfApe
     * @Date: 2020/9/10 9:36
     * @Parameter [newBlock]
     * @Return void
     **/
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //循环区块链检查散列
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
            //比较注册散列和计算散列
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //比较以前的散列和注册的先前的散列
            if (!previousBlock.hash.equals(previousBlock.calculateHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //检查hash是否被使用
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("这个区块还没有被开采");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("正在创建第一个区块链");
        //创世块
        BlockChain.addBlock(new Block("我是第一个区块链", "0"));

        System.out.println("正在创建第二个区块链");
        BlockChain.addBlock(new Block("我是第二个区块链", blockChain.get(blockChain.size() - 1).hash));

        System.out.println("正在创建第三个区块链");
        BlockChain.addBlock(new Block("我是第三个区块链", blockChain.get(blockChain.size() - 1).hash));

        System.out.println("区块链是否为有效？" + BlockChain.isChainValid());

        String blockChainJson = StringUtil.getJson(blockChain);
        System.out.println(blockChainJson);
    }
}