package loja.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import loja.model.Loja;

/**
 * Base dos controllers: lê os parâmetros da requisição, chama o método
 * específico e transforma erros de validação em mensagens para a tela.
 */
public abstract class BaseController implements HttpHandler {
    protected final Loja loja = Loja.getInstancia();

    /** Trata a requisição já com os parâmetros lidos. */
    protected abstract void tratar(HttpExchange ex, String caminho, Map<String, String> params) throws IOException;

    /** Página para onde o usuário volta quando ocorre um erro. */
    protected abstract String paginaDeErro(String caminho);

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String caminho = ex.getRequestURI().getPath();
        try {
            tratar(ex, caminho, lerParametros(ex));
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirecionar(ex, paginaDeErro(caminho), "erro", e.getMessage());
        } finally {
            ex.close();
        }
    }

    protected boolean isPost(HttpExchange ex) {
        return "POST".equalsIgnoreCase(ex.getRequestMethod());
    }

    protected void enviarHtml(HttpExchange ex, int status, String html) throws IOException {
        byte[] corpo = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(status, corpo.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(corpo);
        }
    }

    protected void naoEncontrado(HttpExchange ex) throws IOException {
        enviarHtml(ex, 404, "<h1>Página não encontrada</h1><a href='/produtos'>Voltar</a>");
    }

    /** Redireciona após um POST, levando uma mensagem de sucesso ou erro. */
    protected void redirecionar(HttpExchange ex, String destino, String tipo, String mensagem) throws IOException {
        String url = destino + "?" + tipo + "=" + URLEncoder.encode(mensagem, StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Location", url);
        ex.sendResponseHeaders(303, -1);
    }

    protected int lerInteiro(Map<String, String> params, String campo, String rotulo) {
        String valor = obrigatorio(params, campo, rotulo);
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O campo " + rotulo + " deve ser um número inteiro.");
        }
    }

    protected double lerDecimal(Map<String, String> params, String campo, String rotulo) {
        String valor = obrigatorio(params, campo, rotulo).replace(",", ".");
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O campo " + rotulo + " deve ser um número.");
        }
    }

    protected String obrigatorio(Map<String, String> params, String campo, String rotulo) {
        String valor = params.get(campo);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O campo " + rotulo + " é obrigatório.");
        }
        return valor.trim();
    }

    private Map<String, String> lerParametros(HttpExchange ex) throws IOException {
        Map<String, String> params = new HashMap<>();
        adicionar(params, ex.getRequestURI().getRawQuery());
        if (isPost(ex)) {
            adicionar(params, new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        }
        return params;
    }

    private void adicionar(Map<String, String> params, String texto) {
        if (texto == null || texto.isEmpty()) {
            return;
        }
        for (String par : texto.split("&")) {
            String[] chaveValor = par.split("=", 2);
            String chave = URLDecoder.decode(chaveValor[0], StandardCharsets.UTF_8);
            String valor = chaveValor.length > 1 ? URLDecoder.decode(chaveValor[1], StandardCharsets.UTF_8) : "";
            params.put(chave, valor);
        }
    }
}
