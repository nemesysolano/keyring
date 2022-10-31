package com.yareakh.keyring.model;

/**
 * Analog to <code>Map.Entry</code> interface
 * @param <K> Key type
 * @param <V> Value type
 */
public class Tuple<K,V> {

    /**
     * Tuple key (this is conventional).
     */
    public final K key;

    /**
     * Tuple value (also conventional)
     */
    public final V value;

    /**
     *
     * @param key Tuple key (this is conventional).
     * @param value Tuple value (also conventional.
     */
    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }


}
