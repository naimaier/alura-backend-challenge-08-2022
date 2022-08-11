package naimaier.finances.dto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

public interface CategoriaProjection {

	@Value("#{target.categoria.descricao}")
	String getCategoria();
	BigDecimal getTotal();
}
