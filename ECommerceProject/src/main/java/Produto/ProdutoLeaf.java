package Produto;

import jakarta.persistence.*;
import Atores.Cliente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class ProdutoLeaf extends ProdutoComponent implements ProdutoSubject {
    private String nome;
    private double preco;
    private int estoque;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cliente> observers = new ArrayList<>();

    public ProdutoLeaf() {}

    public ProdutoLeaf(String nome, double preco, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    @Override
    public double getPreco() {
        return preco;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getEstoque() {
        return estoque;
    }

    @Override
    public void baixarEstoque(int quantidade) {
        this.estoque -= quantidade;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
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
