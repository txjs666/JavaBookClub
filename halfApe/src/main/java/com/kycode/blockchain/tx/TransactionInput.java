package com.kycode.blockchain.tx;

import lombok.Data;

/**
 * @version: 1.0
 * @description: 交易输入
 * @author: halfApe
 * @date: 2020/09/10 14:04
 **/
@Data
public class TransactionInput {
    //Reference to TransactionOutputs -> transactionId
    private String transactionOutputId;
    //区别于基于账户的设计 未花费过的交易输出
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }


}