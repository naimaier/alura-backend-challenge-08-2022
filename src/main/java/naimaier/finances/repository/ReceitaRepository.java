package naimaier.finances.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naimaier.finances.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long>{

	public Optional<Receita> findByDescricaoAndDataBetween(String descricao, LocalDate startDate, LocalDate endDate);
	public Optional<Receita> findByIdNotAndDescricaoAndDataBetween(Long id, String descricao, LocalDate startDate, LocalDate endDate);
}
