package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@Component
public class MercadoriaSelecionador {
	public Mercadoria selecionar(MercadoriaRepositorio bancoMercadoria, long id) {
		Mercadoria selecionado = bancoMercadoria.findById(id).orElseGet(null);
		return selecionado;
	}
}