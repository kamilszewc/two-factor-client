package io.github.kamilszewc.twofactorclient;

import io.github.kamilszewc.Totp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Entry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String serviceName;
    private String userName;
    private String secret;
    private String issuer;
    private String algorithm;

    public String getCode() throws NoSuchAlgorithmException, InvalidKeyException {
        return Totp.getCode(secret, Totp.HashFunction.HMACSHA1);
    }

    public long getRemainingCodeValidity() {
        return Totp.getCodeRemainingValidityTime();
    }
}
