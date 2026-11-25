package loja.model.observer;

import java.util.List;
import loja.model.Pedido;
import loja.model.estado.EstadoPedido;

/** Registra uma notificação para o cliente a cada mudança de estado. */
public class NotificacaoObserver implements PedidoObserver {
    private final List<String> notificacoes;

    public NotificacaoObserver(List<String> notificacoes) {
        this.notificacoes = notificacoes;
    }

    @Override
    public void estadoAlterado(Pedido pedido, EstadoPedido anterior) {
        notificacoes.add(0, "Pedido #" + pedido.getId() + ": "
                + anterior.getNome() + " → " + pedido.getEstado().getNome());
    }
}
