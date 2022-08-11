package naimaier.finances.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import naimaier.finances.repository.DespesaRepository;
import naimaier.finances.repository.ReceitaRepository;

public class ResumoDto {

	private BigDecimal totalReceitas;
	private BigDecimal totalDespesas;
	private BigDecimal saldo;
	private List<CategoriaProjection> gastoTotalPorCategoria;
	
	public ResumoDto(LocalDate startDate, LocalDate endDate, 
			ReceitaRepository receitaRepository, DespesaRepository despesaRepository) {
		
		this.totalReceitas = receitaRepository.sumBetweenData(startDate, endDate).orElse(BigDecimal.ZERO);
		this.totalDespesas = despesaRepository.sumBetweenData(startDate, endDate).orElse(BigDecimal.ZERO);
		this.saldo = this.totalReceitas.subtract(this.totalDespesas);
		this.gastoTotalPorCategoria = despesaRepository.countTotalDespesaByCategoryBetweenData(startDate, endDate);
	}

	public BigDecimal getTotalReceitas() {
		return totalReceitas;
	}

	public BigDecimal getTotalDespesas() {
		return totalDespesas;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public List<CategoriaProjection> getGastoTotalPorCategoria() {
		return gastoTotalPorCategoria;
	}
	
}
