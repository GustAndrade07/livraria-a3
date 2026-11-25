package loja.model.estado;

import java.util.List;
import loja.model.Pedido;

/**
 * Padrão STATE: cada estado do pedido decide quais ações são permitidas
 * e para qual estado o pedido vai. Por padrão, toda ação é proibida;
 * cada estado concreto libera apenas as transições válidas.
 */
public interface EstadoPedido {
    String getNome();

    /** Ações que a tela pode oferecer neste estado. */
    List<String> getAcoesPermitidas();

    default void pagar(Pedido pedido) { negar("pagar"); }
    default void enviar(Pedido pedido) { negar("enviar"); }
    default void entregar(Pedido pedido) { negar("entregar"); }
    default void cancelar(Pedido pedido) { negar("cancelar"); }

    private void negar(String acao) {
        throw new IllegalStateException("Não é possível " + acao
                + " um pedido no estado \"" + getNome() + "\".");
    }
}
