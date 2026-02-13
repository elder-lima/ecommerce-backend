package dev.elder.ecommerce.service;

import dev.elder.ecommerce.dto.request.PedidoItemRequest;
import dev.elder.ecommerce.dto.request.PedidoRequest;
import dev.elder.ecommerce.dto.response.PedidoItemResponse;
import dev.elder.ecommerce.dto.response.PedidoResponse;
import dev.elder.ecommerce.dto.response.UsuarioResponse;
import dev.elder.ecommerce.entity.*;
import dev.elder.ecommerce.entity.enums.StatusPedido;
import dev.elder.ecommerce.repository.AuthUserRepository;
import dev.elder.ecommerce.repository.PedidoRepository;
import dev.elder.ecommerce.repository.ProdutoRepository;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest pedidoRequest, JwtAuthenticationToken token) {

        Usuario usuario = usuarioRepository.findByUserId(UUID.fromString(token.getName())).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        Pedido pedido =  new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setCriacao(Instant.now());

        for (PedidoItemRequest itemRequest : pedidoRequest.itens()) {

            Produto produto = produtoRepository.findById(itemRequest.produtoId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: "+ itemRequest.produtoId()));

            PedidoItem item = new PedidoItem(pedido, produto, itemRequest.quantidade(), produto.getPreco());

            pedido.getItems().add(item);
        }

        pedidoRepository.save(pedido);

        List<PedidoItemResponse> pedidoItemResponses = pedido.getItems().stream()
                .map(
                        item -> new PedidoItemResponse(
                                item.getProduto().getProduto_id(),
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getPreco()
                        )
                ).toList();

        return new PedidoResponse(
                pedido.getPedidoId(),
                pedido.getCriacao(),
                pedido.getStatus(),
                pedido.getTotal(),
                new UsuarioResponse(
                        usuario.getUserId(),
                        usuario.getAuthUser().getEmail(),
                        usuario.getNome(),
                        usuario.getTelefone()),
                        pedidoItemResponses
                );
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> findByUsuario(JwtAuthenticationToken token) {
        Usuario usuario = usuarioRepository.findByUserId(UUID.fromString(token.getName())).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado."));
        List<Pedido> pedidos = pedidoRepository.findAllByUsuarioUserId(usuario.getUserId());
        return pedidos.stream().map(pedido -> new PedidoResponse(
                pedido.getPedidoId(),
                pedido.getCriacao(),
                pedido.getStatus(),
                pedido.getTotal(),
                new UsuarioResponse(
                        pedido.getUsuario().getUserId(),
                            pedido.getUsuario().getAuthUser().getEmail(),
                            pedido.getUsuario().getNome(),
                        pedido.getUsuario().getTelefone()
                        ),
                pedido.getItems().stream().map( item -> new PedidoItemResponse(
                        item.getProduto().getProduto_id(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPreco()
                )).toList()
        )).toList();
    }

}
