package com.kycode.blockchain;

/**
 * @version: 1.0
 * @description: 交易输入
 * @author: halfApe
 * @date: 2020/09/10 14:04
 **/
public class TransactionInput {
    //Reference to TransactionOutputs -> transactionId
    public String transactionId;
    //区别于基于账户的设计 未花费过的交易输出
    public TransactionOutput UTXO;

    public TransactionInput() {
    }
}