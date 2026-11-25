package loja.model.estado;

import java.util.List;
import loja.model.Pedido;

public class Pago implements EstadoPedido {
    public String getNome() { return "Pago"; }
    public List<String> getAcoesPermitidas() { return List.of("enviar", "cancelar"); }

    @Override
    public void enviar(Pedido pedido) { pedido.mudarEstado(new Enviado()); }

    @Override
    public void cancelar(Pedido pedido) { pedido.mudarEstado(new Cancelado()); }
}
