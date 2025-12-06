package com.sistemaFacturacion.Mambo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.sistemaFacturacion.Mambo.model.Usuario;
import com.sistemaFacturacion.Mambo.model.cliente;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

        private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getToken(Usuario user) {
        UserDetails userDetails = buildUserDetails(user);
        return generateToken(new HashMap<>(), userDetails);
    }

    private UserDetails buildUserDetails(Usuario user) {
        return new User(
            user.getNumeroDocumento(), // username
            user.getContra(),           // password
            user.isEnabled(),           // enabled
            true, true, true,           // account flags
            java.util.List.of(new SimpleGrantedAuthority(user.getRol().getNombre())) // rol
        );
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
     // Verifica si el token pertenece a un usuario válido
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, claims -> claims.getSubject());
    }

    private <T> T getClaim(String token, java.util.function.Function<io.jsonwebtoken.Claims, T> claimsResolver) {
        final io.jsonwebtoken.Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private io.jsonwebtoken.Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, io.jsonwebtoken.Claims::getExpiration);
    }

    public String getTokenCliente(cliente cliente) {
    UserDetails userDetails = buildClienteUserDetails(cliente);
    return generateToken(new HashMap<>(), userDetails);
}

private UserDetails buildClienteUserDetails(cliente cliente) {
    return new User(
        cliente.getNumeroDocumento(),  // username
        cliente.getContra(),            // password
        cliente.isEnabled(),            // enabled
        true, true, true,               // account flags
        java.util.List.of(new SimpleGrantedAuthority(cliente.getRol().getNombre())) // rol
    );
}

}
