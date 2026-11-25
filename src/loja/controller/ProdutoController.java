package loja.controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;
import loja.view.ProdutoView;

/** Rotas: GET /produtos (consulta) e POST /produtos/cadastrar. */
public class ProdutoController extends BaseController {
    private final ProdutoView view = new ProdutoView();

    @Override
    protected void tratar(HttpExchange ex, String caminho, Map<String, String> params) throws IOException {
        if (caminho.equals("/produtos")) {
            String busca = params.get("busca");
            enviarHtml(ex, 200, view.render(loja.buscarProdutos(busca), busca,
                    params.get("msg"), params.get("erro"), loja.getCarrinho().getQuantidadeTotal()));
        } else if (caminho.equals("/produtos/cadastrar") && isPost(ex)) {
            String nome = obrigatorio(params, "nome", "Nome");
            double preco = lerDecimal(params, "preco", "Preço");
            int estoque = lerInteiro(params, "estoque", "Estoque");
            loja.cadastrarProduto(nome, preco, estoque);
            redirecionar(ex, "/produtos", "msg", "Produto \"" + nome + "\" cadastrado.");
        } else {
            naoEncontrado(ex);
        }
    }

    @Override
    protected String paginaDeErro(String caminho) {
        return "/produtos";
    }
}
