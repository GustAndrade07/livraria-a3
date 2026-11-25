package loja.model;

/** Um produto e a quantidade escolhida, no carrinho ou no pedido. */
public class ItemCarrinho {
    private final Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public void somar(int quantidade) {
        this.quantidade += quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
}
