package com.yareakh.keyring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>This class represents a message.</p>
 */
@SuppressWarnings("all")
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldNameConstants
@Entity
@Table(name="MESSAGE")
public class Message {
    /**
     * ID VARCHAR(72) NOT NULL
     */
    @Id
    @Column(name = "ID")
    public final String id;

    /**
     * AES_KEY bytea NOT NULL
     */
    @Lob
    @Column(name = "AES_KEY")
    @Type(type = "org.hibernate.type.BinaryType")
    public final byte[] aesKey;

    /**
     * IV bytea NOT NULL
     */
    @Lob
    @Column(name = "IV")
    @Type(type = "org.hibernate.type.BinaryType")
    public final byte[] iv;


    /**
     * CONTENT VARCHAR(512) NOT NULL;
     */
    @Lob
    @Column(name = "CONTENT")
    @Type(type = "org.hibernate.type.BinaryType")
    public final byte[]  content;

    /**
     * START_KEYPAIR_ID BIGINT NOT NULL,
     */
    @ManyToOne()
    @JoinColumn(name="START_KEYPAIR_ID", referencedColumnName = "ID")
    public final KeyPair start;

    /**
     * END_KEYPAIR_ID BIGINT NOT NULL,
     */
    @ManyToOne()
    @JoinColumn(name="END_KEYPAIR_ID", referencedColumnName = "ID")
    public final KeyPair end;

    /**
     * CREATION_DATE TIMESTAMP NOT NULL
     */
    @Column(name = "CREATION_DATE")
    public final Date creationDate;

    /**
     * CLEARANCE_DATE TIMESTAMP NOT NULL
     */
    @Column(name = "CLEARANCE_DATE")
    public final Date clearanceDate;

}
