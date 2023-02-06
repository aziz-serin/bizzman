package com.bizzman.jwt;

import com.bizzman.BizzmanApplication;
import com.bizzman.security.jwt.JwtValidator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class JwtValidatorTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private JwtValidator jwtValidator;

    @Test
    public void jwtValidatorFailsGivenMissingClaims() {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("issuer")
                .subject("user")
                .build();

        assertThat(jwtValidator.containsRequiredClaims(jwtClaimsSet)).isFalse();
    }

    @Test
    public void jwtValidatorSucceedsGivenNoMissingClaims() {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("issuer")
                .subject("user")
                .expirationTime(Date.from(Instant.now()))
                .build();

        assertThat(jwtValidator.containsRequiredClaims(jwtClaimsSet)).isTrue();
    }

    @Test
    public void jwtValidatorFailsGivenUnvalidIssuer() {
        assertThat(jwtValidator.isIssuerValid("unknown_issuer")).isFalse();
    }

    @Test
    public void jwtValidatorSucceedsGivenCorrectIssuer() {
        assertThat(jwtValidator.isIssuerValid("https://bizmann.com")).isTrue();
    }

    @Test
    public void jwtValidatorFailsGivenTokenExpired() {
        Date date = Date.from(Instant.now().minusSeconds(10*60));
        assertThat(jwtValidator.isExpValid(date)).isFalse();
    }

    @Test
    public void jwtValidatorSucceedsGivenTokenIsNotExpired() {
        Date date = Date.from(Instant.now().plusSeconds(10*60));
        assertThat(jwtValidator.isExpValid(date)).isTrue();
    }

    @Test
    public void jwtValidatorFailsGivenInvalidSignature() throws Exception {
        SignedJWT signedJWT = Mockito.mock(SignedJWT.class);
        given(signedJWT.verify(any(JWSVerifier.class))).willThrow(JOSEException.class);

        assertThat(jwtValidator.isSignatureValid(signedJWT)).isFalse();
    }

    @Test
    public void jwtValidatorSucceedsGivenValidSignature() throws Exception {
        SignedJWT signedJWT = Mockito.mock(SignedJWT.class);
        given(signedJWT.verify(any(JWSVerifier.class))).willReturn(true);

        assertThat(jwtValidator.isSignatureValid(signedJWT)).isTrue();
    }



}
