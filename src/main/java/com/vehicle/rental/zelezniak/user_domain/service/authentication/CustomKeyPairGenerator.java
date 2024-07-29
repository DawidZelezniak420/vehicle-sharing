package com.vehicle.rental.zelezniak.user_domain.service.authentication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CustomKeyPairGenerator {

    static KeyPair generateRSAKey(){
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
           throwException();
        }
        return keyPair;
    }

    private static void throwException() {
        throw new IllegalArgumentException(
                "Such algorithm does not exists.");
    }
}
