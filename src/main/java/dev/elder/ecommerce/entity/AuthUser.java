package dev.elder.ecommerce.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "auth_user")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID auth_user_id;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "auth_user_fk"),
            inverseJoinColumns = @JoinColumn(name = "role_fk"))
    private Set<Role> roles = new HashSet<>();

    public AuthUser() {
    }

    public AuthUser(UUID auth_user_id, String email, String senha) {
        this.auth_user_id = auth_user_id;
        this.email = email;
        this.senha = senha;
    }

    public UUID getAuth_user_id() {
        return auth_user_id;
    }

    public void setAuth_user_id(UUID auth_user_id) {
        this.auth_user_id = auth_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
