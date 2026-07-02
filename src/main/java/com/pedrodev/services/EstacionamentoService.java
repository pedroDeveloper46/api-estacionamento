package com.pedrodev.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedrodev.enums.StatusEstacionamento;
import com.pedrodev.exceptions.ErroNegocioException;
import com.pedrodev.models.Estacionamento;
import com.pedrodev.repositories.EstacionamentoRepository;
import com.pedrodev.utils.TempoUtils;

@Service
public class EstacionamentoService {
	
	@Autowired
	private EstacionamentoRepository estacionamentoRepository;
	
	public List<Estacionamento> listarTodosEstacionados(){
		return estacionamentoRepository.findAllOrderByHorarioEntrada();
	}
	
	public List<Estacionamento> filtrar(String placa, String tempoDesejado, String status){
		
		StatusEstacionamento statusEstacionamento = status != null ? StatusEstacionamento.valueOf(status) : null;
		
		List<Estacionamento> lista = estacionamentoRepository.filtrar(placa, tempoDesejado, statusEstacionamento);
		
		if (lista.isEmpty() || lista == null) {
			throw new ErroNegocioException("Sem resultado");
		}
		
		return lista;
	}
	
	public Estacionamento cadastrarEntrada(Estacionamento estacionamento) {
		
		if(!estacionamento.validarPlacaFormatoAntigo() && !estacionamento.validarPlacaFormatoNovo()) {
			throw new ErroNegocioException("A placa é inválida");
		}
		
		if(buscarEntradaPelaPlaca(estacionamento.getPlaca(), StatusEstacionamento.ATIVO)) {
			throw new ErroNegocioException("O carro já está estacionado");
		}
		
		
		
		ZoneId zoneBr = ZoneId.of("America/Sao_Paulo");
		
		estacionamento.setHorarioEntrada(LocalDateTime.now(zoneBr).truncatedTo(ChronoUnit.SECONDS));
		
		int minutosHorarioPrevisto = TempoUtils.converterParaMinutos(estacionamento.getTempoDesejado());
		
		estacionamento.setPrevistoSaida(estacionamento.getHorarioEntrada().plusMinutes(minutosHorarioPrevisto));
		
		estacionamento.setValorCobranca(gerarValorCobranca(minutosHorarioPrevisto));
				
		estacionamento.setStatus(StatusEstacionamento.ATIVO);
		
		return estacionamentoRepository.save(estacionamento);
		
	}
	
	public Estacionamento finalizarEntrada(Integer id) {
		
		Estacionamento estacionamento = buscarEstacionamentoPorId(id);
		
		if (estacionamento.getStatus() != StatusEstacionamento.ATIVO) {
			throw new ErroNegocioException("O carro "+estacionamento.getPlaca()+" já saiu do estacionamento");
		}
		
		estacionamento.setSaidaReal(horaAtual());
		
		estacionamento.setValorCobranca(definirValorFinal(estacionamento.getHorarioEntrada(), estacionamento.getSaidaReal()));
		
		estacionamento.setStatus(StatusEstacionamento.FINALIZADO);
		
		estacionamento.setId(id);
		
		return estacionamentoRepository.save(estacionamento);
		
		
		
	}
	
	public Estacionamento buscarEstacionamentoPorId(Integer id) {
		
		return estacionamentoRepository.findById(id).
				orElseThrow(() -> new ErroNegocioException("Estacionamento não encontrado"));
		
	}
	
	private boolean buscarEntradaPelaPlaca(String placa, StatusEstacionamento status) {
		
		if (estacionamentoRepository.findByPlacaAndStatus(placa, status) != null) {
			return true;
		}
		
		return false;
		
	}
	
	private BigDecimal gerarValorCobranca(int minutos) {
		return new BigDecimal(Math.ceil(minutos / 30.0)  * 5);
	}
	
	public BigDecimal definirValorFinal(LocalDateTime horarioEntrada, LocalDateTime horarioSaida) {
		
		BigDecimal valor = new BigDecimal(0.0);
		
		int minutosTotais = (int) Duration.between(horarioEntrada, horarioSaida)
				.toMinutes();
		
		if(minutosTotais > 15) {
			valor = gerarValorCobranca(minutosTotais);
		}
		
		
		return valor;
		
	}
	
	private LocalDateTime horaAtual() {
		
		ZoneId zoneBr = ZoneId.of("America/Sao_Paulo");
		
		return LocalDateTime.now(zoneBr).truncatedTo(ChronoUnit.SECONDS);
		
	}

}
