package dev.elder.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @Column(name = "pagamento_id")
    private UUID pagamentoId;

    private Instant momento;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "pagamento_id")
    private Pedido pedido;

    public Pagamento() {
    }

    public Pagamento(UUID pagamentoId, Instant momento, Pedido pedido) {
        this.pagamentoId = pagamentoId;
        this.momento = momento;
        this.pedido = pedido;
    }

    public UUID getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(UUID pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public Instant getMomento() {
        return momento;
    }

    public void setMomento(Instant momento) {
        this.momento = momento;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
