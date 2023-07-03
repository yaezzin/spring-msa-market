package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class AuthSellerFilter extends AbstractGatewayFilterFactory<AuthSellerFilter.Config> {

    private final Environment env;

    public AuthSellerFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            System.out.println("jwt Seller Filter + " + jwt);


            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            if (!hasSellerAuthority(jwt)) {
                return onError(exchange, "User does not have SELLER authority", HttpStatus.FORBIDDEN);
            }

            System.out.println("????");


            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

    // 토큰의 유효성 확인
    private boolean isJwtValid(String jwt) {
        byte[] keyBytes = Base64.getDecoder().decode(env.getProperty("jwt.secret"));
        boolean returnValue = true;
        String subject = null;

        try {
            subject = Jwts.parserBuilder()
                    .setSigningKey(keyBytes)
                    .build()
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        System.out.println("2. return Value" + returnValue);
        return returnValue;
    }

    // SELLER 권한 확인
    private boolean hasSellerAuthority(String jwt) {
        byte[] keyBytes = Base64.getDecoder().decode(env.getProperty("jwt.secret"));

        // JWT 디코딩하여 권한 정보 확인
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyBytes)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // 권한 정보에서 SELLER 권한 확인
        String authorities = claims.get("role", String.class);

        System.out.println("???? " + authorities);
        if (authorities != null && authorities.equals("ROLE_SELLER")) {
            System.out.println("AUTH : " + authorities);
            return true;
        }
        return false;
    }
}
