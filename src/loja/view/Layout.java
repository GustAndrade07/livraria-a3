package loja.view;

import java.util.Locale;

/** Moldura HTML comum a todas as telas (menu, mensagens e estilo). */
public final class Layout {
    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    private Layout() { }

    public static String pagina(String titulo, String msg, String erro, int itensCarrinho, String conteudo) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'>")
            .append("<title>").append(esc(titulo)).append(" - Livraria A3</title>")
            .append("<style>")
            .append("body{font-family:Arial,sans-serif;max-width:900px;margin:20px auto;padding:0 16px;color:#222}")
            .append("nav{background:#2c3e50;padding:12px;border-radius:6px}")
            .append("nav a{color:#fff;margin-right:18px;text-decoration:none;font-weight:bold}")
            .append("table{width:100%;border-collapse:collapse;margin:12px 0}")
            .append("th,td{border:1px solid #ccc;padding:6px;text-align:left}th{background:#eee}")
            .append("form.inline{display:inline}input,select,button{padding:5px;margin:2px}")
            .append("button{cursor:pointer}fieldset{margin:12px 0}")
            .append(".msg{background:#d4edda;padding:10px;border-radius:4px}")
            .append(".erro{background:#f8d7da;padding:10px;border-radius:4px}")
            .append("</style></head><body>")
            .append("<nav><a href='/produtos'>Produtos</a>")
            .append("<a href='/carrinho'>Carrinho (").append(itensCarrinho).append(")</a>")
            .append("<a href='/pedidos'>Pedidos</a></nav>")
            .append("<h1>").append(esc(titulo)).append("</h1>");
        if (msg != null && !msg.isBlank()) {
            html.append("<p class='msg' role='status'>").append(esc(msg)).append("</p>");
        }
        if (erro != null && !erro.isBlank()) {
            html.append("<p class='erro' role='alert'>").append(esc(erro)).append("</p>");
        }
        html.append(conteudo).append("</body></html>");
        return html.toString();
    }

    public static String dinheiro(double valor) {
        return String.format(BR, "R$ %,.2f", valor);
    }

    /** Escapa texto digitado pelo usuário antes de colocá-lo no HTML. */
    public static String esc(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
