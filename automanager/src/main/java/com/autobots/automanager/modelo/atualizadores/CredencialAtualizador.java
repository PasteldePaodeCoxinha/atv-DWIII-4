package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class CredencialAtualizador {
	
	private StringVerificadorNulo verificador;

	public void atualizar(Credencial credencial, Credencial atualizacao) {
		if (atualizacao != null) {
			if (atualizacao.getUltimoAcesso() != null) {
				credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
			}
			
			if (!verificador.verificar(atualizacao.getNomeUsuario())) {
				credencial.setNomeUsuario(atualizacao.getNomeUsuario());
			}
			
			if (!verificador.verificar(atualizacao.getSenha())) {
				credencial.setSenha(atualizacao.getSenha());
			}
		}
	}

	public void atualizar(Set<Credencial> credencials, Set<Credencial> atualizacoes) {
		for (Credencial atualizacao : atualizacoes) {
			for (Credencial credencial : credencials) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == credencial.getId()) {
						atualizar(credencial, atualizacao);
					}
				}
			}
		}
	}
}
