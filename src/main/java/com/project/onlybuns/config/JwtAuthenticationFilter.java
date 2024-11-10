package com.project.onlybuns.config;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import com.project.onlybuns.model.AdminUser;
import com.project.onlybuns.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Direktno definišemo tajni ključ
    private static final String JWT_SECRET = "DajBozeDaProradiKonacnoViseMolimTe";

    private SecretKey secretKey;

    public JwtAuthenticationFilter() {
        this.secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @PostConstruct
    public void init() {
        if (JWT_SECRET == null || JWT_SECRET.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT secret cannot be null or empty");
        }
        secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        System.out.println("Secret key initialized successfully.");
    }

    public String generateToken(User user) {
        String userType = user instanceof AdminUser ? "ROLE_ADMIN" : "ROLE_REGISTERED";
        System.out.println("Generating token for user: " + user.getUsername() + " with type: " + userType);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", Collections.singletonList(userType))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(secretKey) // Koristi inicijalizovani secretKey
                .compact();
    }

    // Dodaj novu metodu za generisanje tokena sa prilagodljivim vremenom isteka
    public String generateTokenWithExpiration(User user, long expirationMillis) {
        String userType = user instanceof AdminUser ? "ROLE_ADMIN" : "ROLE_REGISTERED";
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", Collections.singletonList(userType))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Vreme isteka u milisekundama
                .signWith(secretKey)
                .compact();
    }

    // Postojeća metoda za generisanje tokena sa podrazumevanim vremenom isteka (1 dan)
    /*public String generateToken(User user) {
        return generateTokenWithExpiration(user, 86400000); // 1 dan u milisekundama
    }*/


    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)  // Use your signing key here
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null; // Return null if the token is invalid
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject(); // Ovde dobijamo username iz JWT-a
                if (username != null) {
                    // Dodeljujemo korisniku rolu i autentifikujemo ga
                    List<String> roles = claims.get("authorities", List.class);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                System.out.println("Error parsing JWT: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }


}
