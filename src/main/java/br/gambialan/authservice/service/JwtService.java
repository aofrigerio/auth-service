package br.gambialan.authservice.service;

import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.privateKey}")
    private String secretKey;

    private KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    public String generateToken(String sub){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("u", sub);
        claims.put("scope", "admin");
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject("Alan")
                .setHeaderParam("kid", "sso-key")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public RSAKey getPublicKey() {

            System.out.println("---------------------------------");
            System.out.println(keyPair.getPublic());
            System.out.println("---------------------------------");
            System.out.println(keyPair.getPrivate());
            System.out.println("---------------------------------");

            RSAPublicKey exposePublicKey = (RSAPublicKey) keyPair.getPublic();
            return new RSAKey.Builder(exposePublicKey)
                    .keyID("sso-key")
                    .build();
    }
}
