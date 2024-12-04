package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.atualizadores.EnderecoAtualizador;
import com.autobots.automanager.modelo.selecionadores.EnderecoSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private EnderecoSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		try {
			Endereco endereco = selecionador.selecionar(repositorio, id);
			if (endereco == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(endereco);
				return new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		
		if (enderecos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(enderecos);
			return new ResponseEntity<List<Endereco>>(enderecos, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		try {
			if (endereco.getId() == null) {
				repositorio.save(endereco);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		try {
			Endereco endereco = repositorio.getById(atualizacao.getId());
			
			if (endereco.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				EnderecoAtualizador atualizador = new EnderecoAtualizador();
				atualizador.atualizar(endereco, atualizacao);
				repositorio.save(endereco);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
		try {
			Endereco endereco = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByEndereco(endereco);
			
			if (endereco.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.setEndereco(null);
				repositorio.delete(endereco);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Endereco> pegarEnderecoCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Endereco endereco = cliente.getEndereco();
			adicionadorLink.adicionarLink(endereco);
			return new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
		}
	}
}
