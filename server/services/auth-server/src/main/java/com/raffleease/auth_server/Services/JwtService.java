package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Model.User;
import com.raffleease.common_models.Exceptions.CustomExceptions.AuthorizationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException exp) {
            throw new AuthorizationException("Invalid JWT signature or token: " + exp.getMessage());
        } catch (ExpiredJwtException exp) {
            throw new AuthorizationException("JWT token is expired: " + exp.getMessage());
        } catch (UnsupportedJwtException exp) {
            throw new AuthorizationException("JWT token is unsupported: " + exp.getMessage());
        } catch (IllegalArgumentException exp) {
            throw new AuthorizationException("JWT claims string is empty: " + exp.getMessage());
        }
    }


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getAssociationId());
        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), HS256).compact();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}