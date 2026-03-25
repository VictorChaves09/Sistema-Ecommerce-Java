package Produto;

import Atores.ClientObserver;
import Atores.Cliente;
import jakarta.persistence.EntityManager;

public interface ProdutoSubject {
    void adicionarObserver(Cliente observer);
    void removerObserver(Cliente observer);
    void notificarObservers(EntityManager em);
}
