package dev.elder.ecommerce.repository;

import dev.elder.ecommerce.entity.Role;
import dev.elder.ecommerce.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByNome(RoleName nome);

}
