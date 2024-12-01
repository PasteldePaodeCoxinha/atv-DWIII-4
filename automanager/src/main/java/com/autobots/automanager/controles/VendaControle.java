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
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.adicionadoresLink.AdicionadorLinkVenda;
import com.autobots.automanager.modelo.atualizadores.VendaAtualizador;
import com.autobots.automanager.modelo.selecionadores.VendaSelecionador;
import com.autobots.automanager.modelo.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
@RequestMapping("/venda")
public class VendaControle {
	@Autowired
	private VendaRepositorio repositorio;
	@Autowired
	private VendaSelecionador selecionador;
	@Autowired
	private UsuarioRepositorio clienteRepositorio;
	@Autowired
	private UsuarioSelecionador clienteSelecionador;
	@Autowired
	private AdicionadorLinkVenda adicionadorLink;

	@GetMapping("/venda/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		try {
			Venda venda = selecionador.selecionar(repositorio, id);
			if (venda == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				adicionadorLink.adicionarLink(venda);
				return new ResponseEntity<Venda>(venda, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/vendas")
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		
		if (vendas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(vendas);
			return new ResponseEntity<List<Venda>>(vendas, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
		try {
			if (venda.getId() == null) {
				repositorio.save(venda);
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
		try {
			Venda venda = repositorio.getById(atualizacao.getId());
			
			if (venda.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				VendaAtualizador atualizador = new VendaAtualizador();
				atualizador.atualizar(venda, atualizacao);
				repositorio.save(venda);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVenda(@RequestBody Venda exclusao) {
		try {
			Venda venda = repositorio.getById(exclusao.getId());
			Usuario cliente = clienteRepositorio.findByVendas(venda);
			
			if (venda.getId() == null || cliente.getId() == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				cliente.getVendas().remove(venda);
				repositorio.delete(venda);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Set<Venda>> pegarVendasCliente(@PathVariable long id){
		Usuario cliente = clienteSelecionador.selecionar(clienteRepositorio, id);
		
		if (cliente.getId() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			Set<Venda> vendas = cliente.getVendas();
			adicionadorLink.adicionarLink(new ArrayList<>(vendas));
			return new ResponseEntity<Set<Venda>>(vendas, HttpStatus.FOUND);
		}
	}
}
