package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class VendaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	
	private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();
	private MercadoriaAtualizador mercadoriaAtualizador = new MercadoriaAtualizador();
	private ServicoAtualizador servicoAtualizador = new ServicoAtualizador();
	private VeiculoAtualizador veiculoAtualizador = new VeiculoAtualizador();

	public void atualizarDados(Venda venda, Venda atualizacao) {
		if (atualizacao != null) {
			if (atualizacao.getCadastro() != null) {
				venda.setCadastro(atualizacao.getCadastro());
			}
			
			if (!verificador.verificar(atualizacao.getIdentificacao())) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}
		}
	}
	
	public void atualizar(Venda venda, Venda atualizacao) {
		atualizarDados(venda, atualizacao);
		mercadoriaAtualizador.atualizar(venda.getMercadorias(), atualizacao.getMercadorias());
		servicoAtualizador.atualizar(venda.getServicos(), atualizacao.getServicos());
		veiculoAtualizador.atualizar(venda.getVeiculo(), atualizacao.getVeiculo());
		if (atualizacao.getCliente() != null) {
			usuarioAtualizador.atualizar(venda.getCliente(), atualizacao.getCliente());
		}
		if (atualizacao.getFuncionario() != null) {
			usuarioAtualizador.atualizar(venda.getFuncionario(), atualizacao.getFuncionario());
		}
	}

	public void atualizar(Set<Venda> vendas, Set<Venda> atualizacoes) {
		for (Venda atualizacao : atualizacoes) {
			for (Venda venda : vendas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == venda.getId()) {
						atualizar(venda, atualizacao);
					}
				}
			}
		}
	}
}
