package loja.model.estado;

import java.util.List;
import loja.model.Pedido;

public class AguardandoPagamento implements EstadoPedido {
    public String getNome() { return "Aguardando pagamento"; }
    public List<String> getAcoesPermitidas() { return List.of("pagar", "cancelar"); }

    @Override
    public void pagar(Pedido pedido) { pedido.mudarEstado(new Pago()); }

    @Override
    public void cancelar(Pedido pedido) { pedido.mudarEstado(new Cancelado()); }
}
