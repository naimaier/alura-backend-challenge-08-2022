package naimaier.finances.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naimaier.finances.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long>{

	public Optional<Despesa> findByDescricaoAndDataBetween(String descricao, LocalDate startDate, LocalDate endDate);
}
