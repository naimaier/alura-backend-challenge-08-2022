package naimaier.finances.controller;

import java.net.URI;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import naimaier.finances.dto.ReceitaDto;
import naimaier.finances.dto.ReceitaUpdateDto;
import naimaier.finances.model.Receita;
import naimaier.finances.repository.ReceitaRepository;

@RestController
@RequestMapping("receitas")
public class ReceitasController {
	
	@Autowired
	private ReceitaRepository receitaRepository;

	@PostMapping
	@Transactional
	public ResponseEntity<?> save(@Valid @RequestBody ReceitaDto receitaDto, 
			UriComponentsBuilder uriBuilder) {
		
		if (receitaDto.isRepeated(receitaRepository)) {
			return ResponseEntity
					.badRequest()
					.body("Receita duplicada");
		}
		
		Receita receita = receitaDto.toReceita();
		Receita savedItem = receitaRepository.save(receita);
		
		URI uri = uriBuilder
				.path("/receitas/{id}")
				.buildAndExpand(savedItem.getId())
				.toUri();
		
		return ResponseEntity
				.created(uri)
				.body(new ReceitaDto(savedItem));
	}
	
	
	@GetMapping
	public List<ReceitaDto> readAll(String descricao){
		
		List<Receita> receitas;
		
		if (descricao == null) {
			receitas = receitaRepository.findAll();						
		} else {
			receitas = receitaRepository.findByDescricaoContaining(descricao);
		}
		
		return ReceitaDto.convert(receitas);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ReceitaDto> readOne(@PathVariable Long id){
		Optional<Receita> receita = receitaRepository.findById(id);
		
		if (!receita.isPresent()) {
			return ResponseEntity
					.notFound()
					.build();
		}
		
		return ResponseEntity.ok(new ReceitaDto(receita.get()));
	}
	
	
	@GetMapping("/{ano}/{mes}")
	public ResponseEntity<List<ReceitaDto>> readByMonth(@PathVariable Integer ano, @PathVariable Integer mes){
		
		LocalDate startDate;
		
		try {
			startDate = LocalDate.of(ano, mes, 1);			
		} catch (DateTimeException e) {
			return ResponseEntity
					.badRequest()
					.build();
		}
		
		LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
		
		List<Receita> receitas = receitaRepository.findByDataBetween(startDate, endDate);
		
		return ResponseEntity.ok(ReceitaDto.convert(receitas));
	}
	
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> update(@PathVariable Long id, 
			@Valid @RequestBody ReceitaUpdateDto receitaUpdateDto){
		Optional<Receita> receita = receitaRepository.findById(id);
		
		if (!receita.isPresent()) {
			return ResponseEntity
					.notFound()
					.build();
		}
		
		
		if (receitaUpdateDto.isRepeated(id, receitaRepository)) {
			return ResponseEntity
					.badRequest()
					.body("Receita duplicada");
		}
		
		Receita updatedItem = receitaUpdateDto.update(id, receitaRepository);
		
		return ResponseEntity.ok(new ReceitaDto(updatedItem));
	}
	
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Optional<Receita> receita = receitaRepository.findById(id);
		
		if (!receita.isPresent()) {			
			return ResponseEntity
					.notFound()
					.build();
		}
		
		receitaRepository.deleteById(id);
		
		return ResponseEntity
				.ok()
				.build();
	}
}
