package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import naimaier.finances.model.Categoria;
import naimaier.finances.model.Despesa;
import naimaier.finances.repository.DespesaRepository;

public class DespesaDto {
	
	private Long id;
	@NotEmpty(message="A descrição deve ser informada")
	private String descricao;
	@NotNull(message="Um valor deve ser informado")
	private BigDecimal valor;
	@NotEmpty(message="A data deve ser informada")
	private String data;
	private String categoria;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public DespesaDto() {
	}
	
	public DespesaDto(Despesa despesa) {
		this.id = despesa.getId();
		this.descricao = despesa.getDescricao();
		this.valor = despesa.getValor();
		this.data = despesa.getData().format(formatter);
		this.categoria = despesa.getCategoria().getDescricao();
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
	public void setData(String data) {
		this.data = data;
	}
	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isRepeated(DespesaRepository despesaRepository) {
		LocalDate startDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = LocalDate.parse(data, formatter)
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return despesaRepository
				.findByDescricaoAndDataBetween(descricao, startDate, endDate)
				.isPresent();
	}

	public Despesa toDespesa() {
		Despesa despesa = new Despesa();
		
		despesa.setDescricao(descricao);
		despesa.setValor(valor);
		
		LocalDate date = LocalDate.parse(data, formatter);
		despesa.setData(date);
		
		Categoria inputCategory = Categoria.OUTRAS;
		
		if (categoria != null) {
			try {
				inputCategory = Categoria.valueOf(categoria.toUpperCase());				
			} catch (IllegalArgumentException e) {
				
			}
		}
		
		despesa.setCategoria(inputCategory);
		
		return despesa;
	}
	
	public static List<DespesaDto> convert(List<Despesa> despesas) {
		return despesas
				.stream()
				.map(DespesaDto::new)
				.collect(Collectors.toList());
	}
}
