package com.pedrodev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedrodev.enums.StatusEstacionamento;
import com.pedrodev.models.Estacionamento;
import com.pedrodev.services.EstacionamentoService;
import com.pedrodev.utils.TempoUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/estacionamento")
public class EstacionamentoController {
	
	@Autowired
	private EstacionamentoService estacionamentoService;
	
	@GetMapping(path = "/")
	public String teste() {
		return "Teste";
	}
	
	@GetMapping(path="/listarEstacionados")
	public List<Estacionamento> listarEstacionados(){
		return estacionamentoService.listarTodosEstacionados();
	}
	
	@GetMapping(path="listarEntrada/{id}")
	public Estacionamento listarEntradaPorId(@PathVariable Integer id) {
		return estacionamentoService.buscarEstacionamentoPorId(id);
	}
	
	@PostMapping(path="/estacionar")
	public ResponseEntity<?> cadastrarEntrada(@RequestBody @Valid Estacionamento estacionamento){
		return ResponseEntity.ok(estacionamentoService.cadastrarEntrada(estacionamento));
	}
	
	@PutMapping(path="/finalizar/{id}")
	public ResponseEntity<?> finalizarEstacionamento(@PathVariable Integer id){
		return ResponseEntity.ok(estacionamentoService.finalizarEntrada(id));
	}
	
	@GetMapping("/filtrar")
	public List<Estacionamento> filtrar(
			@RequestParam(required = false) String placa,
			@RequestParam(required = false) String tempoDesejado,
			@RequestParam(required = false) String status){
		return estacionamentoService.filtrar(placa, tempoDesejado, status);
	}
	
	
	@GetMapping(path = "/validarPlaca")
	public boolean testaPlaca() {
		
		Estacionamento estacionamento = new Estacionamento();
		
		estacionamento.setPlaca("ABC1C32");
		
		return estacionamento.validarPlacaFormatoNovo();
	}
	
	@GetMapping(path = "/converterMinutos")
	public int converterMinutos() {
		return TempoUtils.converterParaMinutos("1h");
	}
	
	@GetMapping(path="/calcularCobranca")
	public double gerarCobranca() {
		return ((TempoUtils.converterParaMinutos("1h45") / 60) * 10);
	}

}
