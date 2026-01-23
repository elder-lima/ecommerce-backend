package dev.elder.ecommerce.repository;

import dev.elder.ecommerce.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM AuthUser u
        JOIN FETCH u.roles
        WHERE u.email = :email
    """)
    Optional<AuthUser> findByEmailWithRoles(String email);

    @Query("""
        SELECT u FROM AuthUser u
        JOIN FETCH u.roles
        WHERE u.auth_user_id = :role_id
    """)
    Optional<AuthUser> findByIdWithRoles(UUID id);

}
