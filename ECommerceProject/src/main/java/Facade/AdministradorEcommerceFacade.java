package Facade;

import Atores.Administrador;
import DatabaseConnection.DatabaseConnectionSingleton;
import Pedido.Pedido;
import Produto.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AdministradorEcommerceFacade {
    public AdministradorEcommerceFacade() {};

    public Administrador login(String email, String senha){
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            return em.createQuery("SELECT a FROM Administrador a WHERE a.email = :email AND a.senha = :senha", Administrador.class).setParameter("email", email).setParameter("senha", senha).getSingleResult();
        } catch (Exception e) {
            System.out.println("Login inválido.");
            return null;
        } finally {
            em.close();
        }
    }

    public List<ProdutoComponent> listarProdutos() {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            return em.createQuery("SELECT p FROM ProdutoComponent p", ProdutoComponent.class).getResultList();
        } finally {
            em.close();
        }
    }

    public ProdutoComponent getProduto(Long id) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            ProdutoComponent produto = em.find(ProdutoComponent.class, id);
            if (produto != null) {
                return produto;
            } else {
                System.out.println("Produto não encontrado.");
                return null;
            }
        } finally {
            em.close();
        }
    }

    public void adicionarProduto(ProdutoComponent produto) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            em.merge(produto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void removerProduto(Long id) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            ProdutoComponent produto = em.find(ProdutoComponent.class, id);
            if (produto != null) em.remove(produto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void atualizarEstoque(Long id, int novoEstoque) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            ProdutoLeaf leaf = em.find(ProdutoLeaf.class, id);
            int estoqueAntigo = leaf.getEstoque();
            leaf.setEstoque(estoqueAntigo + novoEstoque);
            if (estoqueAntigo == 0 && leaf.getEstoque() > 0) {
                leaf.notificarObservers(em);
                List<ComboProdutoComposite> combos = em.createQuery("SELECT c FROM ComboProdutoComposite c JOIN c.produtos p WHERE p.id = :id", ComboProdutoComposite.class).setParameter("id", id).getResultList();
                for (ComboProdutoComposite combo : combos) {
                    if (combo.getEstoque() > 0) {
                        combo.notificarObservers(em);
                    }
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Pedido> visualizarPedidos() {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void gerarRelatorio() {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            Double faturamento = em.createQuery("SELECT SUM(p.total) FROM Pedido p", Double.class).getSingleResult();
            Long totalVendas = em.createQuery("SELECT COUNT(p) FROM Pedido p", Long.class).getSingleResult();

            System.out.println("======= Relatório de vendas =======");
            System.out.println("Total de vendas: " + totalVendas);
            System.out.printf("Faturamento total: R$ %.2f\n", (faturamento != null ? faturamento : 0.0));
            System.out.println("===================================");
         } finally {
            em.close();
        }
    }
}
