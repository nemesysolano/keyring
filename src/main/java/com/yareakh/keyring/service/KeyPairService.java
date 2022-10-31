package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;

/**
 * <p>Provides extra validation logic for create/update/delete operations.</p>
 */
public interface KeyPairService {
    /**
     * <p>Creates a new key pair.</p>
     * <p>The <code>name</code> and <code>password</code> parameters must comply the following constraints</p>
     * <ul>
     *     <li>Both must be non null/empty strings and longer than 8 characters.</li>
     *     <li>Name must be unique, they can't be shared across key pairs.</li>
     *     <li>Password must include letters (both capital and lowercase), number and special characters.</li>
     * </ul>
     * <p>Key pairs are immutable, therefore we are not providing update methods.</p>
     * @param keyPair Key pair entity with valid <code>name</code> and <code>password</code>.
     * @return The id (primary key) for the new key pair
     * @throws com.yareakh.keyring.service.KeyPairServiceException when any of the aforementioned constraint is violated.
     */
    Long create(KeyPair keyPair);

    /**
     * <p>Searches a key pair in database.</p>
     * @param name Non empty/null string
     * @return The key pair corresponding to <code>name</code> parameter or <code>null</code> if it exists.
     */
    KeyPair find(String name);

    /**
     * <p>Searches a key pair in database.</p>
     * @param name Non empty/null string
     * @return The key pair corresponding to <code>name</code> parameter.
     * @throws com.yareakh.keyring.service.KeyPairServiceException if no key pair has the given name.
     */
    KeyPair findOrFail(String name);

    /**
     * <p>Searches a key pair in database.</p>
     * @param id  Non null 64 bits integer
     * @return he key pair corresponding to <code>id</code> parameter or <code>null</code> if it exists.
     */
    KeyPair find(Long id);

    /**
     * <p>Searches a key pair in database.</p>
     * @param id  Non null 64 bits integer
     * @return he key pair corresponding to <code>id</code> parameter<code>.
     * @throws com.yareakh.keyring.service.KeyPairServiceException if no key pair has the given id.
     */
    KeyPair findOrFail(Long id);

    /**
     * <p>Deletes a key pair from database.</p>
     * @param id Non null 64 bits integer.
     * @throws com.yareakh.keyring.service.KeyPairServiceException Other entities reference this key pair
     */
    void delete(Long id);
}
