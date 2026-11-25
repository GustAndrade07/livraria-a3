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
            html.append("<p>Seu carrinho está vazio. <a href='/produtos'>Ver produtos</a></p>");
            return Layout.pagina("Carrinho", msg, erro, 0, html.toString());
        }

        html.append("<table><tr><th>Produto</th><th>Preço</th><th>Qtd.</th><th>Subtotal</th><th></th></tr>");
        for (ItemCarrinho item : carrinho.getItens()) {
            html.append("<tr><td>").append(esc(item.getProduto().getNome())).append("</td>")
                .append("<td>").append(dinheiro(item.getProduto().getPreco())).append("</td>")
                .append("<td>").append(item.getQuantidade()).append("</td>")
                .append("<td>").append(dinheiro(item.getSubtotal())).append("</td><td>")
                .append("<form class='inline' method='post' action='/carrinho/remover'>")
                .append("<input type='hidden' name='produtoId' value='").append(item.getProduto().getId()).append("'>")
                .append("<button type='submit' aria-label='Remover ").append(esc(item.getProduto().getNome()))
                .append("'>Remover</button></form></td></tr>");
        }
        html.append("</table>")
            .append("<p><strong>Subtotal: ").append(dinheiro(carrinho.getSubtotal())).append("</strong></p>");

        html.append("<fieldset><legend>Forma de pagamento</legend>")
            .append("<form method='post' action='/carrinho/finalizar'>");
        for (FormaPagamento forma : FormaPagamento.todas()) {
            html.append("<label><input type='radio' name='pagamento' value='").append(forma.getCodigo()).append("'> ")
                .append(esc(forma.getNome())).append(" — total ")
                .append(dinheiro(forma.calcularTotal(carrinho.getSubtotal())))
                .append("</label><br>");
        }
        html.append("<button type='submit'>Finalizar pedido</button></form></fieldset>");

        return Layout.pagina("Carrinho", msg, erro, carrinho.getQuantidadeTotal(), html.toString());
    }
}
