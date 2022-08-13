package naimaier.finances.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import naimaier.finances.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long>{

	public Optional<Receita> findByDescricaoAndDataBetween(String descricao, LocalDate startDate, LocalDate endDate);
	public Optional<Receita> findByIdNotAndDescricaoAndDataBetween(Long id, String descricao, LocalDate startDate, LocalDate endDate);
	public List<Receita> findByDescricaoContaining(String descricao);
	public List<Receita> findByDataBetween(LocalDate startDate, LocalDate endDate);
	@Query("SELECT SUM(r.valor) FROM Receita r WHERE r.data >= :startDate AND r.data <= :endDate")
	public Optional<BigDecimal> sumBetweenData(LocalDate startDate, LocalDate endDate);
}
