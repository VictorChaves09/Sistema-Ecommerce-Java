package Pagamento;

public class PagamentoFactory {
    public PagamentoStrategy criarPagamento(String tipo){
        switch (tipo){
            case "PIX":
                return new PixStrategy();
            case "Cartão":
                return new CartaoStrategy();
            case "Boleto":
                return new BoletoStrategy();
            default:
                return null;
        }
    }
}
