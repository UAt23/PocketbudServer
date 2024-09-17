package com.pocketbud.pocketbud.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final String SECRET_KEY = "caf7d894ee8f0ff8665481093d3653d0839cfedb9591fdae6869f96049183eb5133edabaf6f781bb5b0684d7b2369935aa255ffdf57a7e9bf06ec5584c617d63c00df36ee1640ff348b93f7f311d39b0c7c51e50af07fcaaf76e0fafbbc0e46eee4576026ab1b99ff976e40627bb31bed631246443b499fe243b0b760e82bf2bc32b70cc8110e69cbf61f9af18d75b0b898ff57a89ed9b2bd2e4a3313e475b4f55537edc0eea32f7443d509c1a7c3c8688feb75a51b839356f5266d875393024187b7e5489cfdb879ee52174479afeed509de7330f3ce43ef3dc0f239d68bec26501b5e215ecf8cfbb74a1acb67bcd314b707ed6716cb409b564e345d259db53";  // You should store this securely!

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        try {
            System.out.println("Generating token");
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 hour expiration
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            System.out.println("Generated token: " + token);
            return token;
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
