package loja.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Carrinho de compras: guarda os itens antes de virar pedido. */
public class Carrinho {
    private final Map<Integer, ItemCarrinho> itens = new LinkedHashMap<>();

    public void adicionar(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        int jaNoCarrinho = itens.containsKey(produto.getId())
                ? itens.get(produto.getId()).getQuantidade() : 0;
        if (jaNoCarrinho + quantidade > produto.getEstoque()) {
            throw new IllegalStateException("Estoque insuficiente: há apenas "
                    + produto.getEstoque() + " unidade(s) de " + produto.getNome() + ".");
        }
        if (itens.containsKey(produto.getId())) {
            itens.get(produto.getId()).somar(quantidade);
        } else {
            itens.put(produto.getId(), new ItemCarrinho(produto, quantidade));
        }
    }

    public void remover(int produtoId) {
        if (itens.remove(produtoId) == null) {
            throw new IllegalArgumentException("Este produto não está no carrinho.");
        }
    }

    public double getSubtotal() {
        double total = 0;
        for (ItemCarrinho item : itens.values()) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getQuantidadeTotal() {
        int total = 0;
        for (ItemCarrinho item : itens.values()) {
            total += item.getQuantidade();
        }
        return total;
    }

    public List<ItemCarrinho> getItens() { return new ArrayList<>(itens.values()); }
    public boolean isVazio() { return itens.isEmpty(); }
    public void limpar() { itens.clear(); }
}
