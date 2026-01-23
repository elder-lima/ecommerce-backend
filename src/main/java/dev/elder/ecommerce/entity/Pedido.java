package dev.elder.ecommerce.entity;

import dev.elder.ecommerce.entity.enums.StatusPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pedido_id")
    private UUID pedidoId;

    @Column(nullable = false)
    private Instant criacao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @ManyToOne
    @JoinColumn(name = "usuario_fk", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "id.pedido")
    private Set<PedidoItem> items = new HashSet<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    public Pedido() {
    }

    public Pedido(UUID pedidoId, Instant criacao, StatusPedido status, Usuario usuario, Pagamento pagamento) {
        this.pedidoId = pedidoId;
        this.criacao = criacao;
        this.status = status;
        this.usuario = usuario;
        this.pagamento = pagamento;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(UUID pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Instant getCriacao() {
        return criacao;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for (PedidoItem x : items) {
            sum = sum.add(x.getSubTotal());
        }
        return sum;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<PedidoItem> getItems() {
        return items;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(pedidoId, pedido.pedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pedidoId);
    }
}
