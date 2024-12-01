package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@Component
public class VeiculoSelecionador {
	public Veiculo selecionar(VeiculoRepositorio bancoVeiculo, long id) {
		Veiculo selecionado = bancoVeiculo.findById(id).orElseGet(null);
		return selecionado;
	}
}