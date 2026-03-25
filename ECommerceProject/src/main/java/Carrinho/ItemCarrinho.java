package Carrinho;

import jakarta.persistence.*;
import Produto.ProdutoComponent;

@Entity
public class ItemCarrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ProdutoComponent produto;
    private int quantidade;

    public ItemCarrinho() {}

    public ItemCarrinho(ProdutoComponent produto, int quantidade){
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ProdutoComponent getProduto(){
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double calcularSubtotal(){
        return (produto.getPreco() * quantidade);
    }
}
