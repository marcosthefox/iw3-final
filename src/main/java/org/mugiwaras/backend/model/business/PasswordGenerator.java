package org.mugiwaras.backend.model.business;

import org.w3c.dom.CharacterData;

import java.security.SecureRandom;

public class PasswordGenerator {

    public static long generateFiveDigitPassword(){
        SecureRandom random = new SecureRandom();
        return random.nextInt(100000) + 9999;
    }
}
