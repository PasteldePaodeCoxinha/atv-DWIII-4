package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Component
public class VendaSelecionador {
	public Venda selecionar(VendaRepositorio bancoVenda, long id) {
		Venda selecionado = bancoVenda.findById(id).orElseGet(null);
		return selecionado;
	}
}