package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class VeiculoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	
	private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();
	private VendaAtualizador vendaAtualizador = new VendaAtualizador();

	public void atualizarDados(Veiculo veiculo, Veiculo atualizacao) {
		if (atualizacao != null) {
			if (atualizacao.getTipo() != null) {
				veiculo.setTipo(atualizacao.getTipo());
			}
			
			if (!verificador.verificar(atualizacao.getModelo())) {
				veiculo.setModelo(atualizacao.getModelo());
			}
			
			if (!verificador.verificar(atualizacao.getPlaca())) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
		}
	}
	
	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		atualizarDados(veiculo, atualizacao);
		usuarioAtualizador.atualizar(veiculo.getProprietario(), atualizacao.getProprietario());
		vendaAtualizador.atualizar(veiculo.getVendas(), veiculo.getVendas());
	}

	public void atualizar(Set<Veiculo> veiculos, Set<Veiculo> atualizacoes) {
		for (Veiculo atualizacao : atualizacoes) {
			for (Veiculo veiculo : veiculos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == veiculo.getId()) {
						atualizar(veiculo, atualizacao);
					}
				}
			}
		}
	}
}
