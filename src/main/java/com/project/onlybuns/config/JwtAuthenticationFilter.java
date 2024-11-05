package com.project.onlybuns.config;
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

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                // Proverite da li je secretKey inicijalizovan
                if (secretKey == null) {
                    System.out.println("Secret key is null!");
                    throw new IllegalStateException("Nije inicijalizovan");
                }

                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                List<String> roles = claims.get("authorities", List.class);

                // Ostatak koda...
            } catch (Exception e) {
                // Ispis greške
                System.out.println("Cannot set user authentication: " + e.getMessage());
                e.printStackTrace(); // Ispis kompletne greške na konzolu
            }
        } else {
            System.out.println("Authorization header is missing or does not start with Bearer");
        }

        filterChain.doFilter(request, response);
    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                // Proverite da li je secretKey inicijalizovan
                if (secretKey == null) {
                    System.out.println("Secret key is null!");
                    throw new IllegalStateException("Nije inicijalizovan");
                }

                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                List<String> roles = claims.get("authorities", List.class);

                // Proverite da li je username validan
                if (username != null) {
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                // Ispis greške
                System.out.println("Cannot set user authentication: " + e.getMessage());
                e.printStackTrace(); // Ispis kompletne greške na konzolu
            }
        } else {
            System.out.println("Authorization header is missing or does not start with Bearer");
        }

        filterChain.doFilter(request, response);
    }


}
