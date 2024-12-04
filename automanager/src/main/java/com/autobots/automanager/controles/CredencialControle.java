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
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkCredencial;
import com.autobots.automanager.modelo.atualizadores.CredencialAtualizador;
import com.autobots.automanager.modelo.selecionadores.CredencialSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.CredencialRepositorio;

@RestController
@RequestMapping("/credencial")
public class CredencialControle {
	@Autowired
	private CredencialRepositorio repositorio;
	@Autowired
	private CredencialSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkCredencial adicionadorLink;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/credencial/{id}")
	public ResponseEntity<Credencial> obterCredencial(@PathVariable long id) {
		try {
			Credencial credencial = selecionador.selecionar(repositorio, id);
			if (credencial == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(credencial);
				return new ResponseEntity<Credencial>(credencial, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/credencials")
	public ResponseEntity<List<Credencial>> obterCredencials() {
		List<Credencial> credencials = repositorio.findAll();
		
		if (credencials.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(credencials);
			return new ResponseEntity<List<Credencial>>(credencials, HttpStatus.FOUND);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/cadastroUsuarioSenha")
	public ResponseEntity<?> cadastrarCredencial(@RequestBody Credencial credencial) {
		try {
			if (credencial.getId() == null) {
				repositorio.save(credencial);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarCredencial(@RequestBody Credencial atualizacao) {
		try {
			Credencial credencial = repositorio.getById(atualizacao.getId());
			
			if (credencial.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				CredencialAtualizador atualizador = new CredencialAtualizador();
				atualizador.atualizar(credencial, atualizacao);
				repositorio.save(credencial);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirCredencial(@RequestBody Credencial exclusao) {
		try {
			Credencial credencial = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByCredencial(credencial);
			
			if (credencial.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.setCredencial(null);
				repositorio.delete(credencial);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Credencial> pegarCredencialsCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Credencial credencials = cliente.getCredencial();
			adicionadorLink.adicionarLink(credencials);
			return new ResponseEntity<Credencial>(credencials, HttpStatus.FOUND);
		}
	}
}
