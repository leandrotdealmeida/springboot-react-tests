package com.devjapa.minhasfinancas.exceptions;

public class ErroAutenticacao extends RuntimeException{	
	private static final long serialVersionUID = 1L;

	public ErroAutenticacao(String message) {
		super(message);
		
	}
}
