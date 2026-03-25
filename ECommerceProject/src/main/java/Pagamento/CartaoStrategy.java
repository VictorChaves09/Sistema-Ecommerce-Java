package Pagamento;

public class CartaoStrategy implements PagamentoStrategy {

    @Override
    public void pagar(double valor) {
        System.out.println("=== Pagamento via cartão ===");
        System.out.printf("Valor: R$ %.2f\n", valor);
        System.out.println("Processando pagamento...");
        System.out.println("Pagamento aprovado.");
        System.out.println("============================");
    }
}
