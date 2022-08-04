package naimaier.finances.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import naimaier.finances.dto.ReceitaDto;
import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

@RestController
@RequestMapping("receitas")
public class ReceitasController {
	
	@Autowired
	private ReceitaRepository receitaRepository;

	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody ReceitaDto receitaDto, 
			UriComponentsBuilder uriBuilder) {
		
		Receita receita = receitaDto.toReceita();
		
		if (isRepeated(receita)) {
			return ResponseEntity
					.badRequest()
					.body("Receita duplicada");
		}
		
		Receita savedItem = receitaRepository.save(receita);
		
		URI uri = uriBuilder
				.path("/receitas/{id}")
				.buildAndExpand(savedItem.getId())
				.toUri();
		
		return ResponseEntity
				.created(uri)
				.body(new ReceitaDto(savedItem));
	}
	
	
	private boolean isRepeated(Receita receita) {
		LocalDate startDate = receita.getData()
				.with(TemporalAdjusters.firstDayOfMonth());
		
		LocalDate endDate = receita.getData()
				.with(TemporalAdjusters.lastDayOfMonth());
		
		return receitaRepository
				.findByDescricaoAndDataBetween(receita.getDescricao(), 
						startDate, 
						endDate)
				.isPresent();
	}
	
	
	@GetMapping
	public List<ReceitaDto> readAll(){
		List<Receita> receitas = receitaRepository.findAll();
		
		return ReceitaDto.convert(receitas);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReceitaDto> readOne(@PathVariable Long id){
		Optional<Receita> receita = receitaRepository.findById(id);
		
		if (!receita.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new ReceitaDto(receita.get()));
	}
}
