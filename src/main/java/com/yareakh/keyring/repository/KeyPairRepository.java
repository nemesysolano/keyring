package com.yareakh.keyring.repository;

import com.yareakh.keyring.model.KeyPair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Use this CRU class to keep keyring upto date.
 */
public interface KeyPairRepository extends CrudRepository<KeyPair, Long> {

    /**
     * Looks for existing key pairs whose with name equal to {@code name}.
     * @param name Non null/empty string
     * @return A list of key pairs matching the search criteria.
     */
    public List<KeyPair> findByName(String name);
}
