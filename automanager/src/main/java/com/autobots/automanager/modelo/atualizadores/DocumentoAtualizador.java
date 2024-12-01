package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class DocumentoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Documento documento, Documento atualizacao) {
		if (atualizacao != null) {
			if (atualizacao.getTipo() != null) {
				documento.setTipo(atualizacao.getTipo());
			}
			
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
			
			if (atualizacao.getDataEmissao() != null) {
				documento.setDataEmissao(atualizacao.getDataEmissao());
			}
		}
	}

	public void atualizar(Set<Documento> documentos, Set<Documento> atualizacoes) {
		for (Documento atualizacao : atualizacoes) {
			for (Documento documento : documentos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == documento.getId()) {
						atualizar(documento, atualizacao);
					}
				}
			}
		}
	}
}
