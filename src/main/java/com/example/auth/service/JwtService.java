package com.example.auth.service;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.auth.config.KeyProvider;
import com.example.auth.model.User;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final KeyProvider keyProvider;

    @Value("${auth.jwt.issuer}")
    private String issuer;

    @Value("${auth.jwt.access-token-minutes}")
    private long accessTokenMinutes;

    public String generateAccessToken(User user) {
        try {
            Instant now = Instant.now();
            
            JWSSigner signer = new RSASSASigner(keyProvider.getRsaKey().toPrivateKey());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(issuer)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plus(accessTokenMinutes, ChronoUnit.MINUTES)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("role", user.getRole().getName())
                    .claim("customerId", user.getCustomerId())
                    .build();

            SignedJWT jwt = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(keyProvider.getRsaKey().getKeyID())
                            .type(JOSEObjectType.JWT)
                            .build(),
                    claims
            );

            jwt.sign(signer);

            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generando token JWT", e);
        }
    }
}