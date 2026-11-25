package loja.view;

import java.util.Locale;

/** Moldura HTML comum a todas as telas (cabeçalho, menu, mensagens e estilo). */
public final class Layout {
    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    private static final String CSS = """
        *{box-sizing:border-box}
        body{margin:0;font-family:'Segoe UI',Roboto,Arial,sans-serif;background:#f3f5f9;color:#1f2937}
        header{background:linear-gradient(90deg,#1e3a8a,#2563eb);color:#fff;box-shadow:0 2px 8px rgba(0,0,0,.15)}
        .topo{max-width:1000px;margin:0 auto;padding:14px 20px;display:flex;align-items:center;gap:24px}
        .marca{font-size:22px;font-weight:700;color:#fff;text-decoration:none;white-space:nowrap}
        .marca small{display:block;font-size:12px;font-weight:400;opacity:.8}
        nav{display:flex;gap:6px;margin-left:auto}
        nav a{color:#dbeafe;text-decoration:none;padding:8px 14px;border-radius:8px;font-weight:600}
        nav a:hover{background:rgba(255,255,255,.15);color:#fff}
        nav a.ativo{background:#fff;color:#1e3a8a}
        main{max-width:1000px;margin:24px auto;padding:0 20px}
        h1{margin:0 0 16px;font-size:26px}
        h2{font-size:18px;margin:0 0 12px}
        .card{background:#fff;border-radius:12px;box-shadow:0 1px 3px rgba(0,0,0,.08);padding:20px;margin-bottom:20px}
        .msg,.erro{padding:12px 16px;border-radius:8px;margin-bottom:16px;font-weight:500}
        .msg{background:#dcfce7;color:#166534;border-left:4px solid #22c55e}
        .erro{background:#fee2e2;color:#991b1b;border-left:4px solid #ef4444}
        table{width:100%;border-collapse:collapse}
        th{text-align:left;font-size:13px;text-transform:uppercase;letter-spacing:.03em;color:#6b7280;
          padding:10px 12px;border-bottom:2px solid #e5e7eb}
        td{padding:12px;border-bottom:1px solid #f0f1f4;vertical-align:middle}
        tr:hover td{background:#f9fafb}
        .num{text-align:right;white-space:nowrap}
        .preco{font-weight:600;color:#1e3a8a}
        .zero{color:#dc2626;font-weight:700}
        input,select{padding:9px 11px;border:1px solid #d1d5db;border-radius:8px;font-size:14px;font-family:inherit}
        input:focus{outline:2px solid #93c5fd;border-color:#2563eb}
        button{padding:9px 16px;border:0;border-radius:8px;font-size:14px;font-weight:600;cursor:pointer;
          background:#2563eb;color:#fff;font-family:inherit}
        button:hover{background:#1d4ed8}
        button.sec{background:#e5e7eb;color:#374151}
        button.sec:hover{background:#d1d5db}
        button.perigo{background:#fee2e2;color:#b91c1c}
        button.perigo:hover{background:#fecaca}
        button.inativo{background:#f3f4f6;color:#9ca3af}
        form.inline{display:inline}
        .busca{display:flex;gap:8px;margin-bottom:16px}
        .busca input{flex:1}
        .qtd{width:64px;margin-right:6px}
        .grade{display:grid;grid-template-columns:2fr 1fr 1fr auto;gap:12px;align-items:end}
        .grade label{display:flex;flex-direction:column;gap:4px;font-size:13px;font-weight:600;color:#4b5563}
        .total{display:flex;justify-content:space-between;font-size:18px;padding-top:12px}
        .opcao{display:flex;align-items:center;gap:10px;padding:12px 14px;border:2px solid #e5e7eb;border-radius:10px;
          margin-bottom:10px;cursor:pointer}
        .opcao:hover{border-color:#93c5fd;background:#eff6ff}
        .opcao:has(input:checked){border-color:#2563eb;background:#eff6ff}
        .vazio{text-align:center;color:#6b7280;padding:30px}
        .vazio a{color:#2563eb;font-weight:600}
        .badge{display:inline-block;padding:4px 10px;border-radius:999px;font-size:13px;font-weight:600}
        .b-aguardando{background:#fef3c7;color:#92400e}.b-pago{background:#dbeafe;color:#1e40af}
        .b-enviado{background:#ede9fe;color:#5b21b6}.b-entregue{background:#dcfce7;color:#166534}
        .b-cancelado{background:#fee2e2;color:#991b1b}
        .acoes{display:flex;flex-wrap:wrap;gap:6px}
        .acoes button{padding:6px 12px;font-size:13px;text-transform:capitalize}
        ul.notas{list-style:none;padding:0;margin:0}
        ul.notas li{padding:10px 12px;border-left:3px solid #2563eb;background:#f8fafc;margin-bottom:8px;border-radius:4px}
        footer{text-align:center;color:#9ca3af;font-size:13px;padding:20px}
        """;

    private Layout() { }

    public static String pagina(String titulo, String msg, String erro, int itensCarrinho, String conteudo) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1'>")
            .append("<title>").append(esc(titulo)).append(" - Livraria A3</title>")
            .append("<style>").append(CSS).append("</style></head><body>")
            .append("<header><div class='topo'>")
            .append("<a class='marca' href='/produtos'>📚 Livraria A3<small>Livros para todos os gostos</small></a>")
            .append("<nav>")
            .append(link("/produtos", "Produtos", titulo))
            .append("<a href='/carrinho'").append(titulo.equals("Carrinho") ? " class='ativo'" : "").append(">")
            .append("Carrinho (").append(itensCarrinho).append(")</a>")
            .append(link("/pedidos", "Pedidos", titulo))
            .append("</nav></div></header><main>")
            .append("<h1>").append(esc(titulo)).append("</h1>");
        if (msg != null && !msg.isBlank()) {
            html.append("<p class='msg' role='status'>").append(esc(msg)).append("</p>");
        }
        if (erro != null && !erro.isBlank()) {
            html.append("<p class='erro' role='alert'>").append(esc(erro)).append("</p>");
        }
        html.append(conteudo)
            .append("</main><footer>Livraria A3 · Engenharia e Arquitetura de Software</footer></body></html>");
        return html.toString();
    }

    private static String link(String href, String texto, String titulo) {
        return "<a href='" + href + "'" + (texto.equals(titulo) ? " class='ativo'" : "") + ">" + texto + "</a>";
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
