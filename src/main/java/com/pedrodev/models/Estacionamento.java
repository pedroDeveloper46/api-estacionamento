package com.pedrodev.models;

import java.time.Duration;
import java.time.LocalDateTime;

import com.pedrodev.enums.StatusEstacionamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estacionamento")
@Getter
@Setter
public class Estacionamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@NotBlank(message = "A placa do carro não pode ser vazia!")
	private String placa;
	
	@NotNull
	@NotBlank(message = "É preciso informar o horário desejado para estacionar!")
	private String tempoDesejado;
	
	@Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime horarioEntrada;

	@Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime previstoSaida;
	
	@Column(nullable = true, columnDefinition = "DATETIME(0)")
	private LocalDateTime saidaReal;
	

	private Double valorCobranca;
	
	
	@Enumerated(EnumType.STRING)
	private StatusEstacionamento status;
	
	
	public boolean validarPlacaFormatoAntigo() {
		return this.placa.matches("[A-Z]{3}[0-9]{4}");
	}
	
	public boolean validarPlacaFormatoNovo() {
		return this.placa.matches("[A-Z]{3}[0-9][A-Z][0-9]{2}");
	}
	
	public void arredondaCobranca() {
		this.valorCobranca = Math.round(this.valorCobranca * 100.0) / 100.0;
	}
	
	
	
	public int diferencaHoras() {
		
		Duration diferenca = Duration.between(this.horarioEntrada, this.saidaReal);
		
		int horas = (int) (diferenca.toHours() * 60);
		
		int minutos = (int) (diferenca.toMinutes());
		
		return (horas + minutos);
			
	}
	 

}
