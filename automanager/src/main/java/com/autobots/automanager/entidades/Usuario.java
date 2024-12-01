package com.autobots.automanager.entidades;

import java.util.HashSet;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"mercadorias", "vendas", "veiculos"}, callSuper = false)
@Entity
public class Usuario extends RepresentationModel<Usuario>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column
	private String nomeSocial;
	
	@Column
	private Date dataNascimento;
	
	@Column
	private Date dataCadastro;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Documento> documentos = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Endereco endereco;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Telefone> telefones = new HashSet<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<PerfilUsuario> perfis = new HashSet<PerfilUsuario>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Email> emails = new HashSet<Email>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Credencial> credenciais = new HashSet<Credencial>();
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Set<Mercadoria> mercadorias = new HashSet<Mercadoria>();
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonManagedReference
	private Set<Venda> vendas = new HashSet<Venda>();
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonManagedReference
	private Set<Veiculo> veiculos = new HashSet<Veiculo>();
}