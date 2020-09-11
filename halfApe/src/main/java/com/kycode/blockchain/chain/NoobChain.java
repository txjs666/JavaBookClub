package com.kycode.blockchain.chain;

import com.kycode.blockchain.tx.Transaction;
import com.kycode.blockchain.tx.TransactionInput;
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
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap();
    public static BigDecimal minimumTransaction = new BigDecimal(0.1);
    public static int difficulty = 1;
    public static Wallet walletA;
    public static Wallet walletB;
    private static Transaction genesisTransaction;


    public static void main(String[] args) {
        //Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //Test public and private keys
        System.out.println("Private and Public keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.getPrivateKey()));
        System.out.println(StringUtil.getStringFromKey(walletA.getPublicKey()));

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), new BigDecimal(100), null);
        genesisTransaction.generateSignature(coinbase.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
        //its important to store our first transaction in the UTXOs list.
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        System.out.println("Creating and Mining Genesis block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA‘s balance is:" + walletA.getBalance());
        System.out.println("\nWalletA‘s Attempting to send funds(40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), new BigDecimal(40)));
        addBlock(block1);
        System.out.println("\nWalletA's balance is:" + walletA.getBalance());
        System.out.println("WalletB's balance is:" + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA‘s balance is:" + walletA.getBalance());
        System.out.println("\nWalletA‘s Attempting to send funds(1000) to WalletB...");
        block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), new BigDecimal(1000)));
        addBlock(block2);
        System.out.println("\nWalletA's balance is:" + walletA.getBalance());
        System.out.println("WalletB's balance is:" + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletA‘s balance is:" + walletA.getBalance());
        System.out.println("\nWalletA‘s Attempting to send funds(20) to WalletB...");
        block1.addTransaction(walletB.sendFunds(walletA.getPublicKey(), new BigDecimal(20)));
        addBlock(block3);
        System.out.println("\nWalletA's balance is:" + walletA.getBalance());
        System.out.println("WalletB's balance is:" + walletB.getBalance());

        isChainValid();


    }

    private static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));
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

            //loop thru blockchains transaction：
            TransactionOutput tempOutput;
            for (int j = 0; j < currentBlock.transactions.size(); j++) {
                Transaction currentTransaction = currentBlock.transactions.get(0);

                if (!currentTransaction.verifySignature()){
                    System.out.println("#Signature on Transaction("+j+") is Invalid");
                    return false;
                }

                if (currentTransaction.getInputsValue().compareTo(currentTransaction.getOutputsValue()) != 0){
                    System.out.println("Inputs are not equal to outputs on Transaction("+j+")");
                    return false;
                }
                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());
                    if (tempOutput == null){
                        System.out.println("#Referenced input on Transaction("+j+") is Missing");
                        return false;
                    }
                    if (input.getUTXO().getValue().compareTo(tempOutput.getValue()) != 0 ){
                        System.out.println("Referenced input Transaction("+j+") value is Invalid");
                        return false;
                    }
                    tempUTXOs.remove(input.getTransactionOutputId());
                }
                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }
                if(currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()){
                    System.out.println("#Transaction("+j+") output reciepient is not who it should be");
                    return false;
                }
                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("Transaction ("+j+") ouput 'change' is not sender.");
                    return false;
                }
            }
            
        }
        System.out.println("Blockchain is valid");
        return true;
    }

}