package com.example.onlineStoreApi.services.JwtService;

import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.services.cache.CacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final CacheService cacheService;
    private static final String secretKey = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";


    @Autowired
    public JwtService(@Qualifier("InMemoryCache") CacheService cacheService) {
        this.cacheService = cacheService;
    }


    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String extractTokenType(String token) {
        return extractClaim(token, (claims -> (String) claims.get("type")));
    }


    public Boolean isTokenValidate(String token, String userName) {
        final String username = extractUsername(token);
        return (username.equals(userName) && !isTokenExpired(token));
    }

    public void blacklistToken(String token) {
        cacheService.put(token, token);
    }

    public Boolean isTokenBlacklisted(String token) {
        return cacheService.isHas(token);
    }


    public String generateAccessToken(User user) {
        return generateToken(
                user.getEmail(),
                new HashMap<>() {{
                    put("type", "access");
                }},
                new Date(System.currentTimeMillis() + 1000 * 60 * 20)); // 20 minutes
    }


    public String generateRefreshToken(User user) {
        return generateToken(
                user.getEmail(),
                new HashMap<>() {{
                    put("type", "refresh");
                }},
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 1 hour * 48  = 2 days
    }


    private String generateToken(String subject, Map<String, Object> claims, Date expiration) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .signWith(getSingingKey())  //, SignatureAlgorithm.HS256
                .compact();
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // Claims === JwtPayload
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSingingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSingingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
