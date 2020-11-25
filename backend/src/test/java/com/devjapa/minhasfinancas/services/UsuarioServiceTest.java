package com.devjapa.minhasfinancas.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjapa.minhasfinancas.domain.Usuario;
import com.devjapa.minhasfinancas.exceptions.RegraNegocioException;
import com.devjapa.minhasfinancas.repositories.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;

	@Autowired
	UsuarioRepository repository;

	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			repository.deleteAll();

			// ação
			service.validarEmail("email@gmail.com");

		});

	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {

		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenário
			Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
			repository.save(usuario);

			// ação
			service.validarEmail("email@email.com");

		});

	}
}
