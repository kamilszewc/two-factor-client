package com.computinglaboratory.twofactorclient;

import com.computinglaboratory.totp.Totp;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Entry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String serviceName;

    public String getServiceName() {
        return this.serviceName;
    }

    @Builder.Default
    private String userName = "";
    private String secret;
    private String issuer;

    public String getIssuer() {
        return this.issuer;
    }

    @Builder.Default
    private String algorithm = "HMACSHA1";

    public String getCode() throws NoSuchAlgorithmException, InvalidKeyException {
        return Totp.getCode(secret, Totp.HashFunction.HMACSHA1);
    }

    public long getRemainingCodeValidity() {
        return Totp.getCodeRemainingValidityTime();
    }
}
