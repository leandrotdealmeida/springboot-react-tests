package com.devjapa.minhasfinancas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjapa.minhasfinancas.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {	
	
	boolean existsByEmail(String email);

}
