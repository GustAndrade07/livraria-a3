package loja.model;

import java.util.ArrayList;
import java.util.List;
import loja.model.observer.EstoqueObserver;
import loja.model.observer.NotificacaoObserver;
import loja.model.pagamento.FormaPagamento;

/**
 * Padrão SINGLETON: a loja guarda em memória os produtos, o carrinho e os
 * pedidos. Existe uma única instância, compartilhada por todos os controllers,
 * para que todos vejam os mesmos dados.
 */
public class Loja {
    private static Loja instancia;

    private final List<Produto> produtos = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<String> notificacoes = new ArrayList<>();
    private final Carrinho carrinho = new Carrinho();
    private int proximoIdProduto = 1;
    private int proximoIdPedido = 1;

    private Loja() {
        cadastrarProduto("Dom Casmurro", 29.90, 10);
        cadastrarProduto("O Pequeno Príncipe", 24.50, 5);
        cadastrarProduto("Clean Code", 89.00, 3);
    }

    public static synchronized Loja getInstancia() {
        if (instancia == null) {
            instancia = new Loja();
        }
        return instancia;
    }

    // ---------- Produtos ----------
    public Produto cadastrarProduto(String nome, double preco, int estoque) {
        for (Produto p : produtos) {
            if (nome != null && p.getNome().equalsIgnoreCase(nome.trim())) {
                throw new IllegalArgumentException("Já existe um produto com este nome.");
            }
        }
        Produto produto = new Produto(proximoIdProduto, nome, preco, estoque);
        produtos.add(produto);
        proximoIdProduto++;
        return produto;
    }

    public List<Produto> buscarProdutos(String termo) {
        if (termo == null || termo.isBlank()) {
            return new ArrayList<>(produtos);
        }
        List<Produto> encontrados = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(termo.trim().toLowerCase())) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    public Produto buscarProduto(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Produto não encontrado.");
    }

    // ---------- Pedidos ----------
    public Pedido finalizarPedido(FormaPagamento formaPagamento) {
        if (carrinho.isVazio()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }
        for (ItemCarrinho item : carrinho.getItens()) {
            if (item.getQuantidade() > item.getProduto().getEstoque()) {
                throw new IllegalStateException("Estoque insuficiente para "
                        + item.getProduto().getNome() + ".");
            }
        }
        for (ItemCarrinho item : carrinho.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        Pedido pedido = new Pedido(proximoIdPedido++, carrinho.getItens(),
                formaPagamento, carrinho.getSubtotal());
        pedido.adicionarObservador(new EstoqueObserver());
        pedido.adicionarObservador(new NotificacaoObserver(notificacoes));
        pedidos.add(pedido);
        carrinho.limpar();
        return pedido;
    }

    public Pedido buscarPedido(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Pedido não encontrado.");
    }

    public List<Pedido> getPedidos() { return new ArrayList<>(pedidos); }
    public List<String> getNotificacoes() { return new ArrayList<>(notificacoes); }
    public Carrinho getCarrinho() { return carrinho; }
}
