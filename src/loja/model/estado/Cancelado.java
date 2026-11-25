package loja.model.estado;

import java.util.List;

/** Estado final: nenhuma ação é permitida. */
public class Cancelado implements EstadoPedido {
    public String getNome() { return "Cancelado"; }
    public List<String> getAcoesPermitidas() { return List.of(); }
}
