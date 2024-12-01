package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.repositorios.EmailRepositorio;

@Component
public class EmailSelecionador {
	public Email selecionar(EmailRepositorio bancoEmail, long id) {
		Email selecionado = bancoEmail.findById(id).orElseGet(null);
		return selecionado;
	}
}