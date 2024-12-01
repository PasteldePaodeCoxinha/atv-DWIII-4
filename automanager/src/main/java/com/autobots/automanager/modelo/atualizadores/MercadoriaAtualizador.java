package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class MercadoriaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
		if (atualizacao != null) {
			if (atualizacao.getValidade() != null) {
				mercadoria.setValidade(atualizacao.getValidade());
			}
			
			if (atualizacao.getFabricao() != null) {
				mercadoria.setFabricao(atualizacao.getFabricao());
			}
			
			if (atualizacao.getCadastro() != null) {
				mercadoria.setCadastro(atualizacao.getCadastro());
			}
			
			if (!verificador.verificar(atualizacao.getNome())) {
				mercadoria.setNome(atualizacao.getNome());
			}
			
			if (atualizacao.getQuantidade() != null) {
				mercadoria.setQuantidade(atualizacao.getQuantidade());
			}
			
			if (atualizacao.getValor() != null) {
				mercadoria.setValor(atualizacao.getValor());
			}
			
			if (atualizacao.getDescricao() != null) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
		}
	}

	public void atualizar(Set<Mercadoria> mercadorias, Set<Mercadoria> atualizacoes) {
		for (Mercadoria atualizacao : atualizacoes) {
			for (Mercadoria mercadoria : mercadorias) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == mercadoria.getId()) {
						atualizar(mercadoria, atualizacao);
					}
				}
			}
		}
	}
}
