package dev.elder.ecommerce.repository;

import dev.elder.ecommerce.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByUserId(UUID authUserId);

    boolean existsByCpf(String cpf);

}
