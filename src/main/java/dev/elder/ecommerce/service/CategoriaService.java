package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.CategoriaRequest;
import dev.elder.ecommerce.dto.response.CategoriaResponse;
import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.repository.CategoriaRepository;
import dev.elder.ecommerce.service.exceptions.ConflictException;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {
    
    private CategoriaRepository repository;
    
    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }
    
    public List<CategoriaResponse> findAll() {
        return repository.findAll().stream().map(x -> new CategoriaResponse(x.getNome())).toList();
    }
    
    public CategoriaResponse findByName(String name) {
        Categoria categoria = repository.findByNome(name).orElseThrow(() -> new ResourceNotFoundException(name));
        return new CategoriaResponse(categoria.getNome());
    }

    public CategoriaResponse insert(CategoriaRequest obj) {
        if (repository.existsByNome(obj.nome())) {
            throw new ConflictException("Categoria "+ obj.nome() +" já existe.");
        }
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome(obj.nome());
        repository.save(novaCategoria);
        CategoriaResponse dto = new CategoriaResponse(obj.nome());
        return dto;
    }

    public void delete(String nome) {
        Categoria categoria = repository.findByNome(nome).orElseThrow(() -> new ResourceNotFoundException(nome));
        repository.delete(categoria);
    }
    
}
