package naimaier.finances.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naimaier.finances.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long>{

}
