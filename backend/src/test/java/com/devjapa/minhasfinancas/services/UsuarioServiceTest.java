package com.devjapa.minhasfinancas.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjapa.minhasfinancas.domain.Usuario;
import com.devjapa.minhasfinancas.exceptions.RegraNegocioException;
import com.devjapa.minhasfinancas.repositories.UsuarioRepository;
import com.devjapa.minhasfinancas.services.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	UsuarioService service;

	@Mock
	UsuarioRepository repository;

	@BeforeEach
	public void setup() {
		service = new UsuarioServiceImpl(repository);
	}

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {
			// cenário
			String email = "email@email.com";
			String senha = "senha";

			Usuario usuario = Usuario.builder().email(email).senha(senha).build();
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

			// ação
			Usuario autenticar = service.autenticar(email, senha);

			// verificação
			org.assertj.core.api.Assertions.assertThat(autenticar).isNotNull();
		});

	}

	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// ação
			service.validarEmail("email@gmail.com");

		});

	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {

		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

			// ação
			service.validarEmail("email@email.com");

		});

	}
}
