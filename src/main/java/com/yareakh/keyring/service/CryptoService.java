package com.yareakh.keyring.service;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * This interface contain shorthand methods that create crypto objects.
 */
public interface CryptoService {
    /**
     * &quot;RSA&quot;
     */
     String RSA_ALGORITHM = "RSA";

    /**
     * &quot;AES&quot;
     */
    String AES_ALGORITHM = "AES";

    /**
     * <p>Creates a RSA cipher for encryption.</p>
     * @param publicKeyData Raw bytes used as public key
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption.
     * @throws NoSuchAlgorithmException .-
     * @throws InvalidKeySpecException .-
     * @throws NoSuchPaddingException .-
     * @throws InvalidKeyException .-
     */
    RSACipher createRSACipherForEncryption(final byte[] publicKeyData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException;

    /**
     * <p>Creates a RSA cipher for decryption.</p>
     * @param privateKeyData Raw bytes used as private key
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption.
     * @throws NoSuchAlgorithmException .-
     * @throws InvalidKeySpecException .-
     * @throws NoSuchPaddingException .-
     * @throws InvalidKeyException .-
     */
    RSACipher createRSACipherForDecryption(final byte[] privateKeyData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException;

    /**
     * <p>Creates an AES cipher for encryption.</p>
     * @param aesKeyData Raw bytes used to create the cipher key
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption/decryption.
     * @throws NoSuchPaddingException .-
     * @throws NoSuchAlgorithmException .-
     * @throws InvalidAlgorithmParameterException .-
     * @throws InvalidKeyException .-
     */
    AESCipher createAESCipherForEncryption(byte[] aesKeyData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException;

    /**
     * <p>Creates an AES cipher for decryption.</p>
     * @param aesKeyData Raw bytes used to create the cipher key
     * @param ivData Initialization vector
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption/decryption.
     * @throws NoSuchPaddingException .-
     * @throws NoSuchAlgorithmException .-
     * @throws InvalidAlgorithmParameterException .-
     * @throws InvalidKeyException .-
     */
    AESCipher createAESCipherForDecryption(byte[] aesKeyData, byte[] ivData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException;
}
