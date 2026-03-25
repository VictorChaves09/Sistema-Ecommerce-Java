package Pagamento;

public class ProcessadorPagamento {
    private PagamentoStrategy strategy;
    private PagamentoFactory factory = new PagamentoFactory();

    public ProcessadorPagamento(String tipo){
        strategy = factory.criarPagamento(tipo);
    }

    public void processarPagamento(double valor){
        strategy.pagar(valor);
    }
}
