package com.devjapa.minhasfinancas.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjapa.minhasfinancas.domain.Usuario;
import com.devjapa.minhasfinancas.exceptions.ErroAutenticacao;
import com.devjapa.minhasfinancas.exceptions.RegraNegocioException;
import com.devjapa.minhasfinancas.repositories.UsuarioRepository;
import com.devjapa.minhasfinancas.services.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test
	public void deveSalvarUmUsuario() {
		// Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1L).nome("nome").email("email@email.com").senha("senha").build();

		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		// ação
		Usuario usuarioSalvo = service.salvarUsuario(usuario);

		// Verificação
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");

	}

	@Test
	public void naodeveSalvarUmUsuarioComEmailJaCadastrado() {

		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenario
			String email = "email@email.com";
			Usuario usuario = Usuario.builder().email(email).build();
			Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

			// ação
			service.salvarUsuario(usuario);

			// verificação
			Mockito.verify(repository, Mockito.never()).save(usuario);
		});
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
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {

		// cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// ação
		Throwable exception = org.assertj.core.api.Assertions
				.catchThrowable(() -> service.autenticar("email@email.com", "senha"));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Usuario não existe para o email informado.");

	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {

		// cenário
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// ação
		Throwable exception = org.assertj.core.api.Assertions
				.catchThrowable(() -> service.autenticar("email@email.com", "123"));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Senha inválida.");

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
