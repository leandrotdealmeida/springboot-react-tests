package com.devjapa.minhasfinancas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjapa.minhasfinancas.domain.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
