package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import naimaier.finances.model.Despesa;
import naimaier.finances.repository.DespesaRepository;

public class DespesaUpdateDto {
	
	@NotEmpty(message="A descrição deve ser informada")
	private String descricao;
	@NotNull(message="Um valor deve ser informado")
	private BigDecimal valor;
	@NotEmpty(message="A data deve ser informada")
	private String data;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public DespesaUpdateDto() {
	}
	
	public DespesaUpdateDto(Despesa despesa) {
		this.descricao = despesa.getDescricao();
		this.valor = despesa.getValor();
		this.data = despesa.getData().format(formatter);
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public boolean isRepeated(Long id, DespesaRepository despesaRepository) {
		LocalDate startDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return despesaRepository
				.findByIdNotAndDescricaoAndDataBetween(id, 
						descricao, 
						startDate, 
						endDate)
				.isPresent();
	}

	public Despesa update(long id, DespesaRepository despesaRepository) {
		
		Despesa despesa = despesaRepository.getReferenceById(id);
		
		despesa.setDescricao(descricao);
		despesa.setValor(valor);
		
		LocalDate date = LocalDate.parse(data, formatter);
		despesa.setData(date);
		
		return despesa;
	}
}
