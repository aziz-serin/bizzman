package com.bizzman.security.jwt;

import com.bizzman.util.PropertiesManager;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Properties;

@Component
public class JwtGenerator {
    private final Properties properties;
    private static final String JWT_FILE = "jwt.properties";

    public JwtGenerator() {
        this.properties = PropertiesManager.readProperties(this.getClass(), JWT_FILE);
    }

    public String generateToken(Authentication authentication) throws JOSEException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        JWSSigner signer = new MACSigner(properties.getProperty(JwtProperties.SECRET.getName()));

        Instant instant = Instant.now().plusSeconds(
                60 * Long.parseLong(properties.getProperty(JwtProperties.EXP_TIME.getName())));

        Date expTime = Date.from(instant);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(properties.getProperty(JwtProperties.ISSUER.getName()))
                .expirationTime(expTime)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}
