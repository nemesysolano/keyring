package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.service.*;
import com.yareakh.keyring.stubs.CipherStub;
import com.yareakh.keyring.stubs.KeyFactoryStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class CryptoServiceImpl implements CryptoService {
    KeyFactoryStub keyFactoryStub;
    CipherStub cipherStub;

    /**
     *
     * @param keyFactoryStub stub for <code>java.security.KeyFactory</code>
     */
    public CryptoServiceImpl(KeyFactoryStub keyFactoryStub, CipherStub cipherStub) {
        this.keyFactoryStub = keyFactoryStub;
        this.cipherStub = cipherStub;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RSACipher createRSACipherForEncryption(final byte[] publicKeyData) throws WrappedCheckedException {
        try {
            KeyFactory rsaKeyFactory = keyFactoryStub.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
            PublicKey publicKey = rsaKeyFactory.generatePublic(publicKeySpec);
            Cipher rsaCipher = cipherStub.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return new RSACipherImpl(rsaCipher, publicKey);
        }catch(GeneralSecurityException cause) {
            throw new WrappedCheckedException("Can't create RSA cipher for encryption.", cause);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RSACipher createRSACipherForDecryption(byte[] privateKeyData) throws WrappedCheckedException {
        try {
            KeyFactory rsaKeyFactory = keyFactoryStub.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec endPrivateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
            PrivateKey privateKey = rsaKeyFactory.generatePrivate(endPrivateKeySpec);
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

            return new RSACipherImpl(rsaCipher, privateKey);
        }catch(GeneralSecurityException cause) {
            throw new WrappedCheckedException("Can't create RSA cipher for decryption.", cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public AESCipher createAESCipherForEncryption(byte[] aesKeyData) throws WrappedCheckedException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            ThreadLocalRandom.current().nextBytes(aesKeyData);
            SecretKey aesSecretKey = new SecretKeySpec(aesKeyData, AES_ALGORITHM);
            IvParameterSpec iv = MessageService.generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, iv);

            return new AESCipherImpl(aesSecretKey, iv, cipher);
        }catch(GeneralSecurityException cause) {
            throw new WrappedCheckedException("Can't AES cipher for encryption.", cause);
        }
    }

    @Override
    public AESCipher createAESCipherForDecryption(byte[] aesKeyData, byte[] ivData) throws WrappedCheckedException {
        try {
            Cipher aesCipher = cipherStub.getInstance("AES/CBC/PKCS5PADDING");
            SecretKey aesSecretKey = new SecretKeySpec(aesKeyData, AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(ivData);
            aesCipher.init(Cipher.DECRYPT_MODE, aesSecretKey, iv);

            return new AESCipherImpl(aesSecretKey, iv, aesCipher);
        }catch(GeneralSecurityException cause) {
            throw new WrappedCheckedException("Can't AES cipher for decryption.", cause);
        }
    }
}
