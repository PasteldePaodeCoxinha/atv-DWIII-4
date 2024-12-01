package com.autobots.automanager.modelo.adicionadoresLink;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneController;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

	@Override
	public void adicionarLink(List<Telefone> lista) {
		for (Telefone telefone : lista) {
			long id = telefone.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(TelefoneController.class)
							.obterTelefone(id))
					.withSelfRel();
			telefone.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Telefone objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(TelefoneController.class)
						.obterTelefones())
				.withRel("telefones");
		objeto.add(linkProprio);
	}
}
