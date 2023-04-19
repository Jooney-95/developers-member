package com.developers.member.config;

import com.developers.member.auth.security.exception.CustomeBasicAuthenticationEntryPoint;
import com.developers.member.auth.security.filter.LoginFilter;
import com.developers.member.auth.security.filter.RefreshTokenFilter;
import com.developers.member.auth.security.filter.TokenCheckFilter;
import com.developers.member.auth.security.handler.LoginSuccessHandler;
import com.developers.member.auth.security.service.MemberDetailsService;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final MemberRepository memberRepository;
    private final MemberDetailsService memberDetailService;
    private final JwtUtil jwtUtil;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000")); // 모든 요청에 설정
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")); // 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 헤더 설정
        configuration.setAllowCredentials(true); //인증 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // 웹에서의 시큐리티 적용 설정 - 설정 파일은 security 적용 대상아 아님
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("------------web configure-------------------");
        return (web) -> web.ignoring().
                requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private TokenCheckFilter tokenCheckFilter(JwtUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomeBasicAuthenticationEntryPoint();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-------------------configure-------------------------------");
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberDetailService).passwordEncoder(passwordEncoder());
        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        //반드시 필요
        http.authenticationManager(authenticationManager);
        //APILoginFilter
        //스프링 Security에서 username 과 password를 처리하는 UsernamePasswordAuthenticationFilter 의 앞쪽에서 동작하도록 설정
        LoginFilter apiLoginFilter = new LoginFilter("/api/auth/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        // APILoginFilter 다음에 동작할 핸들러 생성하기
        // 로그인 성공과 실패에 따른 핸들러를 설정할 수 있다.
        // 생성자를 이용해 주입한다.
        LoginSuccessHandler successHandler = new LoginSuccessHandler(jwtUtil, memberRepository);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // APILoginFilter의 위치 조정, 로그인 필터 적용
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
        // Acess 토큰 검증 필터 적용하기
        http.addFilterBefore(tokenCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        // Refresh 토큰 컴증 필터를 적용하기
        http.addFilterBefore(new RefreshTokenFilter("/api/auth/refresh", jwtUtil), TokenCheckFilter.class);
        // API Server는 세션을 사용하지 않기에 세션 사용 중지
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // API Server에서 동작헤야 하기 때문에 CORS 설정 추가
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        http
                .csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/authenticate", "/ouath2/**", "/docs/**", "/api/**", "/api/auth/**").permitAll()
                .anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/")
                .failureForwardUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/");
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        return http.build();
    }
}