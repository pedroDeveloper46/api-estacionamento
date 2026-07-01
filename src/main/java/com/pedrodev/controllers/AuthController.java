package com.pedrodev.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedrodev.exceptions.ErrorResponse;
import com.pedrodev.models.LoginDTO;
import com.pedrodev.models.LoginResponseDTO;
import com.pedrodev.models.Usuario;
import com.pedrodev.services.TokenService;

import jakarta.validation.Valid;

@RestController //* Define que esta classe é um controller REST (retorna JSON)
@RequestMapping(path="/autenticacao") //* Define o prefixo da rota para todos os endpoints deste controller
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager; //* Injeta o gerenciador de autenticação do Spring Security
    
    @Autowired
    private TokenService tokenService; //* Injeta o serviço responsável por gerar e validar tokens JWT
    
    @PostMapping(path="/login") //* Define o endpoint POST /autenticacao/login
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) { //* Recebe o corpo da requisição com email e senha (validados)
        
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getSenha() //* Cria um objeto de autenticação com email e senha
            );

            var auth = authenticationManager.authenticate(usernamePassword); //* Autentica o usuário usando o AuthenticationManager

            var token = tokenService.generateToken((Usuario) auth.getPrincipal()); //* Gera um token JWT para o usuário autenticado

            return ResponseEntity.ok(new LoginResponseDTO(token)); //* Retorna 200 OK com o token no corpo da resposta

        } catch (AuthenticationException e) { //* Caso a autenticação falhe (credenciais inválidas)
            
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), //* Código HTTP 400
                "Erro de negócio", //* Tipo de erro
                "Credenciais inválidas: Verifique seu e-mail e senha", //* Mensagem amigável para o usuário
                LocalDateTime.now() //* Timestamp do erro
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); //* Retorna 400 com JSON estruturado de erro
        }
    }
}
