package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Telefone;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	
	Usuario findByDocumentos(Documento documento);
	Usuario findByEndereco(Endereco endereco);
	Usuario findByTelefones(Telefone telefone);
	Usuario findByEmails(Email email);
	Usuario findByCredenciais(Credencial credencial);
	Usuario findByMercadorias(Mercadoria mercadoria);
	Usuario findByVendas(Venda venda);
	Usuario findByVeiculos(Veiculo veiculo);
}