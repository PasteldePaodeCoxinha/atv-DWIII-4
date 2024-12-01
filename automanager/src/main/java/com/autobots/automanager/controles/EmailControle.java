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
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkEmail;
import com.autobots.automanager.modelo.atualizadores.EmailAtualizador;
import com.autobots.automanager.modelo.selecionadores.EmailSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.EmailRepositorio;

@RestController
@RequestMapping("/email")
public class EmailControle {
	@Autowired
	private EmailRepositorio repositorio;
	@Autowired
	private EmailSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkEmail adicionadorLink;

	@GetMapping("/email/{id}")
	public ResponseEntity<Email> obterEmail(@PathVariable long id) {
		try {
			Email email = selecionador.selecionar(repositorio, id);
			if (email == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(email);
				return new ResponseEntity<Email>(email, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/emails")
	public ResponseEntity<List<Email>> obterEmails() {
		List<Email> emails = repositorio.findAll();
		
		if (emails.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(emails);
			return new ResponseEntity<List<Email>>(emails, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmail(@RequestBody Email email) {
		try {
			if (email.getId() == null) {
				repositorio.save(email);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmail(@RequestBody Email atualizacao) {
		try {
			Email email = repositorio.getById(atualizacao.getId());
			
			if (email.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				EmailAtualizador atualizador = new EmailAtualizador();
				atualizador.atualizar(email, atualizacao);
				repositorio.save(email);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEmail(@RequestBody Email exclusao) {
		try {
			Email email = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByEmails(email);
			
			if (email.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.getEmails().remove(email);
				repositorio.delete(email);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Set<Email>> pegarEmailsCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Set<Email> emails = cliente.getEmails();
			adicionadorLink.adicionarLink(new ArrayList<>(emails));
			return new ResponseEntity<Set<Email>>(emails, HttpStatus.FOUND);
		}
	}
}
