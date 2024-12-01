package com.autobots.automanager.controles;

import java.util.List;

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
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.atualizadores.UsuarioAtualizador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private UsuarioSelecionador selecionador;
	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;

	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		try {
			Usuario usuario = selecionador.selecionar(repositorio, id);
			
			if(usuario == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(usuario);
				return new ResponseEntity<Usuario>(usuario, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		
		if(usuarios.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(usuarios);
			return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		try {
			if (usuario.getId() == null) {
				repositorio.save(usuario);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
		
		try {
			Usuario usuario = repositorio.getById(atualizacao.getId());
			if(usuario.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				UsuarioAtualizador atualizador = new UsuarioAtualizador();
				atualizador.atualizar(usuario, atualizacao);
				repositorio.save(usuario);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
		try {
			Usuario usuario = repositorio.getById(exclusao.getId());
			
			if(usuario.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				repositorio.delete(usuario);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
	}
}
