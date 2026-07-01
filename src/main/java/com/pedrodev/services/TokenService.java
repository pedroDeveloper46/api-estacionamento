package com.pedrodev.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pedrodev.models.Usuario;

@Service //* Marca a classe como um serviço do Spring (bean gerenciado pelo container)
public class TokenService {
    
    @Value("${api.security.token.secret}") //* Injeta o valor da chave secreta definida no application.properties
    private String secret;
    
    public String generateToken(Usuario usuario) { //* Método para gerar um token JWT para um usuário
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); //* Define o algoritmo de assinatura HMAC256 com a chave secreta
            String token = JWT.create() //* Cria um novo token JWT
                    .withIssuer("auth-api") //* Define quem está emitindo o token (issuer)
                    .withSubject(usuario.getEmail()) //* Define o "subject" do token (normalmente o login/email do usuário)
                    .withExpiresAt(genExpirationDate()) //* Define a data de expiração do token
                    .sign(algorithm); //* Assina o token com o algoritmo definido
            
            return token; //* Retorna o token gerado
        } catch (JWTCreationException e) { //* Caso ocorra erro na criação do token
            throw new RuntimeException("Erro ao gerar o Token:" + e.getMessage()); //* Lança exceção com mensagem de erro
        }
    }
    
    public String validateToken(String token) { //* Método para validar um token JWT recebido
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); //* Define o algoritmo com a mesma chave secreta usada na criação
            return JWT.require(algorithm) //* Cria um verificador de token
                    .withIssuer("auth-api") //* Exige que o issuer seja "auth-api"
                    .build() //* Constrói o verificador
                    .verify(token) //* Verifica se o token é válido
                    .getSubject(); //* Retorna o subject (login/email do usuário) contido no token
        } catch (JWTVerificationException e) { //* Caso o token seja inválido ou expirado
            return ""; //* Retorna string vazia (não autenticado)
        }
    }
    
    private Instant genExpirationDate() { //* Método auxiliar para gerar a data de expiração do token
        return LocalDateTime.now().plusHours(2) //* Define expiração para 2 horas a partir de agora
                .toInstant(ZoneOffset.of("-03:00")); //* Converte para Instant com fuso horário -03:00 (Brasil)
    }
}
