package com.finance.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtGatewayFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    private final SecretKey secretKey;

    public JwtGatewayFilter(
            @Value("${jwt.secret}") String secret) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        // Public endpoints
        if (path.contains("/api/auth/")
                || path.equals("/user-service/api/users")) {

            return chain.filter(exchange);
        }

        String authorizationHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            return unauthorized(exchange);
        }

        String token =
                authorizationHeader.substring(7);

        try {

            Claims claims =
                    Jwts.parser()
                            .verifyWith(secretKey)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

            String email = claims.getSubject();

            String role =
                    claims.get("role", String.class);

            if (email == null || role == null) {
                return unauthorized(exchange);
            }

            return chain.filter(exchange);

        } catch (Exception exception) {

            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(
            ServerWebExchange exchange) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse()
                .setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}