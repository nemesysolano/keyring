package com.yareakh.keyring.service;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;


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
     * @throws WrappedCheckedException .-
     */
    RSACipher createRSACipherForEncryption(final byte[] publicKeyData) throws WrappedCheckedException;

    /**
     * <p>Creates a RSA cipher for decryption.</p>
     * @param privateKeyData Raw bytes used as private key
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption.
     * @throws WrappedCheckedException .-
     */
    RSACipher createRSACipherForDecryption(final byte[] privateKeyData) throws WrappedCheckedException;

    /**
     * <p>Creates an AES cipher for encryption.</p>
     * @param aesKeyData Raw bytes used to create the cipher key
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption/decryption.
     * @throws WrappedCheckedException .-
     */
    AESCipher createAESCipherForEncryption(byte[] aesKeyData) throws WrappedCheckedException;

    /**
     * <p>Creates an AES cipher for decryption.</p>
     * @param aesKeyData Raw bytes used to create the cipher key
     * @param ivData Initialization vector
     * @return A new <code>javax.crypto.Cipher</code> suitable for encryption/decryption.
     * @throws WrappedCheckedException .-
     */
    AESCipher createAESCipherForDecryption(byte[] aesKeyData, byte[] ivData) throws WrappedCheckedException;


    /**
     *
     * @param password Non null/empty character string
     * @return Encrypted password
     */
    static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));
    }

    static boolean matchPassword(String encoded, String raw) {
        return BCrypt.checkpw(raw, encoded);
    }
}
