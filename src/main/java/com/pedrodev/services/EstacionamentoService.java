package com.pedrodev.services;

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
		return estacionamentoRepository.findAllByStatus(StatusEstacionamento.ATIVO);
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
		
		estacionamento.arredondaCobranca();
		
		estacionamento.setStatus(StatusEstacionamento.ATIVO);
		
		return estacionamentoRepository.save(estacionamento);
		
	}
	
	public Estacionamento finalizarEntrada(Integer id) {
		
		Estacionamento estacionamento = buscarEstacionamentoPorId(id);
		
		if (estacionamento.getStatus() != StatusEstacionamento.ATIVO) {
			throw new ErroNegocioException("O carro "+estacionamento.getPlaca()+" já saiu do estacionamento");
		}
		
		estacionamento.setSaidaReal(horaAtual());
		
		int minutosHorarioPrevisto = TempoUtils.converterParaMinutos(estacionamento.getTempoDesejado());
		
		int minutosSaida = estacionamento.diferencaHoras();
		
		estacionamento.setValorCobranca(definirValorFinal(minutosHorarioPrevisto, minutosSaida));
		
		estacionamento.setStatus(StatusEstacionamento.FINALIZADO);
		
		estacionamento.setId(id);
		
		return estacionamentoRepository.save(estacionamento);
		
		//implementar um metodo que verifique se a quant de minutos previstos bate com os minutos reais
		  //1 - Se bater, não tem alteração no valor
		  //2 - Se houver pra cima, pegar o valor e adicionar a fração necessária
		  //3 - Se houver pra baixo, cobrar apenas uma hora (se o carro ficar acima de 15min)
		
		
	}
	
	private Estacionamento buscarEstacionamentoPorId(Integer id) {
		
		return estacionamentoRepository.findById(id).
				orElseThrow(() -> new ErroNegocioException("Estacionamento não encontrado"));
		
	}
	
	private boolean buscarEntradaPelaPlaca(String placa, StatusEstacionamento status) {
		
		if (estacionamentoRepository.findByPlacaAndStatus(placa, status) != null) {
			return true;
		}
		
		return false;
		
	}
	
	private double gerarValorCobranca(int minutos) {
		return (Math.ceil(minutos / 30.0)  * 5);
	}
	
	public Double definirValorFinal(int minutosSaidaPrevista, int minutosSaida) {
		
		double valor = 0.0;
		
		if((minutosSaidaPrevista == minutosSaida) || (minutosSaida > minutosSaidaPrevista) || minutosSaida > 15) {
			valor = gerarValorCobranca(minutosSaida);
		}
		
		return valor;
		
	}
	
	private LocalDateTime horaAtual() {
		
		ZoneId zoneBr = ZoneId.of("America/Sao_Paulo");
		
		return LocalDateTime.now(zoneBr).truncatedTo(ChronoUnit.SECONDS);
		
	}

}
