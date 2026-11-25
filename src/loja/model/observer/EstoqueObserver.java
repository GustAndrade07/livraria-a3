package loja.model.observer;

import loja.model.ItemCarrinho;
import loja.model.Pedido;
import loja.model.estado.Cancelado;
import loja.model.estado.EstadoPedido;

/** Devolve os itens ao estoque quando um pedido é cancelado. */
public class EstoqueObserver implements PedidoObserver {
    @Override
    public void estadoAlterado(Pedido pedido, EstadoPedido anterior) {
        if (pedido.getEstado() instanceof Cancelado) {
            for (ItemCarrinho item : pedido.getItens()) {
                item.getProduto().reporEstoque(item.getQuantidade());
            }
        }
    }
}
