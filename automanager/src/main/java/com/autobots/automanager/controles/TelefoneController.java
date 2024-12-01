package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.atualizadores.TelefoneAtualizador;
import com.autobots.automanager.modelo.selecionadores.TelefoneSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneController {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private TelefoneSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		try {
			Telefone telefone = selecionador.selecionar(repositorio, id);
			if (telefone == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(telefone);
				return new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		
		if (telefones.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(telefones);
			return new ResponseEntity<List<Telefone>>(telefones, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		try {
			if (telefone.getId() == null) {
				repositorio.save(telefone);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		try {
			Telefone telefone = repositorio.getById(atualizacao.getId());
			
			if (telefone.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				TelefoneAtualizador atualizador = new TelefoneAtualizador();
				atualizador.atualizar(telefone, atualizacao);
				repositorio.save(telefone);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
		try {
			Telefone telefone = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByTelefones(telefone);
			
			if (telefone.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.getTelefones().remove(telefone);
				repositorio.delete(telefone);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Set<Telefone>> pegarTelefonesCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Set<Telefone> telefones = cliente.getTelefones();
			adicionadorLink.adicionarLink(new ArrayList<>(telefones));
			return new ResponseEntity<Set<Telefone>>(telefones, HttpStatus.FOUND);
		}
	}
}
