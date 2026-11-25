package loja.controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;
import loja.model.Pedido;
import loja.view.PedidoView;

/** Rotas: GET /pedidos e POST /pedidos/acao (pagar, enviar, entregar, cancelar). */
public class PedidoController extends BaseController {
    private final PedidoView view = new PedidoView();

    @Override
    protected void tratar(HttpExchange ex, String caminho, Map<String, String> params) throws IOException {
        if (caminho.equals("/pedidos")) {
            enviarHtml(ex, 200, view.render(loja.getPedidos(), loja.getNotificacoes(),
                    params.get("msg"), params.get("erro"), loja.getCarrinho().getQuantidadeTotal()));
        } else if (caminho.equals("/pedidos/acao") && isPost(ex)) {
            Pedido pedido = loja.buscarPedido(lerInteiro(params, "id", "Pedido"));
            String acao = obrigatorio(params, "acao", "Ação");
            switch (acao) {
                case "pagar" -> pedido.pagar();
                case "enviar" -> pedido.enviar();
                case "entregar" -> pedido.entregar();
                case "cancelar" -> pedido.cancelar();
                default -> throw new IllegalArgumentException("Ação inválida.");
            }
            redirecionar(ex, "/pedidos", "msg",
                    "Pedido #" + pedido.getId() + " agora está: " + pedido.getEstado().getNome() + ".");
        } else {
            naoEncontrado(ex);
        }
    }

    @Override
    protected String paginaDeErro(String caminho) {
        return "/pedidos";
    }
}
