package loja.model.estado;

import java.util.List;

/** Estado final: nenhuma ação é permitida. */
public class Entregue implements EstadoPedido {
    public String getNome() { return "Entregue"; }
    public List<String> getAcoesPermitidas() { return List.of(); }
}
