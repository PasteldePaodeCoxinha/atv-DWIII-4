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
public class CredencialCodigoBarra extends Credencial {
	
	@Column(unique = true, nullable = false)
	private Long codigo;
}
