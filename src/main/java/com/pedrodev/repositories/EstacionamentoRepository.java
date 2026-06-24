package com.pedrodev.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedrodev.enums.StatusEstacionamento;
import com.pedrodev.models.Estacionamento;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Integer> {

	public Estacionamento findByPlacaAndStatus(String placa, StatusEstacionamento status);
	
	public List<Estacionamento> findAllByStatus(StatusEstacionamento status);
}
