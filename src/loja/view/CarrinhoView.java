package loja.view;

import loja.model.Carrinho;
import loja.model.ItemCarrinho;
import loja.model.pagamento.FormaPagamento;

import static loja.view.Layout.dinheiro;
import static loja.view.Layout.esc;

/** Tela do carrinho: itens, totais por forma de pagamento e finalização. */
public class CarrinhoView {

    public String render(Carrinho carrinho, String msg, String erro) {
        StringBuilder html = new StringBuilder();

        if (carrinho.isVazio()) {
            html.append("<div class='card vazio'>🛒<p>Seu carrinho está vazio. <a href='/produtos'>Ver produtos</a></p></div>");
            return Layout.pagina("Carrinho", msg, erro, 0, html.toString());
        }

        html.append("<div class='card'><table><tr><th>Produto</th><th class='num'>Preço</th>")
            .append("<th class='num'>Qtd.</th><th class='num'>Subtotal</th><th></th></tr>");
        for (ItemCarrinho item : carrinho.getItens()) {
            html.append("<tr><td>").append(esc(item.getProduto().getNome())).append("</td>")
                .append("<td class='num'>").append(dinheiro(item.getProduto().getPreco())).append("</td>")
                .append("<td class='num'>").append(item.getQuantidade()).append("</td>")
                .append("<td class='num preco'>").append(dinheiro(item.getSubtotal())).append("</td><td class='num'>")
                .append("<form class='inline' method='post' action='/carrinho/remover'>")
                .append("<input type='hidden' name='produtoId' value='").append(item.getProduto().getId()).append("'>")
                .append("<button class='perigo' type='submit' aria-label='Remover ").append(esc(item.getProduto().getNome()))
                .append("'>Remover</button></form></td></tr>");
        }
        html.append("</table>")
            .append("<div class='total'><strong>Subtotal: ").append(dinheiro(carrinho.getSubtotal()))
            .append("</strong></div></div>");

        html.append("<div class='card'><h2>Forma de pagamento</h2>")
            .append("<form method='post' action='/carrinho/finalizar'>");
        for (FormaPagamento forma : FormaPagamento.todas()) {
            html.append("<label class='opcao'><input type='radio' name='pagamento' value='").append(forma.getCodigo()).append("'> ")
                .append(esc(forma.getNome())).append(" — total ")
                .append(dinheiro(forma.calcularTotal(carrinho.getSubtotal())))
                .append("</label>");
        }
        html.append("<button type='submit'>Finalizar pedido</button></form></div>");

        return Layout.pagina("Carrinho", msg, erro, carrinho.getQuantidadeTotal(), html.toString());
    }
}
