package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.repositorios.CredencialRepositorio;

@Component
public class CredencialSelecionador {
	public Credencial selecionar(CredencialRepositorio bancoCredencial, long id) {
		Credencial selecionado = bancoCredencial.findById(id).orElseGet(null);
		return selecionado;
	}
}