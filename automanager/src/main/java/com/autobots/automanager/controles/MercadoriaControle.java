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
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelo.atualizadores.MercadoriaAtualizador;
import com.autobots.automanager.modelo.selecionadores.MercadoriaSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {
	@Autowired
	private MercadoriaRepositorio repositorio;
	@Autowired
	private MercadoriaSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;

	@GetMapping("/mercadoria/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
		try {
			Mercadoria mercadoria = selecionador.selecionar(repositorio, id);
			if (mercadoria == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(mercadoria);
				return new ResponseEntity<Mercadoria>(mercadoria, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/mercadorias")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		
		if (mercadorias.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(mercadorias);
			return new ResponseEntity<List<Mercadoria>>(mercadorias, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		try {
			if (mercadoria.getId() == null) {
				repositorio.save(mercadoria);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
		try {
			Mercadoria mercadoria = repositorio.getById(atualizacao.getId());
			
			if (mercadoria.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				MercadoriaAtualizador atualizador = new MercadoriaAtualizador();
				atualizador.atualizar(mercadoria, atualizacao);
				repositorio.save(mercadoria);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirMercadoria(@RequestBody Mercadoria exclusao) {
		try {
			Mercadoria mercadoria = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByMercadorias(mercadoria);
			
			if (mercadoria.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.getMercadorias().remove(mercadoria);
				repositorio.delete(mercadoria);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Set<Mercadoria>> pegarMercadoriasCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Set<Mercadoria> mercadorias = cliente.getMercadorias();
			adicionadorLink.adicionarLink(new ArrayList<>(mercadorias));
			return new ResponseEntity<Set<Mercadoria>>(mercadorias, HttpStatus.FOUND);
		}
	}
}
