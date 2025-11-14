package com.example.auth.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Component
public class KeyProvider {

    private final RSAKey rsaKey;

    public KeyProvider() throws Exception {
        // Generar un par de llaves RSA de 2048 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        this.rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)        // esta llave se usa para firmar
                .algorithm(JWSAlgorithm.RS256)    // algoritmo del JWT
                .keyID(UUID.randomUUID().toString()) // ID de la clave
                .build();
    }

    // Devuelve la llave privada y pública
    public RSAKey getRsaKey() {
        return rsaKey;
    }

    // Devuelve solo la clave pública en formato JWKS
    public JWKSet getJwkSet() {
        return new JWKSet(rsaKey.toPublicJWK());
    }
}

