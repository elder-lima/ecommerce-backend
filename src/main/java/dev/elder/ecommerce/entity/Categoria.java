package dev.elder.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoria_id;

    @Column(nullable = false, length = 100)
    private String nome;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Produto> produtos = new HashSet<>();

    public Categoria() {
    }

    public Categoria(UUID categoria_id, String nome) {
        this.categoria_id = categoria_id;
        this.nome = nome;
    }

    public UUID getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(UUID categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
