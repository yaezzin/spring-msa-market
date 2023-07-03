package com.example.inventoryservice.security;

import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurity {

    private InventoryService inventoryService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] WHITE_LIST = {
            "/actuator/**",
            "/",
            "/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LIST).permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(new IpAddressMatcher("192.168.35.230")).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));
        return http.build();
    }
}
