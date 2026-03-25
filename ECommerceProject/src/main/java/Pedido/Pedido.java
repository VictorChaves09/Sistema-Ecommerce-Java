package Pedido;

import jakarta.persistence.*;
import Atores.Cliente;
import Carrinho.ItemCarrinho;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cliente cliente;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemCarrinho> itens = new ArrayList<>();
    private double total;

    public Pedido() {}

    public Pedido(Cliente cliente, List<ItemCarrinho> itens, double total){
        this.cliente = cliente;
        this.itens = itens;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public String getItens() {
        StringBuilder itensNome = new StringBuilder();
        for (ItemCarrinho item : itens) {
            itensNome.append(item.getProduto().getNome()).append(", ");
        }
        return itensNome.toString();
    }

    public double getTotal() {
        return total;
    }
}
