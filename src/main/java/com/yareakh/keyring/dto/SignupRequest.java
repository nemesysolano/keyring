package com.yareakh.keyring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>This DTO packages data required to create a new key pair.</p>
 */
@SuppressWarnings("all")
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class SignupRequest {
    /**
     * <p>Key pair password. Must be longer than 8 character and contains numbers, upper/lower case letter and special
     * symbols.</p>
     */
    public final String password;

    /**
     * <p>Key pair name (usually an email). Names are unique per realm.</p>
     */
    public final String name;
}
