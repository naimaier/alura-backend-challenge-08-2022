package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

public class ReceitaDto {

	private Long id;
	@NotNull
	private String descricao;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private String data;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public ReceitaDto() {
	}
	
	public ReceitaDto(Receita receita) {
		this.id = receita.getId();
		this.descricao = receita.getDescricao();
		this.valor = receita.getValor();
		this.data = receita.getData().format(formatter);
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
	public String getData() {
		return data;
	}
	public void String(String data) {
		this.data = data;
	}
	
	public boolean isRepeated(ReceitaRepository receitaRepository) {
		LocalDate startDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = LocalDate.parse(data, formatter)
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
		
		LocalDate date = LocalDate.parse(data, formatter);
		receita.setData(date);
		
		return receita;
	}
	
	public static List<ReceitaDto> convert(List<Receita> receitas){
		
		return receitas
				.stream()
				.map(ReceitaDto::new)
				.collect(Collectors.toList());
	}
}
