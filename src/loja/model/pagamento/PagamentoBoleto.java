package loja.model.pagamento;

/** Boleto: taxa fixa de R$ 3,00. */
public class PagamentoBoleto implements FormaPagamento {
    public String getCodigo() { return "boleto"; }
    public String getNome() { return "Boleto (taxa de R$ 3,00)"; }
    public double calcularTotal(double subtotal) { return subtotal + 3.00; }
}
