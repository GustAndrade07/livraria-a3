package loja.model.pagamento;

/** Pix: 10% de desconto. */
public class PagamentoPix implements FormaPagamento {
    public String getCodigo() { return "pix"; }
    public String getNome() { return "Pix (10% de desconto)"; }
    public double calcularTotal(double subtotal) { return subtotal * 0.90; }
}
