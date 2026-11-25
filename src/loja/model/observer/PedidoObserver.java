package loja.model.observer;

import loja.model.Pedido;
import loja.model.estado.EstadoPedido;

/**
 * Padrão OBSERVER: quem implementa esta interface é avisado sempre que
 * um pedido muda de estado, sem que o Pedido precise conhecê-lo.
 */
public interface PedidoObserver {
    void estadoAlterado(Pedido pedido, EstadoPedido anterior);
}
