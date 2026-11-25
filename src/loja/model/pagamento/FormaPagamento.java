package loja.model.pagamento;

import java.util.List;

/**
 * Padrão STRATEGY: cada forma de pagamento calcula o total da compra
 * à sua maneira (desconto, taxa ou valor integral).
 */
public interface FormaPagamento {
    String getCodigo();
    String getNome();
    double calcularTotal(double subtotal);

    /** Todas as estratégias disponíveis na loja. */
    static List<FormaPagamento> todas() {
        return List.of(new PagamentoPix(), new PagamentoCartao(), new PagamentoBoleto());
    }

    static FormaPagamento porCodigo(String codigo) {
        for (FormaPagamento forma : todas()) {
            if (forma.getCodigo().equals(codigo)) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Selecione uma forma de pagamento válida.");
    }
}
