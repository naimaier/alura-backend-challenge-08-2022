package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

public class ReceitaDto {

	private Long id;
	@NotEmpty(message="A descrição deve ser informada")
	private String descricao;
	@NotNull(message="Um valor deve ser informado")
	private BigDecimal valor;
	@NotNull(message="A data deve ser informada")
	@JsonFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	

	public ReceitaDto() {
	}
	
	public ReceitaDto(Receita receita) {
		this.id = receita.getId();
		this.descricao = receita.getDescricao();
		this.valor = receita.getValor();
		this.data = receita.getData();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public boolean isRepeated(ReceitaRepository receitaRepository) {
		LocalDate startDate = data
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = data
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return receitaRepository
				.findByDescricaoAndDataBetween(descricao, 
						startDate, 
						endDate)
				.isPresent();
	}
	
	public Receita toReceita() {
		Receita receita = new Receita();
		
		receita.setDescricao(descricao);
		receita.setValor(valor);
		receita.setData(data);
		
		return receita;
	}
	
	public static List<ReceitaDto> convert(List<Receita> receitas){
		
		return receitas
				.stream()
				.map(ReceitaDto::new)
				.collect(Collectors.toList());
	}
}
