package Carrinho;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarItem(ItemCarrinho novoItem) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().equals(novoItem.getProduto())) {
                item.setQuantidade(novoItem.getQuantidade());
                return;
            }
        }
        itens.add(novoItem);
    }

    public void removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()){
            itens.remove(indice);
        } else {
            System.out.println("Item não está presente no carrinho.");
        }
    }

    public double calcularTotal() {
        double total = 0.;
        for (ItemCarrinho item : itens){
            total += item.calcularSubtotal();
        }
        return total;
    }

    public List<ItemCarrinho> getItens(){
        return new ArrayList<>(itens);
    }

    public void limparCarrinho() {
        this.itens.clear();
    }
}
