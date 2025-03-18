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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import ex.model.Cliente;
import ex.model.repository.ClienteRepository;
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
	public ResponseEntity<Page<ClienteFormRequest>> getLista(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "4") int size,
	    @RequestParam(required = false) String nome) {

	    Pageable pageable = PageRequest.of(page, size);
	    
	    Page<ClienteFormRequest> clientesPage;
	    
	    if (nome != null && !nome.isEmpty()) {
	        // Usa a busca filtrada
	        clientesPage = repository.findByNomeContaining(nome, pageable)
	            .map(ClienteFormRequest::fromModel);
	    } else {
	        // Busca geral
	        clientesPage = repository.findAll(pageable)
	            .map(ClienteFormRequest::fromModel);
	    }
	    
	    return ResponseEntity.ok(clientesPage);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
	    return repository.findById(id)
	        .map(cliente -> ResponseEntity.ok(cliente))
	        .orElse(ResponseEntity.notFound().build());
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
