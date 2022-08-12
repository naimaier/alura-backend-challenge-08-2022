package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

public class ReceitaUpdateDto {

	@NotEmpty(message="A descrição deve ser informada")
	private String descricao;
	@NotNull(message="Um valor deve ser informado")
	private BigDecimal valor;
	@NotNull(message="A data deve ser informada")
	@JsonFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	
	
	public ReceitaUpdateDto() {
	}
	
	public ReceitaUpdateDto(Receita receita) {
		this.descricao = receita.getDescricao();
		this.valor = receita.getValor();
		this.data = receita.getData();
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public boolean isRepeated(long id, ReceitaRepository receitaRepository) {
		
		LocalDate startDate = data
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = data
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return receitaRepository
				.findByIdNotAndDescricaoAndDataBetween(id, descricao, 
						startDate, 
						endDate)
				.isPresent();
	}
	
	public Receita update(long id, ReceitaRepository receitaRepository) {
		
		Receita receita = receitaRepository.getReferenceById(id);
		
		receita.setDescricao(descricao);
		receita.setValor(valor);
		receita.setData(data);
		
		return receita;
	}
	
}
