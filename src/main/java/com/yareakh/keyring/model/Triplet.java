package com.yareakh.keyring.model;

/**
 * Shorthand for <code>Tuple&lt;K,Tuple&lt;V1, V2&gt;&gt;</code>
 * @param <K>
 * @param <V1>
 * @param <V2>
 */
public class Triplet <K, V1, V2> {

    /**
     * Triplet key (this is conventional).
     */
    public final K key;

    /**
     * First value.
     */
    public final V1 v1;

    /**
     * Second value.
     */
    public final V2 v2;

    /**
     *
     * @param key Triplet key (this is conventional).
     * @param v1 first value
     * @param v2 second value.
     */
    public Triplet(K key, V1 v1, V2 v2) {
        this.key = key;
        this.v1 = v1;
        this.v2 = v2;
    }
}
