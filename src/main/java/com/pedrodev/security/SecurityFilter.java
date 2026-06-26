package com.pedrodev.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pedrodev.repositories.UsuarioRepository;
import com.pedrodev.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		// TODO Auto-generated method stub
		 String path = request.getServletPath();
		 if (path.equals("/autenticacao/login")) {
		        filterChain.doFilter(request, response);
		        return;
		 }
		
		var token = recoverToken(request);
		
		if(token != null) {
			var login = tokenService.validateToken(token);
			UserDetails details = usuarioRepository.findByEmail(login);
			var authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		
		
		filterChain.doFilter(request, response);
	}
	
	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		
		if (authHeader == null) {
			return null;
		}
		
		return authHeader.replace("Bearer ", "");
	}

}
