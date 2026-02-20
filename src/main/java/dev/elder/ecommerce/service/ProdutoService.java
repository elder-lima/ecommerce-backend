package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.ProdutoRequest;
import dev.elder.ecommerce.dto.request.ProdutoUpdateRequest;
import dev.elder.ecommerce.dto.response.CategoriaResponse;
import dev.elder.ecommerce.dto.response.ProdutoResponse;
import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.entity.Produto;
import dev.elder.ecommerce.repository.CategoriaRepository;
import dev.elder.ecommerce.repository.ProdutoRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> findAll() {
        List<ProdutoResponse> dto = produtoRepository.findAll().stream().map(x -> new ProdutoResponse(x.getProduto_id(), x.getNome(), x.getDescricao(), x.getPreco(), x.getImagem(), x.getCategorias().stream().map(c -> new CategoriaResponse(c.getNome())).collect(Collectors.toSet()))).toList();
        return dto;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse findById(UUID id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return toResponse(produto);
    }


    @Transactional
    public ProdutoResponse insert(ProdutoRequest request) {
        Set<Categoria> categorias = categoriaRepository.findByNomeIn(request.categorias());

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

        return toResponse(produto);
    }

    @Transactional
    public void delete(UUID id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        produtoRepository.deleteById(id);
    }

    @Transactional
    public ProdutoResponse update(UUID id, ProdutoUpdateRequest dto) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        applyUpdates(produto, dto);
        produtoRepository.save(produto);
        return toResponse(produto);
    }

    public ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getProduto_id(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getImagem(),
                produto.getCategorias().stream().map(categoria -> new CategoriaResponse(categoria.getNome())).collect(Collectors.toSet())
        );
    }

    public void applyUpdates(Produto produto, ProdutoUpdateRequest dto) {
        if (dto.nome() != null) {
            produto.setNome(dto.nome());
        }
        if (dto.descricao() != null) {
            produto.setDescricao(dto.descricao());
        }
        if (dto.preco() != null) {
            produto.setPreco(dto.preco());
        }
        if (dto.imagem() != null) {
            produto.setImagem(dto.imagem());
        }
        if (dto.categorias() != null) {
            Set<Categoria> categorias = dto.categorias().stream().map(categoriaDTO ->
                categoriaRepository.findByNome(categoriaDTO.nome()).orElseThrow(() -> new ResourceNotFoundException("Uma ou mais categorias não existem"))
            ).collect(Collectors.toSet());

            produto.setCategorias(categorias);
        }
    }
}
