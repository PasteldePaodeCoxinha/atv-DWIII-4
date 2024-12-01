package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@Component
public class ServicoSelecionador {
	public Servico selecionar(ServicoRepositorio bancoServico, long id) {
		Servico selecionado = bancoServico.findById(id).orElseGet(null);
		return selecionado;
	}
}