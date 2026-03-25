package Facade;

import Atores.Cliente;
import Carrinho.*;
import DatabaseConnection.DatabaseConnectionSingleton;
import Pagamento.ProcessadorPagamento;
import Pedido.Pedido;
import Produto.ProdutoComponent;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class ClienteEcommerceFacade {
    public ClienteEcommerceFacade(){};

    public void criarConta(String nome, String email, String senha){
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            Long contagem = em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class).setParameter("email", email).getSingleResult();

            if (contagem > 0) {
                System.out.println("Erro: o email " + email + " já está cadastrado.");
                return;
            }

            em.getTransaction().begin();
            Cliente cliente = new Cliente(nome, email, senha);
            em.persist(cliente);
            em.getTransaction().commit();

            System.out.println("Conta criada com sucesso.");
        } finally {
            em.close();
        }
    }

    public Cliente login(String email, String senha){
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email AND c.senha = :senha", Cliente.class).setParameter("email", email).setParameter("senha", senha).getSingleResult();
        } catch (Exception e) {
            System.out.println("Login inválido.");
            return null;
        } finally {
            em.close();
        }
    }

    public List<ProdutoComponent> visualizarProdutos(){
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

    public void acompanharProduto(Cliente cliente, Long produtoId) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            ProdutoComponent produto = em.find(ProdutoComponent.class, produtoId);
            Cliente clienteManaged = em.find(Cliente.class, cliente.getId());

            if (produto != null && clienteManaged != null) {
                produto.adicionarObserver(clienteManaged);
                em.merge(produto);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void finalizarCompra(Cliente cliente, String tipoPagamento) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            Carrinho carrinho = cliente.getCarrinho();

            if (carrinho.getItens().isEmpty()) {
                System.out.println("Carrinho vazio.");
                return;
            }

            double total = carrinho.calcularTotal();

            ProcessadorPagamento processador = new ProcessadorPagamento(tipoPagamento);
            processador.processarPagamento(total);

            List<ItemCarrinho> itensParaPedido = new ArrayList<>();
            for (ItemCarrinho item : carrinho.getItens()) {
                ItemCarrinho copiaParaPedido = new ItemCarrinho(item.getProduto(), item.getQuantidade());
                itensParaPedido.add(copiaParaPedido);
                ProdutoComponent produto = em.find(ProdutoComponent.class, item.getProduto().getId());
                produto.baixarEstoque(item.getQuantidade());
            }

            Pedido pedido = new Pedido(cliente, itensParaPedido, total);
            em.persist(pedido);

            cliente.adicionarPedido(pedido);
            carrinho.limparCarrinho();

            em.getTransaction().commit();

            cliente.getCarrinho().getItens().clear();
            System.out.println("Compra finalizada com sucesso.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void atualizarCliente(Cliente cliente) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
