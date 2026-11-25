package loja;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import loja.controller.CarrinhoController;
import loja.controller.PedidoController;
import loja.controller.ProdutoController;

/** Inicia o servidor web e liga cada caminho ao seu controller. */
public class Main {
    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer servidor = HttpServer.create(new InetSocketAddress(porta), 0);

        servidor.createContext("/produtos", new ProdutoController());
        servidor.createContext("/carrinho", new CarrinhoController());
        servidor.createContext("/pedidos", new PedidoController());
        servidor.createContext("/", ex -> {
            ex.getResponseHeaders().set("Location", "/produtos");
            ex.sendResponseHeaders(303, -1);
            ex.close();
        });

        servidor.start();
        System.out.println("Livraria A3 rodando em http://localhost:" + porta);
    }
}
