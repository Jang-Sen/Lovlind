package com.project.lovlind.configuration;

import com.project.lovlind.configuration.dsl.JwtFilterDsl;
import com.project.lovlind.conmon.security.handler.AuthenticationEntryPointHandler;
import com.project.lovlind.conmon.security.handler.LogoutSuccessCustomHandler;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilterDsl jwtFilterDsl;
  private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
  private final LogoutSuccessCustomHandler logoutSuccessCustomHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.with(jwtFilterDsl, JwtFilterDsl::build);
    http.headers(headerConfig -> headerConfig.frameOptions(FrameOptionsConfig::disable));
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        // cors 관련 옵션 끄기
        .cors(cors -> cors.configurationSource(apiConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPointHandler))
        .logout(logout -> logout.logoutSuccessHandler(logoutSuccessCustomHandler).logoutUrl("/api/logout"));
    return http.build();
  }

  private CorsConfigurationSource apiConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("http://localhost:[*]"
            , "https://localhost:[*]"
            , "http://www.lovlind.me:[*]"
            , "https://www.lovlind.me:[*]"
            , "https://loveliend.vercel.app:[*]"
            , "http://loveliend.vercel.app:[*]"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONAL", "OPTION"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization", "Location", "Refresh", "authorization"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
