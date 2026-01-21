package dev.elder.ecommerce.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID produto_id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 255)
    private String imagem;

    @ManyToMany
    @JoinTable(
            name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_fk"),
            inverseJoinColumns = @JoinColumn(name = "categoria_fk"))
    private Set<Categoria> categorias = new HashSet<>();

    public Produto() {
    }

    public Produto(UUID produto_id, String nome, String descricao, BigDecimal preco, String imagem) {
        this.produto_id = produto_id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
    }

    public UUID getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(UUID produto_id) {
        this.produto_id = produto_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(produto_id, produto.produto_id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(produto_id);
    }
}
