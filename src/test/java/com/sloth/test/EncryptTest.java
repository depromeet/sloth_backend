package com.sloth.test;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class EncryptTest {

    @Test
    public void checkEncrypt(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(2);
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
        encryptor.setPassword("nanagong-backend!@#");
        //encryptor.setPassword("sloth");
        String cliendId = "xFd4ybu4hR"; // 암호화 할 내용
        String encryptedText = encryptor.encrypt(cliendId); // 암호화
        String decryptedText = encryptor.decrypt(encryptedText); // 복호화
        System.out.println("Enc:"+encryptedText+", Dec:"+decryptedText);
        //Assertions.assertThat(cliendId).isEqualTo(decryptedText);
    }

}