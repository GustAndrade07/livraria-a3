package loja.model;

import java.util.ArrayList;
import java.util.List;
import loja.model.estado.AguardandoPagamento;
import loja.model.estado.EstadoPedido;
import loja.model.observer.PedidoObserver;
import loja.model.pagamento.FormaPagamento;

/**
 * Pedido finalizado. É o "contexto" do padrão State (delega as ações ao
 * estado atual) e o "sujeito" do padrão Observer (avisa os observadores).
 */
public class Pedido {
    private final int id;
    private final List<ItemCarrinho> itens;
    private final FormaPagamento formaPagamento;
    private final double subtotal;
    private final double total;
    private EstadoPedido estado = new AguardandoPagamento();
    private final List<PedidoObserver> observadores = new ArrayList<>();

    public Pedido(int id, List<ItemCarrinho> itens, FormaPagamento formaPagamento, double subtotal) {
        this.id = id;
        this.itens = List.copyOf(itens);
        this.formaPagamento = formaPagamento;
        this.subtotal = subtotal;
        this.total = formaPagamento.calcularTotal(subtotal);
    }

    // ---- Ações (State) ----
    public void pagar() { estado.pagar(this); }
    public void enviar() { estado.enviar(this); }
    public void entregar() { estado.entregar(this); }
    public void cancelar() { estado.cancelar(this); }

    /** Chamado apenas pelos estados concretos ao fazer a transição. */
    public void mudarEstado(EstadoPedido novoEstado) {
        EstadoPedido anterior = this.estado;
        this.estado = novoEstado;
        for (PedidoObserver observador : observadores) {
            observador.estadoAlterado(this, anterior);
        }
    }

    // ---- Observer ----
    public void adicionarObservador(PedidoObserver observador) {
        observadores.add(observador);
    }

    public int getId() { return id; }
    public List<ItemCarrinho> getItens() { return itens; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return total; }
    public EstadoPedido getEstado() { return estado; }
}
