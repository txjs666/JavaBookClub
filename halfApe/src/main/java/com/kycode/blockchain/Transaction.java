package com.kycode.blockchain;

import com.kycode.blockchain.util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @description: 交易类
 * @author: weitong@zccp.com
 * @date: 2020/09/10 13:55
 **/
public class Transaction {
    //交易hash
    private String transactionId;
    //发送者地址 公钥
    private PublicKey sender;
    //接收者地址 公钥
    private PublicKey reciepient;

    private float value;
    //为了防止任何人花费我们钱包里面的钱
    //第一，签名用来保证只有货币的拥有者才可以用来发送自己的货币，第二，签名用来阻止其他人试图篡改提交的交易。
    private byte[] signature;

    public List<TransactionInput> inputs = new ArrayList();
    public List<TransactionOutput> outputs = new ArrayList();
    //事务生成树统计
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash(){
        sequence++;
        return StringUtil.applySha256(new StringBuffer().append(StringUtil.getStringFromKey(sender))
                                                        .append(StringUtil.getStringFromKey(reciepient))
                                                        .append(Float.toString(value))
                                                        .append(sequence).toString());
    }
    public void generateSignature(PrivateKey privateKey){
        signature = StringUtil.applyECDSASignature(privateKey,
                    StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+value);
    }

    public boolean verifySignature(){
        return StringUtil.verifyECDSASig(sender,
                                   StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(reciepient)+value
                                         ,signature);
    }


}