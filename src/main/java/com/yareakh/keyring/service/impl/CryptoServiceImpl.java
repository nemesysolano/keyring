package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.service.AESCipher;
import com.yareakh.keyring.service.CryptoService;
import com.yareakh.keyring.service.MessageService;
import com.yareakh.keyring.service.RSACipher;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CryptoServiceImpl implements CryptoService {

    /**
     * {@inheritDoc}
     */
    @Override
    public RSACipherImpl createRSACipherForEncryption(final byte[] publicKeyData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory rsaKeyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = rsaKeyFactory.generatePublic(publicKeySpec);
        Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return new RSACipherImpl(rsaCipher, publicKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RSACipher createRSACipherForDecryption(byte[] privateKeyData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        KeyFactory rsaKeyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec endPrivateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
        PrivateKey privateKey = rsaKeyFactory.generatePrivate(endPrivateKeySpec);
        Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new RSACipherImpl(rsaCipher, privateKey);
    }

    /**
     * {@inheritDoc}
     */
    public AESCipher createAESCipherForEncryption(byte[] aesKeyData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        ThreadLocalRandom.current().nextBytes(aesKeyData);
        SecretKey aesSecretKey = new SecretKeySpec(aesKeyData, AES_ALGORITHM);
        IvParameterSpec iv = MessageService.generateIv();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, iv);

        return new AESCipherImpl(aesSecretKey, iv, cipher);
    }

    @Override
    public AESCipher createAESCipherForDecryption(byte[] aesKeyData, byte[] ivData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        SecretKey aesSecretKey = new SecretKeySpec(aesKeyData, AES_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivData);
        aesCipher.init(Cipher.DECRYPT_MODE, aesSecretKey, iv);

        return new AESCipherImpl(aesSecretKey, iv, aesCipher);
    }
}
