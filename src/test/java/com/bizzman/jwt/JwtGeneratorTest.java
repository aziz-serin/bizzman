package com.bizzman.jwt;


import com.bizzman.BizzmanApplication;
import com.bizzman.security.jwt.JwtGenerator;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.sql.Date;
import java.time.Instant;

import static com.nimbusds.jose.JWSAlgorithm.HS256;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class JwtGeneratorTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private JwtGenerator jwtGenerator;

    private static final String USERNAME = "test";

    private static final int AFTER_MINUTES = 50;

    private SignedJWT signedJwt;

    @Before
    public void setup() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(userDetails.getUsername()).willReturn(USERNAME);

        String jwt = jwtGenerator.generateToken(authentication);
        this.signedJwt = SignedJWT.parse(jwt);
    }

    @Test
    public void jwtHasCorrectSubject() throws Exception {
        assertThat(signedJwt.getJWTClaimsSet().getSubject()).isEqualTo(USERNAME);
    }

    @Test
    public void jwtHasValidIssuer() throws Exception {
        assertThat(signedJwt.getJWTClaimsSet().getIssuer()).isEqualTo("https://bizmann.com");
    }

    @Test
    public void jwtIsMACBased() {
        assertThat(signedJwt.getHeader().getAlgorithm()).isEqualTo(HS256);
    }

    @Test
    public void jwtExpiryTimeIsValid() throws Exception {
        assertThat(signedJwt.getJWTClaimsSet().getExpirationTime())
                .isAfter(Date.from(Instant.now().plusSeconds(60*AFTER_MINUTES)));
    }

}
