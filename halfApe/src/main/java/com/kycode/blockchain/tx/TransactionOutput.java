package com.kycode.blockchain.tx;

import com.kycode.blockchain.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.security.PublicKey;

/**
 * @version: 1.0
 * @description: 交易输出类
 *  将显示从交易中发送给每一方的最终金额。这些作为新交易的输入参考，作为证明你可以发送的金额数量。
 * @author: halfApe
 * @date: 2020/09/10 14:08
 **/
@Data
public class TransactionOutput {
    private String id;
    //数字货币的新拥有者
    private PublicKey recipient;
    //持币数量
    private BigDecimal value;
    //创建此输出的交易ID
    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, BigDecimal value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient)+value+parentTransactionId);
    }
    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey){
        return  publicKey == recipient;
    }

}