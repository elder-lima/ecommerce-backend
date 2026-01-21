package dev.elder.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(columnDefinition = "CHAR(11)", unique = true, nullable = false) // define o tipo no banco
    @JdbcTypeCode(SqlTypes.CHAR) // define o tipo JDBC esperado
    private String cpf;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "auth_user_fk",
            nullable = false,
            unique = true
    )
    private AuthUser authUser;


}
