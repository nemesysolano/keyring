package com.yareakh.keyring.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class VersionInfoResponse {
    public final String version;
    public final String buildNumber;
    public final String commitMessage;
}
