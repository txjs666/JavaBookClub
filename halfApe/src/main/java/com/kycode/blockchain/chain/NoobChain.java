package com.kycode.blockchain.chain;

import com.kycode.blockchain.tx.Transaction;
import com.kycode.blockchain.tx.TransactionOutput;
import com.kycode.blockchain.tx.Wallet;
import com.kycode.blockchain.util.StringUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigDecimal;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version: 1.0
 * @description: 区块链入门
 * @author: weitong@zccp.com
 * @date: 2020/09/10 15:36
 **/
public class NoobChain {
    public static List<Block> blockChain = new ArrayList();
    //未使用的交易作为可用的输入
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap ();
    public static BigDecimal minimumTransaction = new BigDecimal(0.1);
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;


    public static void main(String[] args) {
        //Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();

        //Test public and private keys
        System.out.println("Private and Public keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.getPrivateKey()));
        System.out.println(StringUtil.getStringFromKey(walletA.getPublicKey()));

        //Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), new BigDecimal(5), null);
        transaction.generateSignature(walletA.getPrivateKey());

        //Verify the signature works and verify it from the public reciepient
        System.out.println("Is signature verified");
        System.out.println(transaction.verifySignature());
    }

}