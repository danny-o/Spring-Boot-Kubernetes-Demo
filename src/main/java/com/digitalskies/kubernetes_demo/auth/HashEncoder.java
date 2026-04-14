package com.digitalskies.kubernetes_demo.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashEncoder {

    private BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();

    String encode(String rawString){
       return bCryptPasswordEncoder.encode(rawString);
    }
    boolean compare(String rawString,String hashedString){
        return bCryptPasswordEncoder.matches(rawString,hashedString);
    }

}
