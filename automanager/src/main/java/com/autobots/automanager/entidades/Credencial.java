package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Credencial extends RepresentationModel<Credencial> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Date criacao;
	
	@Column
	private Date ultimoAcesso;
	
	@Column(nullable = false)
	private boolean inativo;
	
	@Column(unique = true, nullable = false)
	private String nomeUsuario;
	
	@Column(nullable = false)
	@JsonIgnore
	private String senha;
}
