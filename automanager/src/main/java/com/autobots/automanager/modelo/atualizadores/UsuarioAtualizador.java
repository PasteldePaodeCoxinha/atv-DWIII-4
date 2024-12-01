package com.autobots.automanager.modelo.atualizadores;

import java.util.Set;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.StringVerificadorNulo;

public class UsuarioAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
	private DocumentoAtualizador documentoAtualizador = new DocumentoAtualizador();
	private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();
	private EmailAtualizador emailAtualizador = new EmailAtualizador();
	private CredencialAtualizador credencialAtualizador = new CredencialAtualizador();
	private MercadoriaAtualizador mercadoriaAtualizador = new MercadoriaAtualizador();
	private VendaAtualizador vendaAtualizador = new VendaAtualizador();
	private VeiculoAtualizador veiculoAtualizador = new VeiculoAtualizador();

	private void atualizarDados(Usuario usuario, Usuario atualizacao) {
		if (!verificador.verificar(atualizacao.getNome())) {
			usuario.setNome(atualizacao.getNome());
		}
		if (!verificador.verificar(atualizacao.getNomeSocial())) {
			usuario.setNomeSocial(atualizacao.getNomeSocial());
		}
		if (!(atualizacao.getDataCadastro() == null)) {
			usuario.setDataCadastro(atualizacao.getDataCadastro());
		}
		if (!(atualizacao.getDataNascimento() == null)) {
			usuario.setDataNascimento(atualizacao.getDataNascimento());
		}
	}

	public void atualizar(Usuario usuario, Usuario atualizacao) {
		atualizarDados(usuario, atualizacao);
		enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
		documentoAtualizador.atualizar(usuario.getDocumentos(), atualizacao.getDocumentos());
		telefoneAtualizador.atualizar(usuario.getTelefones(), atualizacao.getTelefones());
		emailAtualizador.atualizar(usuario.getEmails(), atualizacao.getEmails());
		credencialAtualizador.atualizar(usuario.getCredenciais(), atualizacao.getCredenciais());
		mercadoriaAtualizador.atualizar(usuario.getMercadorias(), atualizacao.getMercadorias());
		vendaAtualizador.atualizar(usuario.getVendas(), atualizacao.getVendas());
		veiculoAtualizador.atualizar(usuario.getVeiculos(), atualizacao.getVeiculos());
	}
	
	public void atualizar(Set<Usuario> usuarios, Set<Usuario> atualizacoes) {
		for (Usuario atualizacao : atualizacoes) {
			for (Usuario usuario : usuarios) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == usuario.getId()) {
						atualizar(usuario, atualizacao);
					}
				}
			}
		}
	}
}
