package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import naimaier.finances.model.Categoria;
import naimaier.finances.model.Despesa;
import naimaier.finances.repository.DespesaRepository;

public class DespesaDto {
	
	private Long id;
	@NotEmpty(message="A descrição deve ser informada")
	private String descricao;
	@NotNull(message="Um valor deve ser informado")
	private BigDecimal valor;
	@NotNull(message="A data deve ser informada")
	@JsonFormat(pattern="dd/MM/yyyy")
	private LocalDate data;
	private String categoria;
	
	
	public DespesaDto() {
	}
	
	public DespesaDto(Despesa despesa) {
		this.id = despesa.getId();
		this.descricao = despesa.getDescricao();
		this.valor = despesa.getValor();
		this.data = despesa.getData();
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
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isRepeated(DespesaRepository despesaRepository) {
		LocalDate startDate = data
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = data
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return despesaRepository
				.findByDescricaoAndDataBetween(descricao, startDate, endDate)
				.isPresent();
	}

	public Despesa toDespesa() {
		Despesa despesa = new Despesa();
		
		despesa.setDescricao(descricao);
		despesa.setValor(valor);
		despesa.setData(data);
		
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
