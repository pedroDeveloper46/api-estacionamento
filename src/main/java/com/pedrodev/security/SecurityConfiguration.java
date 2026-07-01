package com.pedrodev.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration //* Indica que esta classe é uma configuração do Spring
@EnableWebSecurity //* Ativa o suporte ao Spring Security
public class SecurityConfiguration {
    
    @Autowired
    SecurityFilter securityFilter; //* Injeta o filtro customizado que valida o token JWT
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //* Desabilita CSRF (não necessário em APIs stateless)
                .cors(cors -> {}) //* Habilita suporte a CORS usando o bean corsConfigurationSource()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //* Define que não haverá sessão, API será stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //* Libera requisições OPTIONS (pré-flight do CORS)
                        .requestMatchers(HttpMethod.POST, "/autenticacao/login").permitAll() //* Libera login sem autenticação
                        .requestMatchers(HttpMethod.POST, "/usuario/cadastrar").permitAll() //* Libera cadastro sem autenticação
                        .anyRequest().authenticated() //* Qualquer outra requisição precisa estar autenticada
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //* Adiciona o filtro JWT antes do filtro padrão de autenticação
                .build(); //* Constrói a cadeia de filtros de segurança
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //* Define o algoritmo de criptografia de senha (BCrypt)
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); //* Cria configuração de CORS
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); //* Define quais origens podem acessar a API (frontend)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); //* Define métodos HTTP permitidos
        configuration.setAllowedHeaders(List.of("*")); //* Permite todos os headers
        configuration.setAllowCredentials(true); //* Permite envio de credenciais (cookies, Authorization header)
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //* Cria fonte de configuração baseada em URL
        source.registerCorsConfiguration("/**", configuration); //* Aplica configuração de CORS para todos os endpoints
        
        return source; //* Retorna configuração para ser usada pelo Spring Security
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); //* Expõe o AuthenticationManager para autenticação de login
    }
}
