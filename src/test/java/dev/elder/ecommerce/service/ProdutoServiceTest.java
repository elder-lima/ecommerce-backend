package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.CategoriaRequest;
import dev.elder.ecommerce.dto.request.ProdutoRequest;
import dev.elder.ecommerce.dto.request.ProdutoUpdateRequest;
import dev.elder.ecommerce.dto.response.ProdutoResponse;
import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.entity.Produto;
import dev.elder.ecommerce.repository.CategoriaRepository;
import dev.elder.ecommerce.repository.ProdutoRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    @InjectMocks
    ProdutoService produtoService;

    @Test
    @DisplayName("Retorno de Produto com sucesso")
    void deveRetornarProdutoQuandoIdExistir() {

        // ARRANGE
        UUID id = UUID.randomUUID();

        Categoria categoria = new Categoria();
        categoria.setNome("Eletronicos");

        Produto produto = new Produto();
        produto.setProduto_id(id);
        produto.setNome("Notebook");
        produto.setDescricao("Notebook Azus");
        produto.setPreco(BigDecimal.valueOf(2000));
        produto.setImagem("UrlNotebook");
        produto.setCategorias(Set.of(categoria));

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // ACT
        ProdutoResponse result = produtoService.findById(id);

        // ASSERT
        assertNotNull(result);
        assertEquals("Notebook", result.nome());
        assertEquals(1, result.categorias().size());
        assertEquals(BigDecimal.valueOf(2000), result.preco());
        assertEquals("Notebook Azus", result.descricao());
        assertEquals("UrlNotebook", result.imagem());

        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Produto que não Existe")
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        // ARRANGE
        UUID id = UUID.randomUUID();

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> produtoService.findById(id));

        verify(produtoRepository).findById(id);
    }

    @Test
    @DisplayName("Criando produto com Sucesso")
    void deveInserirProduto() {
        // ARRANGE
        CategoriaRequest categoria1 = new CategoriaRequest(
                "Eletronicos"
        );

        CategoriaRequest categoria2 = new CategoriaRequest(
                "Computadores"
        );

        ProdutoRequest request = new ProdutoRequest(
                "Notebook",
                "Notebook Asus",
                BigDecimal.valueOf(2000),
                "UrlNotebook",
                Set.of(categoria1, categoria2)
        );

        Set<String> nomes = request.categorias().stream().map(c -> c.nome()).collect(Collectors.toSet());

        Categoria cat1 = new Categoria();
        cat1.setNome("Eletronicos");

        Categoria cat2 = new Categoria();
        cat2.setNome("Computadores");

        Set<Categoria> categorias = Set.of(cat1, cat2);

        when(categoriaRepository.findByNomeIn(nomes))
                .thenReturn(categorias);

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);

        // ACT
        ProdutoResponse result = produtoService.insert(request);

        // ASSERT
        assertNotNull(result);
        assertEquals("Notebook", result.nome());
        assertEquals("Notebook Asus", result.descricao());
        assertEquals(BigDecimal.valueOf(2000), result.preco());
        assertEquals("UrlNotebook", result.imagem());
        assertEquals(2, result.categorias().size());

        // Captura o objeto salvo
        verify(produtoRepository).save(captor.capture());

        Produto produtoSalvo = captor.getValue();

        // Verifica se o mapeamento para entidade foi correto
        assertEquals("Notebook", produtoSalvo.getNome());
        assertEquals("Notebook Asus", produtoSalvo.getDescricao());
        assertEquals(BigDecimal.valueOf(2000), produtoSalvo.getPreco());
        assertEquals("UrlNotebook", produtoSalvo.getImagem());
        assertEquals(2, produtoSalvo.getCategorias().size());

        // Verifica chamadas
        verify(categoriaRepository).findByNomeIn(nomes);
        verifyNoMoreInteractions(produtoRepository, categoriaRepository);
    }

    @Test
    @DisplayName("Deve deletar Produto quando ele existe")
    void deleteQuandoProdutoExiste() {

        Produto produto = new Produto();
        produto.setProduto_id(UUID.randomUUID());

        when(produtoRepository.findById(produto.getProduto_id())).thenReturn(Optional.of(produto));

        produtoService.delete(produto.getProduto_id());

        verify(produtoRepository).findById(produto.getProduto_id());
        verify(produtoRepository).deleteById(produto.getProduto_id());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não existe")
    void deleteQuandoProdutoNaoExiste() {

        UUID id = UUID.randomUUID();

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.delete(id));

        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve Atualizar Produto com sucesso")
    void update() {
        Produto produto = new Produto();
        produto.setProduto_id(UUID.randomUUID());
        produto.setNome("Monitor");

        ProdutoUpdateRequest updateRequest = new ProdutoUpdateRequest("Monitor Gamer", null, null, null, null);

        when(produtoRepository.findById(produto.getProduto_id())).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoResponse produtoResponse = produtoService.update(produto.getProduto_id(), updateRequest);

        assertEquals("Monitor Gamer", produtoResponse.nome());
        verify(produtoRepository).save(any(Produto.class));
    }

}