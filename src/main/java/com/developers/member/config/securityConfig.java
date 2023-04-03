//package com.developers.member.config;
//
//import com.developers.member.member.entity.Role;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//public class securityConfig {
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/authenticate", "/ouath2/**", "/api/login", "/api/register", "/api/profile/**").permitAll()
//                .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
//                .anyRequest().authenticated()
//
//                .and()
//                .logout()
//                .logoutSuccessUrl("/");
////                .and()
////                .oauth2Login()
////                .userInfoEndpoint()
////                .userService(customOAuth2UserService);
//        return http.build();
//    }
//
//}
