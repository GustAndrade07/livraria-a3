package loja.model.pagamento;

/** Cartão de crédito: valor integral. */
public class PagamentoCartao implements FormaPagamento {
    public String getCodigo() { return "cartao"; }
    public String getNome() { return "Cartão de crédito"; }
    public double calcularTotal(double subtotal) { return subtotal; }
}
