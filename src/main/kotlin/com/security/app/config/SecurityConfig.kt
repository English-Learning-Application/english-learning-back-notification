package com.security.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .formLogin {
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
        return http.build()
    }

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder.build()
    }
}
