package com.senla.training.yeutukhovich.scooterrental.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES = "AUTHORITIES";

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.validity}")
    private Long validityInMilliseconds;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put(AUTHORITIES, authorities);
        return generateToken(claims, user.getUsername());
    }

    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        return username != null && !isTokenExpired(token);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthorities(String token) {
        return getClaimFromToken(token, claims -> (List<String>) claims.get(AUTHORITIES));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
