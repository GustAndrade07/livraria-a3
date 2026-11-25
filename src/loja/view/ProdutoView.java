package loja.view;

import java.util.List;
import loja.model.Produto;

import static loja.view.Layout.dinheiro;
import static loja.view.Layout.esc;

/** Tela de consulta e cadastro de produtos. */
public class ProdutoView {

    public String render(List<Produto> produtos, String busca, String msg, String erro, int itensCarrinho) {
        StringBuilder html = new StringBuilder();

        html.append("<form method='get' action='/produtos'>")
            .append("<input name='busca' placeholder='Buscar produto' value='").append(esc(busca)).append("'>")
            .append("<button type='submit'>Buscar</button></form>");

        if (produtos.isEmpty()) {
            html.append("<p>Nenhum produto encontrado.</p>");
        } else {
            html.append("<table><tr><th>Código</th><th>Produto</th><th>Preço</th><th>Estoque</th><th>Comprar</th></tr>");
            for (Produto p : produtos) {
                html.append("<tr><td>").append(p.getId()).append("</td>")
                    .append("<td>").append(esc(p.getNome())).append("</td>")
                    .append("<td>").append(dinheiro(p.getPreco())).append("</td>")
                    .append("<td>").append(p.getEstoque()).append("</td><td>")
                    .append("<form class='inline' method='post' action='/carrinho/adicionar'>")
                    .append("<input type='hidden' name='produtoId' value='").append(p.getId()).append("'>")
                    .append("<input type='number' name='quantidade' value='1' style='width:60px' ")
                    .append("aria-label='Quantidade de ").append(esc(p.getNome())).append("'>")
                    .append("<button type='submit' aria-label='Adicionar ").append(esc(p.getNome()))
                    .append(" ao carrinho'>Adicionar</button></form></td></tr>");
            }
            html.append("</table>");
        }

        html.append("<fieldset><legend>Cadastrar produto</legend>")
            .append("<form method='post' action='/produtos/cadastrar'>")
            .append("<label>Nome <input name='nome'></label> ")
            .append("<label>Preço <input name='preco' placeholder='0,00'></label> ")
            .append("<label>Estoque <input name='estoque' type='number'></label> ")
            .append("<button type='submit'>Cadastrar</button></form></fieldset>");

        return Layout.pagina("Produtos", msg, erro, itensCarrinho, html.toString());
    }
}
