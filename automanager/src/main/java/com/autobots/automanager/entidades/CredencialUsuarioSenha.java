package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
public class CredencialUsuarioSenha extends Credencial {
	
	@Column(unique = true, nullable = false)
	private String nomeUsuario;
	
	@Column(nullable = false)
	private String senha;
}
