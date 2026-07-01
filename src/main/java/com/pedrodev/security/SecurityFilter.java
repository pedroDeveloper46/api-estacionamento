package com.pedrodev.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pedrodev.repositories.UsuarioRepository;
import com.pedrodev.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component //* Marca a classe como um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter { //* Filtro que roda uma vez por requisição

    @Autowired
    private TokenService tokenService; //* Serviço responsável por validar e extrair informações do token JWT

    @Autowired
    private UsuarioRepository usuarioRepository; //* Repositório para buscar usuários no banco (não usado diretamente aqui)

    @Autowired
    private UserDetailsService userDetailsService; //* Serviço que carrega os dados do usuário (UserDetails) pelo login/email

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { //* Método principal do filtro, chamado em cada requisição

        String path = request.getServletPath(); //* Obtém o caminho da requisição (endpoint chamado)
        if (path.equals("/autenticacao/login")) { //* Se for o endpoint de login...
            filterChain.doFilter(request, response); //* ...não aplica o filtro JWT e segue o fluxo normal
            return; //* encerra aqui para não validar token no login
        }

        var token = recoverToken(request); //* Recupera o token JWT do header Authorization

        if(token != null) { //* Se o token existe...
            var login = tokenService.validateToken(token); //* Valida o token e extrai o login (normalmente o email)
            UserDetails details = userDetailsService.loadUserByUsername(login); //* Carrega o usuário do banco via UserDetailsService

            if (details != null) { //* Se encontrou o usuário...
                var authentication = new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities()); //* Cria objeto de autenticação com usuário e permissões
                SecurityContextHolder.getContext().setAuthentication(authentication); //* Coloca o usuário autenticado no contexto do Spring Security
            }
        }

        filterChain.doFilter(request, response); //* Continua o fluxo da requisição (outros filtros ou controller)
    }

    private String recoverToken(HttpServletRequest request) { //* Método auxiliar para recuperar o token do header
        var authHeader = request.getHeader("Authorization"); //* Pega o header Authorization

        if (authHeader == null) { //* Se não existe, retorna null
            return null;
        }

        return authHeader.replace("Bearer ", ""); //* Remove o prefixo "Bearer " e retorna só o token
    }
}
