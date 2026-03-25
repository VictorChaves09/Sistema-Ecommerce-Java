package Pagamento;

import java.time.LocalDate;

public class BoletoStrategy implements PagamentoStrategy {
    @Override
    public void pagar(double valor) {
        LocalDate vencimento = LocalDate.now().plusDays(3);

        System.out.println("=== Pagamento via boleto ===");
        System.out.printf("Valor: R$ %.2f\n", valor);
        System.out.println("Vencimento: " + vencimento);
        System.out.println("Boleto enviado por email.");
        System.out.println("============================");
    }
}
