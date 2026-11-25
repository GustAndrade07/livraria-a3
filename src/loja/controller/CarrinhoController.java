package loja.controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;
import loja.model.Pedido;
import loja.model.Produto;
import loja.model.pagamento.FormaPagamento;
import loja.view.CarrinhoView;

/** Rotas: GET /carrinho e POST /carrinho/adicionar, /remover, /finalizar. */
public class CarrinhoController extends BaseController {
    private final CarrinhoView view = new CarrinhoView();

    @Override
    protected void tratar(HttpExchange ex, String caminho, Map<String, String> params) throws IOException {
        if (caminho.equals("/carrinho")) {
            enviarHtml(ex, 200, view.render(loja.getCarrinho(), params.get("msg"), params.get("erro")));
        } else if (caminho.equals("/carrinho/adicionar") && isPost(ex)) {
            Produto produto = loja.buscarProduto(lerInteiro(params, "produtoId", "Produto"));
            int quantidade = lerInteiro(params, "quantidade", "Quantidade");
            loja.getCarrinho().adicionar(produto, quantidade);
            redirecionar(ex, "/produtos", "msg", quantidade + "x " + produto.getNome() + " adicionado ao carrinho.");
        } else if (caminho.equals("/carrinho/remover") && isPost(ex)) {
            loja.getCarrinho().remover(lerInteiro(params, "produtoId", "Produto"));
            redirecionar(ex, "/carrinho", "msg", "Item removido do carrinho.");
        } else if (caminho.equals("/carrinho/finalizar") && isPost(ex)) {
            FormaPagamento forma = FormaPagamento.porCodigo(params.get("pagamento"));
            Pedido pedido = loja.finalizarPedido(forma);
            redirecionar(ex, "/pedidos", "msg", "Pedido #" + pedido.getId() + " realizado com sucesso.");
        } else {
            naoEncontrado(ex);
        }
    }

    @Override
    protected String paginaDeErro(String caminho) {
        return caminho.equals("/carrinho/adicionar") ? "/produtos" : "/carrinho";
    }
}
