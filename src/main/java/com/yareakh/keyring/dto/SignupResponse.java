package com.yareakh.keyring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>This DTO is the response to a succesful signup request (<code>com.yareakh.keyring.dto.SignupRequest</code>).</p>
 */
@SuppressWarnings("all")
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class SignupResponse {
    /**
     * &quot;ID&quot;
     */
    public final Long id;
}
