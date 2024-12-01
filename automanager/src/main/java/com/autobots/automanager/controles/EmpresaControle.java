package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

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

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.atualizadores.EmpresaAtualizador;
import com.autobots.automanager.modelo.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
	@Autowired
	private EmpresaRepositorio repositorio;
	@Autowired
	private EmpresaSelecionador selecionador;
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLink;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	@GetMapping("/empresa/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		try {
			Empresa empresa = selecionador.selecionar(repositorio, id);
			
			if(empresa == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(empresa);
				return new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	@GetMapping("/empresas")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		
		if(empresas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(empresas);
			return new ResponseEntity<List<Empresa>>(empresas, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		try {
			if (empresa.getId() == null) {
				repositorio.save(empresa);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
		
		try {
			Empresa empresa = repositorio.getById(atualizacao.getId());
			if(empresa.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				EmpresaAtualizador atualizador = new EmpresaAtualizador();
				atualizador.atualizar(empresa, atualizacao);
				repositorio.save(empresa);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEmpresa(@RequestBody Empresa exclusao) {
		try {
			Empresa empresa = repositorio.getById(exclusao.getId());
			
			if(empresa.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				repositorio.delete(empresa);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/associarUsuario/{usuarioId}/{empresaId}")
	public ResponseEntity<?> associarUsuario(@PathVariable Long usuarioId, @PathVariable Long empresaId){
		try {
			if (usuarioId == null || empresaId == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				Optional<Usuario> novoUsuario = usuarioRepositorio.findById(usuarioId);
				Optional<Empresa> empresaEscolhida = repositorio.findById(empresaId);

				if (novoUsuario.isEmpty() || empresaEscolhida.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} else {
					empresaEscolhida.get().getUsuarios().add(novoUsuario.get());
					repositorio.save(empresaEscolhida.get());
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}
