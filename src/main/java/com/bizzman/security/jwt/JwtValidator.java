package com.bizzman.security.jwt;

import com.bizzman.util.PropertiesManager;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.Properties;

@Component
public class JwtValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);
    private static final String JWT_FILE = "jwt.properties";

    private final Properties properties;

    public JwtValidator() {
        this.properties = PropertiesManager.readProperties(this.getClass(), JWT_FILE);
    }

    public String getUserNameFromJwtToken(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    public boolean validateJwtToken(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);

        if (!containsRequiredClaims(jwt.getJWTClaimsSet())) {
            logger.error("Jwt does not have the required claims!");
            return false;
        }
        if (!isSignatureValid(jwt)) {
            logger.error("Jwt signature is invalid!");
            return false;
        }
        if (!isIssuerValid(jwt.getJWTClaimsSet().getIssuer())) {
            logger.error("Jwt issuer is not valid!");
            return false;
        }
        if (!isExpValid(jwt.getJWTClaimsSet().getExpirationTime())) {
            logger.error("The token is expired!");
            return false;
        }
        return true;
    }

    public boolean containsRequiredClaims(JWTClaimsSet claimsSet) {
        return !StringUtils.isEmpty(claimsSet.getSubject()) && !StringUtils.isEmpty(claimsSet.getIssuer())
                && (claimsSet.getExpirationTime() != null);
    }

    public boolean isSignatureValid(SignedJWT jwt) {
        try {
            JWSVerifier verifier = new MACVerifier(properties.getProperty(JwtProperties.SECRET.getName()));
            return jwt.verify(verifier);
        } catch (JOSEException e) {
            return false;
        }
    }

    public boolean isIssuerValid(String issuer) {
        String issuerProperties = properties.getProperty(JwtProperties.ISSUER.getName());
        if (StringUtils.isEmpty(issuerProperties)) {
            return false;
        } else {
            return issuerProperties.equals(issuer);
        }
    }

    public boolean isExpValid(Date exp) {
        long expiryTime = exp.toInstant().getEpochSecond();
        long currentTime = Instant.now().getEpochSecond();
        return expiryTime > currentTime;
    }




}
