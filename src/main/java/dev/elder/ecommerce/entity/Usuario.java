package dev.elder.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario{

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(columnDefinition = "CHAR(11)", unique = true, nullable = false) // define o tipo no banco
    @JdbcTypeCode(SqlTypes.CHAR) // define o tipo JDBC esperado
    private String cpf;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private AuthUser authUser;

    public Usuario() {
    }

    public Usuario(UUID userId, String nome, String telefone, String cpf, AuthUser authUser) {
        this.userId = userId;
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.authUser = authUser;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }
}
