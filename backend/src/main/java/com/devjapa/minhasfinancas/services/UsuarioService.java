package com.devjapa.minhasfinancas.services;

import com.devjapa.minhasfinancas.domain.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

}
