package naimaier.finances.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import naimaier.finances.dto.DespesaDto;
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
					.body("Despesa repetida");
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

}
