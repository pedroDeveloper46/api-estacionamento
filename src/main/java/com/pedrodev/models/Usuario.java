package com.pedrodev.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pedrodev.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@NotBlank(message = "O nome do usuário não pode ser vazio!")
	private String nome;
	
	@NotNull
	@NotBlank(message = "O e-mail do usuário não pode ser vazio!")
	@Column(unique = true)
	@Email(message = "E-mail inválido")
	private String email;
	
	@NotNull
	@NotBlank(message = "A senha do usuário não pode ser vazio!")
	private String senha;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		if (this.role == Role.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		
		return List.of();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return getEmail();
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", role=" + role
				+ "]";
	}
	
	

}
