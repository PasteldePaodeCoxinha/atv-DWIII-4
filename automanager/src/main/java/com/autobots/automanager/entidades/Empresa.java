package com.autobots.automanager.entidades;

import java.util.HashSet;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Empresa extends RepresentationModel<Empresa> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String razaoSocial;
	
	@Column(nullable = false)
	private String nomeFantasia;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Telefone> telefones = new HashSet<Telefone>();
	
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
	
	@Column(nullable = false)
	private Date cadastro;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Usuario> usuarios = new HashSet<Usuario>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Mercadoria> mercadorias = new HashSet<Mercadoria>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Servico> servicos = new HashSet<Servico>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Venda> vendas = new HashSet<Venda>();
}
