package com.devjapa.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devjapa.minhasfinancas.domain.Usuario;

@SpringBootTest
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistnciaDeUmEmail() {
		// cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		repository.save(usuario);		
		
		// ação / execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result).isTrue();
	}
}
