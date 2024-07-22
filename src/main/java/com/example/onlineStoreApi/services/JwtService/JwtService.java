package com.example.onlineStoreApi.services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String secretKey = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";


    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSingingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }


    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000))
                .signWith(getSingingKey())  //, SignatureAlgorithm.HS256
                .compact();
    }


    public Boolean isTokenValidate(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private SecretKey getSingingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
