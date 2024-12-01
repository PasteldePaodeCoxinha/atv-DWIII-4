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
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelo.atualizadores.VeiculoAtualizador;
import com.autobots.automanager.modelo.selecionadores.VeiculoSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
	@Autowired
	private VeiculoRepositorio repositorio;
	@Autowired
	private VeiculoSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;

	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		try {
			Veiculo veiculo = selecionador.selecionar(repositorio, id);
			if (veiculo == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(veiculo);
				return new ResponseEntity<Veiculo>(veiculo, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		
		if (veiculos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(veiculos);
			return new ResponseEntity<List<Veiculo>>(veiculos, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		try {
			if (veiculo.getId() == null) {
				repositorio.save(veiculo);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
		try {
			Veiculo veiculo = repositorio.getById(atualizacao.getId());
			
			if (veiculo.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				VeiculoAtualizador atualizador = new VeiculoAtualizador();
				atualizador.atualizar(veiculo, atualizacao);
				repositorio.save(veiculo);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVeiculo(@RequestBody Veiculo exclusao) {
		try {
			Veiculo veiculo = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByVeiculos(veiculo);
			
			if (veiculo.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.getVeiculos().remove(veiculo);
				repositorio.delete(veiculo);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Set<Veiculo>> pegarVeiculosCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Set<Veiculo> veiculos = cliente.getVeiculos();
			adicionadorLink.adicionarLink(new ArrayList<>(veiculos));
			return new ResponseEntity<Set<Veiculo>>(veiculos, HttpStatus.FOUND);
		}
	}
}
