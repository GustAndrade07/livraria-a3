package loja.model.estado;

import java.util.List;
import loja.model.Pedido;

public class Enviado implements EstadoPedido {
    public String getNome() { return "Enviado"; }
    public List<String> getAcoesPermitidas() { return List.of("entregar"); }

    @Override
    public void entregar(Pedido pedido) { pedido.mudarEstado(new Entregue()); }
}
