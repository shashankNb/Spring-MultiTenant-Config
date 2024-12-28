package com.ea.crm.commons;

import com.ea.crm.dataprovider.exceptions.DataServiceException;
import com.ea.crm.dataprovider.exceptions.ErrorCodes;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.Session;
import org.apache.catalina.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final JdbcTemplate jdbcTemplate;
    private final HttpServletRequest httpServletRequest;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserInfoService(JdbcTemplate jdbcTemplate, HttpServletRequest httpServletRequest) {
        this.jdbcTemplate = jdbcTemplate;
        this.httpServletRequest = httpServletRequest;
    }

    public Map<String, Object> decodeJwtToken(String token) {
        try {
            Map<String, Object> claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, Object> getUser() {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = authorizationHeader != null ? authorizationHeader.replace("Bearer ", "") : null;
        Map<String, Object> claims = decodeJwtToken(token);
        Map<String, Object> user = new HashMap<>();
        if (claims != null) {
            String email = claims.get("sub").toString();
            user = jdbcTemplate.queryForMap(String.format("SELECT * FROM EA_USER.dbo.USERS where email='%s'", email));
            if (user.isEmpty()) {
                throw new DataServiceException("User Not found", ErrorCodes.INTERNAL_SERVER_ERROR);
            }
        }
        return user;
    }
}
