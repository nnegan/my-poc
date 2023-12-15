package com.my.poc.framework


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableMethodSecurity
@Configuration
class SecurityConfig (private val jwtAuthenticationFilter: JwtAuthenticationFilter) {
    private val allowedUrls = arrayOf("/v3/**", "/", "/swagger-ui/**", "/member/sign-up", "/member/sign-in")

    @Bean
    fun filterChain(http: HttpSecurity) = http
            .csrf().disable()
            .headers { it.frameOptions().sameOrigin() }
            .authorizeHttpRequests { request ->
                request.requestMatchers(*allowedUrls).permitAll()
                        .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)
            .build()!!

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}