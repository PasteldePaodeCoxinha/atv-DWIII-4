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

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkServico;
import com.autobots.automanager.modelo.atualizadores.ServicoAtualizador;
import com.autobots.automanager.modelo.selecionadores.ServicoSelecionador;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
@RequestMapping("/servico")
public class ServicoControle {
	@Autowired
	private ServicoRepositorio repositorio;
	@Autowired
	private ServicoSelecionador selecionador;
	@Autowired
	private AdicionadorLinkServico adicionadorLink;

	@GetMapping("/servico/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
		try {
			Servico servico = selecionador.selecionar(repositorio, id);
			
			if(servico == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(servico);
				return new ResponseEntity<Servico>(servico, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	@GetMapping("/servicos")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		
		if(servicos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(servicos);
			return new ResponseEntity<List<Servico>>(servicos, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
		try {
			if (servico.getId() == null) {
				repositorio.save(servico);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
		
		try {
			Servico servico = repositorio.getById(atualizacao.getId());
			if(servico.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				ServicoAtualizador atualizador = new ServicoAtualizador();
				atualizador.atualizar(servico, atualizacao);
				repositorio.save(servico);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirServico(@RequestBody Servico exclusao) {
		try {
			Servico servico = repositorio.getById(exclusao.getId());
			
			if(servico.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				repositorio.delete(servico);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
	}
}
