package com.kycode.blockchain;

import lombok.Data;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

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

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            //ECDSA 椭圆曲线算法
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom sha1PRNG = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec,sha1PRNG);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}