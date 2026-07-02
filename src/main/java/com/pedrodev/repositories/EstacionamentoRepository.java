package com.pedrodev.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pedrodev.enums.StatusEstacionamento;
import com.pedrodev.models.Estacionamento;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Integer> {

	public Estacionamento findByPlacaAndStatus(String placa, StatusEstacionamento status);
	
	public List<Estacionamento> findAllByStatus(StatusEstacionamento status);
	
	@Query("SELECT e FROM Estacionamento e ORDER BY e.horarioEntrada desc")
	public List<Estacionamento> findAllOrderByHorarioEntrada();
	
	@Query("SELECT e FROM Estacionamento e " + 
			"WHERE (:placa IS NULL OR e.placa = :placa)" +
			" AND  (:tempoDesejado IS NULL OR e.tempoDesejado = :tempoDesejado) "+
			" AND (:status IS NULL OR e.status = :status)")
	List<Estacionamento> filtrar(
			@Param("placa") String placa, 
			@Param("tempoDesejado") String tempoDesejado,
			@Param("status") StatusEstacionamento status);

}
