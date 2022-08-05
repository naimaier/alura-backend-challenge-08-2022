package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.validation.constraints.NotNull;

import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

public class ReceitaUpdateDto {

	@NotNull
	private String descricao;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private String data;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public ReceitaUpdateDto() {
	}
	
	public ReceitaUpdateDto(Receita receita) {
		this.descricao = receita.getDescricao();
		this.valor = receita.getValor();
		this.data = receita.getData().format(formatter);
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
	public void String(String data) {
		this.data = data;
	}
	
	public boolean isRepeated(long id, ReceitaRepository receitaRepository) {
		
		LocalDate startDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = LocalDate.parse(data, formatter)
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
		
		LocalDate date = LocalDate.parse(data, formatter);
		receita.setData(date);
		
		return receita;
	}
	
}
