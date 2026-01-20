package dev.elder.ecommerce.service;

import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    
    private CategoriaRepository repository;
    
    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }
    
    public List<Categoria> findAll() {
        return repository.findAll();
    }
    
    public Categoria findByName(String name) {
        return repository.findByNome(name).get();
    }

    
    
}
