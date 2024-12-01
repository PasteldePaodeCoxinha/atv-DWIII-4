package com.autobots.automanager.modelo.selecionadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@Component
public class EmpresaSelecionador {
	public Empresa selecionar(EmpresaRepositorio bancoEmpresa, long id) {
		Empresa selecionado = bancoEmpresa.findById(id).orElseGet(null);
		return selecionado;
	}
}