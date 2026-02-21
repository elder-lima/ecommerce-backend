package dev.elder.ecommerce.repository;

import dev.elder.ecommerce.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findAllByUsuarioUserId(UUID id);
}
