package naimaier.finances.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import naimaier.finances.dto.CategoriaProjection;
import naimaier.finances.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long>{

	public Optional<Despesa> findByDescricaoAndDataBetween(String descricao, LocalDate startDate, LocalDate endDate);
	public Optional<Despesa> findByIdNotAndDescricaoAndDataBetween(Long id, String descricao, LocalDate startDate, LocalDate endDate);
	public List<Despesa> findByDescricaoContaining(String descricao);
	public List<Despesa> findByDataBetween(LocalDate startDate, LocalDate endDate);
	@Query("SELECT SUM(d.valor) FROM Despesa d WHERE d.data > :startDate AND d.data < :endDate")
	public Optional<BigDecimal> sumBetweenData(LocalDate startDate, LocalDate endDate);
	@Query("SELECT d.categoria AS categoria, SUM(d.valor) AS total FROM Despesa d WHERE d.data > :startDate AND d.data < :endDate GROUP BY d.categoria")
	public List<CategoriaProjection> countTotalDespesaByCategoryBetweenData(LocalDate startDate, LocalDate endDate);
}
