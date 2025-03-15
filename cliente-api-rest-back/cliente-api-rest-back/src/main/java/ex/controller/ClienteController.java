package ex.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ex.model.Cliente;
import ex.model.repository.ClienteRepository;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin
public class ClienteController {
	
	@Autowired
	private ClienteRepository repository;
	
	@PostMapping
	public ResponseEntity<ClienteFormRequest> salvar(@RequestBody ClienteFormRequest request){

		Cliente cliente = request.toModel();
	    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\\nRecebendo cliente com CPF: " + cliente.toString());

		
		repository.save(cliente);
		
		System.out.println(cliente);
		
		return ResponseEntity.ok(ClienteFormRequest.fromModel(cliente));
	}
	
	@GetMapping
	public List<ClienteFormRequest> getLista(){
		return repository.findAll()
				.stream()
				.map(ClienteFormRequest::fromModel)
				.collect(Collectors.toList());
		
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public void atualizaar(@PathVariable Long id, @RequestBody Cliente cliente) {
		cliente.setId(id);
		repository.save(cliente);
	}
	
}
