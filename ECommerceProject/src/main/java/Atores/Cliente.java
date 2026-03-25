package Atores;

import jakarta.persistence.*;
import Carrinho.*;
import Pedido.Pedido;
import Produto.ProdutoComponent;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente extends Usuario implements ClientObserver {
    @OneToOne(cascade = CascadeType.ALL)
    private Carrinho carrinho;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_notificacoes", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<String> notificacoes = new ArrayList<>();

    public Cliente() {
        this.carrinho = new Carrinho();
    }

    public Cliente(String nome, String email, String senha){
        super(nome, email, senha);
        this.carrinho = new Carrinho();
    }

    public void adicionarProdutoCarrinho(ProdutoComponent produto, int quantidade){
        if (quantidade <= produto.getEstoque()){
            ItemCarrinho novoItem = new ItemCarrinho(produto, quantidade);
            carrinho.adicionarItem(novoItem);
        } else {
            System.out.println("Quantidade ultrapassa estoque disponível.");
        }
    }

    public void removerProdutoCarrinho(int indice){
        carrinho.removerItem(indice);
    }

    public Carrinho getCarrinho(){
        return this.carrinho;
    }

    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    @Override
    public void receberNotificacao(String mensagem) {
        notificacoes.add(mensagem);
    }

    public void visualizarNotificacoes(){
        int indice = 0;
        for (String notificacao : notificacoes){
            System.out.println(indice + ": " + notificacao);
            indice++;
        }
    }

    public void limparNotificacoes(){
        notificacoes.clear();
    }
}
