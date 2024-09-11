package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.config.TokenProperties;
import com.example.socialnetwork.domain.port.api.JwtServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class JwtServiceImpl implements JwtServicePort {
    private final String secretKey;
    private final long accessExpiration;

    public JwtServiceImpl(TokenProperties tokenProperties) {
        this.secretKey = tokenProperties.getSecretKey();
        this.accessExpiration = tokenProperties.getAccessExpiration();
    }

    @Override
    public User validateAndExtractUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userName = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> authoritiesList = claims.get("authorities", List.class);
            Collection<GrantedAuthority> authorities = authoritiesList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new User(userName, "", authorities);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String generateAccessToken(User user) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + accessExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateVerifyToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidBearerTokenException("Invalid JWT token: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}