package loja.view;

import java.util.List;
import loja.model.ItemCarrinho;
import loja.model.Pedido;

import static loja.view.Layout.dinheiro;
import static loja.view.Layout.esc;

/** Tela de acompanhamento dos pedidos e das notificações. */
public class PedidoView {

    public String render(List<Pedido> pedidos, List<String> notificacoes, String msg, String erro, int itensCarrinho) {
        StringBuilder html = new StringBuilder("<div class='card'>");

        if (pedidos.isEmpty()) {
            html.append("<p class='vazio'>📦 Nenhum pedido realizado.</p>");
        } else {
            html.append("<table><tr><th>Pedido</th><th>Itens</th><th>Pagamento</th><th class='num'>Total</th>")
                .append("<th>Estado</th><th>Ações</th></tr>");
            for (Pedido p : pedidos) {
                html.append("<tr id='pedido-").append(p.getId()).append("'><td><strong>#").append(p.getId())
                    .append("</strong></td><td>");
                for (ItemCarrinho item : p.getItens()) {
                    html.append(item.getQuantidade()).append("x ").append(esc(item.getProduto().getNome())).append("<br>");
                }
                html.append("</td><td>").append(esc(p.getFormaPagamento().getNome())).append("</td>")
                    .append("<td class='num preco'>").append(dinheiro(p.getTotal())).append("</td>")
                    .append("<td><span class='estado badge ").append(classeDoEstado(p.getEstado().getNome())).append("'>")
                    .append(esc(p.getEstado().getNome())).append("</span></td><td><div class='acoes'>");
                // Botões de todas as ações; o State decide no Model se a ação é válida.
                for (String acao : List.of("pagar", "enviar", "entregar", "cancelar")) {
                    boolean permitida = p.getEstado().getAcoesPermitidas().contains(acao);
                    String estilo = !permitida ? "inativo" : acao.equals("cancelar") ? "perigo" : "";
                    html.append("<form class='inline' method='post' action='/pedidos/acao'>")
                        .append("<input type='hidden' name='id' value='").append(p.getId()).append("'>")
                        .append("<input type='hidden' name='acao' value='").append(acao).append("'>")
                        .append("<button type='submit' class='").append(estilo).append("'")
                        .append(" aria-label='").append(acao).append(" pedido ").append(p.getId()).append("'>")
                        .append(acao).append("</button></form>");
                }
                html.append("</div></td></tr>");
            }
            html.append("</table>");
        }
        html.append("</div>");

        html.append("<div class='card'><h2>🔔 Notificações</h2>");
        if (notificacoes.isEmpty()) {
            html.append("<p class='vazio'>Nenhuma notificação.</p>");
        } else {
            html.append("<ul id='notificacoes' class='notas'>");
            for (String n : notificacoes) {
                html.append("<li>").append(esc(n)).append("</li>");
            }
            html.append("</ul>");
        }
        html.append("</div>");

        return Layout.pagina("Pedidos", msg, erro, itensCarrinho, html.toString());
    }

    private String classeDoEstado(String nome) {
        return switch (nome) {
            case "Pago" -> "b-pago";
            case "Enviado" -> "b-enviado";
            case "Entregue" -> "b-entregue";
            case "Cancelado" -> "b-cancelado";
            default -> "b-aguardando";
        };
    }
}
