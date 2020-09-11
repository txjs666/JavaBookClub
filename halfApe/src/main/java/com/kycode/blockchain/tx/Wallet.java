package com.kycode.blockchain.tx;

import com.kycode.blockchain.chain.NoobChain;
import lombok.Data;

import java.math.BigDecimal;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @description: 电子钱包
 * @author: halfApe
 * @date: 2020/09/10 13:39
 **/
@Data
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    //此钱包拥有的所有UTXO
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            //ECDSA 椭圆曲线算法
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom sha1PRNG = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, sha1PRNG);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 返回余额并存储此钱包拥有的UTXO
     * @Author: halfApe
     * @Date: 2020/9/11 10:23
     * @Parameter * @param null:
     * @Return * @return: null
     **/
    public BigDecimal getBalance() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, TransactionOutput> item : NoobChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.getId(), UTXO);
                total.add(UTXO.getValue());
            }
        }
        return total;
    }

    /**
     * @Description: 从钱包中生成并返回一个新交易
     * @Author: halfApe
     * @Date: 2020/9/11 10:37
     * @Parameter * @param null:
     * @Return * @return: null
     **/
    public Transaction sendFunds(PublicKey _recipient, BigDecimal value) {
        if (getBalance().compareTo(value) == -1) {
            System.out.println("#NOT Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total.add(UTXO.getValue());
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total.compareTo(value) == 1) break;
        }
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);
        for (TransactionInput input : inputs) {
            UTXOs.remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }


}