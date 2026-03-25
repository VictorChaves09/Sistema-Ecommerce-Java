package Pagamento;

import java.util.UUID;

public class PixStrategy implements PagamentoStrategy {

    @Override
    public void pagar(double valor) {
        String chave = UUID.randomUUID().toString();
        System.out.println("=== Pagamento via PIX ===");
        System.out.printf("Valor: R$ %.2f\n", valor);
        System.out.println("Chave PIX: " + chave);
        System.out.println("Pagamento aprovado.");
        System.out.println("==========================");
    }
}
