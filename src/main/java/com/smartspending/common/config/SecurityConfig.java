package com.smartspending.common.config;

import com.smartspending.common.auth.jwt.JwtAuthenticationFilter;
import com.smartspending.common.auth.jwt.JwtTokenProvider;
import com.smartspending.common.auth.oauth2.OAuth2FailHandler;
import com.smartspending.common.auth.oauth2.OAuth2SuccessHandler;
import com.smartspending.common.redis.RedisService;
import com.smartspending.user.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 Origin 허용
        configuration.addAllowedMethod("*");       // 모든 HTTP Method 허용
        configuration.addAllowedHeader("*");       // 모든 헤더 허용
        configuration.setAllowCredentials(true);   // 인증 정보 포함 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 커스텀
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 비활
                .formLogin(AbstractHttpConfigurer::disable) // formLogin 비활
                .logout(AbstractHttpConfigurer::disable) // logout 설정 비활
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session 비활
                .headers((headersConfig) ->
                        headersConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // X-Frame-Options 헤더를 비활성화
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class) // jwt 필터 등록 인증방식
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(
                                        "/", "/login", "/signup", "/dashboard", "/find-password", "/transaction", "/budget",    // 👉 HTML 화면들
                                        "/user/login/**", "/user/logout/**",
                                        "/user/register/**", "/user/find-password/**",  // 패스워드 경로 소문자 일관
                                        "/oauth2/**",                                   // 소셜 로그인 콜백
                                        "/css/**", "/js/**", "/images/**", "/favicon.ico"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                );
        return http.build();
    }
}
