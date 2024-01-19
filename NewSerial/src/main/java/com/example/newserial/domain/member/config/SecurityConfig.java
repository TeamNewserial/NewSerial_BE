package com.example.newserial.domain.member.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.example.newserial.domain.member.config.jwt.AuthEntryPointJwt;
import com.example.newserial.domain.member.config.jwt.AuthTokenFilter;
import com.example.newserial.domain.member.config.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.newserial.domain.member.config.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.newserial.domain.member.config.services.SaveAndDeleteTokenFromRedis;
import com.example.newserial.domain.member.config.services.UserDetailsServiceImpl;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final SaveAndDeleteTokenFromRedis saveAndDeleteTokenFromRedis;
    private final AuthTokenFilter authTokenFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Value("${jwtCookieNameRT}")
    private String RTcookie;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return authTokenFilter;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); //
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //인증 없이 접근할 수 있는 경로: 로그인, 회원 가입
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .sessionFixation().migrateSession()
                )
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                )

                .oauth2Login(oauth2login ->
                        oauth2login
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.logout((logout) ->
                logout.addLogoutHandler(saveAndDeleteTokenFromRedis)
                        .addLogoutHandler(new CookieClearingLogoutHandler(RTcookie))
                        .logoutSuccessUrl("/"));

        return http.build();
    }



}
