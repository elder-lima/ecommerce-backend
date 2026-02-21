package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.PedidoItemRequest;
import dev.elder.ecommerce.dto.request.PedidoRequest;
import dev.elder.ecommerce.dto.response.PedidoResponse;
import dev.elder.ecommerce.entity.*;
import dev.elder.ecommerce.entity.enums.StatusPedido;
import dev.elder.ecommerce.repository.PedidoRepository;
import dev.elder.ecommerce.repository.ProdutoRepository;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    PedidoService pedidoService;

    @Test
    @DisplayName("Deve criar Pedido com sucesso")
    void deveCriarPedidoComSucesso() {

        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(userId.toString());

        AuthUser authUser = new AuthUser();
        authUser.setAuth_user_id(userId);
        authUser.setEmail("elder@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setUserId(userId);
        usuario.setNome("Elder");
        usuario.setTelefone("99999");
        usuario.setAuthUser(authUser);

        Produto produto = new Produto();
        produto.setProduto_id(productId);
        produto.setNome("Monitor");
        produto.setPreco(BigDecimal.valueOf(1000));

        PedidoItemRequest pedidoItemRequest = new PedidoItemRequest(
                produto.getProduto_id(),
                2
        );

        PedidoRequest request = new PedidoRequest(
                List.of(pedidoItemRequest)
        );

        when(usuarioRepository.findByUserId(userId)).thenReturn(Optional.of(usuario));
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoResponse result = pedidoService.criarPedido(request, token);

        assertNotNull(result);
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, result.status());
        assertEquals(1, result.itens().size());
        assertEquals("Monitor", result.itens().get(0).nomeProduto());

        verify(usuarioRepository).findByUserId(userId);
        verify(produtoRepository).findById(productId);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuario não Existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {

        UUID userId = UUID.randomUUID();

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(userId.toString());

        PedidoRequest pedidoRequest = new PedidoRequest(List.of());

        when(usuarioRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.criarPedido(pedidoRequest, token));

        verify(usuarioRepository).findByUserId(userId);
        verifyNoInteractions(produtoRepository);
        verifyNoInteractions(pedidoRepository);

    }

    @Test
    @DisplayName("Deve Lançar Exceção quando Produto não Existe")
    void deveLancarExcecaoQuandoProdutoNaoExiste() {

        // ARRANGE
        UUID userId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setUserId(userId);

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(userId.toString());

        PedidoItemRequest pedidoItemRequest = new PedidoItemRequest(
                produtoId,
                2
        );

        PedidoRequest pedidoRequest = new PedidoRequest(List.of(pedidoItemRequest));

        when(usuarioRepository.findByUserId(userId)).thenReturn(Optional.of(usuario));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        // ACR & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.criarPedido(pedidoRequest, token));

        verify(usuarioRepository).findByUserId(userId);
        verify(produtoRepository).findById(produtoId);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    @DisplayName("Deve retorna Pedidos do Usuario")
    void deveRetornarPedidosDoUsuarioComSucesso() {

        UUID userId = UUID.randomUUID();

        AuthUser authUser = new AuthUser();
        authUser.setAuth_user_id(userId);
        authUser.setEmail("elder@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setUserId(userId);
        usuario.setNome("Elder");
        usuario.setAuthUser(authUser);

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(userId.toString());

        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Produto produto1 = new Produto();
        produto1.setProduto_id(productId1);
        produto1.setNome("Notebook");
        produto1.setPreco(BigDecimal.valueOf(2000));

        Produto produto2 = new Produto();
        produto2.setProduto_id(productId2);
        produto2.setNome("Monitor");
        produto2.setPreco(BigDecimal.valueOf(500));

        Pedido pedido = new Pedido();
        pedido.setPedidoId(UUID.randomUUID());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setUsuario(usuario);

        PedidoItem pedidoItem1 = new PedidoItem(pedido, produto1, 2, produto1.getPreco());
        PedidoItem pedidoItem2 = new PedidoItem(pedido, produto2, 2, produto2.getPreco());

        pedido.getItems().add(pedidoItem1);
        pedido.getItems().add(pedidoItem2);

        when(usuarioRepository.findByUserId(userId)).thenReturn(Optional.of(usuario));
        when(pedidoRepository.findAllByUsuarioUserId(userId)).thenReturn(List.of(pedido));

        List<PedidoResponse> pedidos = pedidoService.findByUsuario(token);

        assertEquals(1, pedidos.size());
        assertNotNull(pedidos);
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedidos.get(0).status());
        assertEquals(BigDecimal.valueOf(5000), pedidos.get(0).total());

        verify(usuarioRepository).findByUserId(userId);
        verify(pedidoRepository).findAllByUsuarioUserId(userId);
    }

    @Test
    @DisplayName("Deve laçar Exceção quando Usuario não Existir")
    void deveLancarExcecaoAoBuscarPedidosEUsuarioNaoExiste() {

        UUID userId = UUID.randomUUID();

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getName()).thenReturn(userId.toString());
        when(usuarioRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.findByUsuario(token));

        verify(usuarioRepository).findByUserId(userId);
        verifyNoInteractions(pedidoRepository);
    }

}