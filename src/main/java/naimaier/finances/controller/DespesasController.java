package naimaier.finances.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import naimaier.finances.dto.DespesaDto;
import naimaier.finances.dto.DespesaUpdateDto;
import naimaier.finances.model.Despesa;
import naimaier.finances.repository.DespesaRepository;

@RestController
@RequestMapping("despesas")
public class DespesasController {
	
	@Autowired
	private DespesaRepository despesaRepository;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> save(@Valid @RequestBody DespesaDto despesaDto, 
			UriComponentsBuilder uriBuilder) {
		
		if (despesaDto.isRepeated(despesaRepository)) {
			return ResponseEntity
					.badRequest()
					.body("Despesa duplicada");
		}
		
		Despesa despesa = despesaDto.toDespesa();
		
		Despesa savedItem = despesaRepository.save(despesa);
		
		URI uri = uriBuilder
				.path("/despesas/{id}")
				.buildAndExpand(savedItem.getId())
				.toUri();
		
		return ResponseEntity
				.created(uri)
				.body(new DespesaDto(savedItem));
	}
	
	@GetMapping
	public List<DespesaDto> readAll() {
		
		List<Despesa> despesas = despesaRepository.findAll();
		
		return DespesaDto.convert(despesas);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DespesaDto> readOne(@PathVariable Long id) {
		
		Optional<Despesa> despesa = despesaRepository.findById(id);
		
		if (!despesa.isPresent()) {
			return ResponseEntity
					.notFound()
					.build();
		}
		
		return ResponseEntity
				.ok(new DespesaDto(despesa.get()));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> update(@PathVariable Long id,
			@Valid @RequestBody DespesaUpdateDto despesaUpdateDto) {
		
		Optional<Despesa> despesa = despesaRepository.findById(id);
		
		if (!despesa.isPresent()) {
			return ResponseEntity
					.notFound()
					.build();
		}
		
		if (despesaUpdateDto.isRepeated(id, despesaRepository)) {
			return ResponseEntity
					.badRequest()
					.body("Despesa duplicada");
		}
		
		Despesa updatedItem = despesaUpdateDto.update(id, despesaRepository);
		
		return ResponseEntity
				.ok(new DespesaDto(updatedItem));
	}

}
