package Produto;

import Atores.Cliente;
import DatabaseConnection.DatabaseConnectionSingleton;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class ComboProdutoComposite extends ProdutoComponent implements ProdutoSubject{
    private String nome;
    private int estoque;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProdutoComponent> produtos = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cliente> observers = new ArrayList<>();

    public ComboProdutoComposite() {}

    public ComboProdutoComposite(String nome){
        this.nome = nome;
    }

    public void adicionarProduto(ProdutoComponent produto) {
        produtos.add(produto);
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public double getPreco() {
        double preco = 0;
        for (ProdutoComponent produto : produtos){
            preco += produto.getPreco();
        }
        return preco * 0.9;
    }

    @Override
    public int getEstoque() {
        estoque = produtos.stream().mapToInt(ProdutoComponent::getEstoque).min().orElse(0);
        return estoque;
    }

    @Override
    public void baixarEstoque(int quantidade) {
        for (ProdutoComponent produto : produtos) {
            produto.baixarEstoque(quantidade);
        }
    }

    @Override
    public void adicionarObserver(Cliente observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(Cliente observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(EntityManager em) {
        String mensagem = "Produto " + nome + " retornou ao estoque.";
        Iterator<Cliente> iterator = observers.iterator();
        while (iterator.hasNext()) {
            Cliente observer = iterator.next();
            Cliente clienteManaged = em.find(Cliente.class, observer.getId());
            if (clienteManaged != null) {
                clienteManaged.receberNotificacao(mensagem);
                em.merge(clienteManaged);
            }
            iterator.remove();
        }
        em.merge(this);
    }
}
