package naimaier.finances.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naimaier.finances.dto.ResumoDto;
import naimaier.finances.repository.DespesaRepository;
import naimaier.finances.repository.ReceitaRepository;

@RestController
@RequestMapping("resumo")
public class ResumoController {
	
	@Autowired
	private ReceitaRepository receitaRepository;
	@Autowired
	private DespesaRepository despesaRepository;

	
	@GetMapping("/{ano}/{mes}")
	public ResponseEntity<?> summaryByMonth(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		LocalDate startDate;
		
		try {
			startDate = LocalDate.of(ano, mes, 1);
		} catch (DateTimeException e) {
			return ResponseEntity
					.badRequest()
					.build();
		}
		
		LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

		ResumoDto resumoDto = new ResumoDto(startDate, 
				endDate, 
				receitaRepository, 
				despesaRepository);
		
		return ResponseEntity.ok(resumoDto);
	}
}
