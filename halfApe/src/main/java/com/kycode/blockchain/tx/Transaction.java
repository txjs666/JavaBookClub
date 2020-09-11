package com.kycode.blockchain.tx;

import com.kycode.blockchain.chain.NoobChain;
import com.kycode.blockchain.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
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
@Data
public class Transaction {
    //交易hash
    private String transactionId;
    //发送者地址 公钥
    private PublicKey sender;
    //接收者地址 公钥
    private PublicKey recipient;

    private BigDecimal value;
    //为了防止任何人花费我们钱包里面的钱
    //第一，签名用来保证只有货币的拥有者才可以用来发送自己的货币，第二，签名用来阻止其他人试图篡改提交的交易。
    private byte[] signature;

    public List<TransactionInput> inputs = new ArrayList();
    public List<TransactionOutput> outputs = new ArrayList();
    //事务生成树统计
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, BigDecimal value, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(new StringBuffer().append(StringUtil.getStringFromKey(sender))
                .append(StringUtil.getStringFromKey(recipient))
                .append(value)
                .append(sequence).toString());
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = StringUtil.applyECDSASignature(privateKey,
                StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value);
    }

    /**
     * @Description: 验证签名
     * @Author: halfApe
     * @Date: 2020/9/11 9:27
     * @Parameter []
     * @Return boolean
     **/
    public boolean verifySignature() {
        return StringUtil.verifyECDSASig(sender,
                StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value
                , signature);
    }

    /**
     * @Description: Returns true if new transaction could be created.
     * @Author: halfApe
     * @Date: 2020/9/11 9:23
     * @Parameter []
     * @Return boolean
     **/
    public boolean processTransaction() {
        if (verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
        //汇总交易输入（确保他们是未花费的）
        for (TransactionInput in : inputs) {
            in.setUTXO(NoobChain.UTXOs.get(in.getTransactionOutputId()));
        }
        //校验交易是否是有效的
        if (getInputsValue().compareTo(NoobChain.minimumTransaction) == -1) {
            System.out.println("#Transaction is to small:" + getInputsValue());
            return false;
        }

        //生成交易输出
        BigDecimal leftOver = getInputsValue().subtract(value);
        transactionId = calculateHash();
        //send value to recipient
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        //send the left over 'change' back to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        //add outputs to Unspent list
        for (TransactionOutput out : outputs) {
            NoobChain.UTXOs.put(out.getId(), out);
        }

        //remove transaction inputs from UTXO lists as spent;
        for (TransactionInput in : inputs) {
            //如果没有交易则跳过
            if (in.getUTXO() == null) continue;
            NoobChain.UTXOs.remove(in.getUTXO().getId());
        }
        return true;
    }

    /**
     * @Description: 返回所有的交易输入inputs(UTXOs)
     * @Author: halfApe
     * @Date: 2020/9/11 9:44
     * @Parameter []
     * @Return float
     **/
    public BigDecimal getInputsValue() {
        BigDecimal total = BigDecimal.ZERO;
        for (TransactionInput in : inputs) {
            //如果没有交易则跳过
            if (in.getUTXO() == null) {
                continue;
            }
            total = total.add(in.getUTXO().getValue());
        }
        return total;
    }

    /**
     * @Description: 获取所有的输出
     * @Author: halfApe
     * @Date: 2020/9/11 10:20
     * @Parameter []
     * @Return java.math.BigDecimal
     **/
    public BigDecimal getOutputsValue() {
        BigDecimal total = BigDecimal.ZERO;
        for (TransactionOutput out : outputs) {
            total = total.add(out.getValue());
        }
        return total;
    }


}