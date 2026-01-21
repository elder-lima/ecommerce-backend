package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.ProdutoRequest;
import dev.elder.ecommerce.dto.response.CategoriaResponse;
import dev.elder.ecommerce.dto.response.ProdutoResponse;
import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.entity.Produto;
import dev.elder.ecommerce.repository.CategoriaRepository;
import dev.elder.ecommerce.repository.ProdutoRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;

    private CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> findAll() {
        List<ProdutoResponse> dto = produtoRepository.findAll().stream().map(x -> new ProdutoResponse(x.getProduto_id(), x.getNome(), x.getDescricao(), x.getPreco(), x.getImagem(), x.getCategorias().stream().map(c -> new CategoriaResponse(c.getNome())).toList())).toList();
        return dto;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse findById(UUID id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ProdutoResponse(
                produto.getProduto_id(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getImagem(),
                produto.getCategorias().stream().map(c -> new CategoriaResponse(c.getNome())).toList()
        );
    }


    @Transactional
    public ProdutoResponse insert(@RequestBody ProdutoRequest request) {
        List<Categoria> categorias = categoriaRepository.findByNomeIn(request.categorias());

        if (categorias.size() != request.categorias().size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não existem");
        }

        Produto produto = new Produto();
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setImagem(request.imagem());
        produto.getCategorias().addAll(categorias);
        produtoRepository.save(produto);

        return new ProdutoResponse(
                produto.getProduto_id(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getImagem(),
                produto.getCategorias().stream().map(x -> new CategoriaResponse(x.getNome())).toList()
        );
    }

    @Transactional
    public void delete(UUID id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        produtoRepository.delete(produto);
    }

}
