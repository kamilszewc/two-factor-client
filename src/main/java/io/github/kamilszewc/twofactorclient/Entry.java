package io.github.kamilszewc.twofactorclient;

import io.github.kamilszewc.Totp;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Entry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String serviceName;

    @Builder.Default
    private String userName = "";
    private String secret;
    private String issuer;

    @Builder.Default
    private String algorithm = "HMACSHA1";

    public String getCode() throws NoSuchAlgorithmException, InvalidKeyException {
        return Totp.getCode(secret, Totp.HashFunction.HMACSHA1);
    }

    public long getRemainingCodeValidity() {
        return Totp.getCodeRemainingValidityTime();
    }
}
