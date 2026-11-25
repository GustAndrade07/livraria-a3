package loja.model;

/** Produto vendido pela loja. */
public class Produto {
    private final int id;
    private final String nome;
    private final double preco;
    private int estoque;

    public Produto(int id, String nome, double preco, int estoque) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.preco = preco;
        this.estoque = estoque;
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade > estoque) {
            throw new IllegalStateException("Estoque insuficiente para " + nome + ".");
        }
        estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        estoque += quantidade;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }
}
