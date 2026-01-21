package dev.elder.ecommerce.entity;

import dev.elder.ecommerce.entity.enums.RoleName;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    private UUID roleId;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName nome;

    public Role() {
    }

    public Role(UUID roleId, RoleName nome) {
        this.roleId = roleId;
        this.nome = nome;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public RoleName getNome() {
        return nome;
    }

    public void setName(RoleName nome) {
        this.nome = nome;
    }
}
