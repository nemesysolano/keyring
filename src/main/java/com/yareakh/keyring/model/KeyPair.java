package com.yareakh.keyring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.Table;

/**
 * <p>This class associates a public/private key pair with an unique name.</p>
 */
@SuppressWarnings("all")
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldNameConstants
@Entity
@Table(name="KEY_PAIR")
public class KeyPair {
    /**
     * &quot;ID&quot;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public final Long id;

    /**
     * &quot;PUBLIC_KEY&quot;
     */
    @Lob
    @Column(name = "PUBLIC_KEY")
    @Type(type = "org.hibernate.type.BinaryType")
    public final byte[] publicKey;

    /**
     * &quot;PRIVATE_KEY&quot;
     */
    @Lob
    @Column(name = "PRIVATE_KEY")
    @Type(type = "org.hibernate.type.BinaryType")
    public final byte[] privateKey;

    /**
     * &quot;PASSWORD&quot;
     */
    @Column(name = "PASSWORD")
    public final String password;

    /**
     * &quot;NAME&quot;
     */
    @Column(name = "NAME")
    public final String name;


}
