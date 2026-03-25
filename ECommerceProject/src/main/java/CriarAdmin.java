import Atores.Administrador;
import DatabaseConnection.DatabaseConnectionSingleton;
import jakarta.persistence.EntityManager;

public class CriarAdmin {
    public static void main(String[] args) {
        EntityManager em = DatabaseConnectionSingleton.getDatabaseConnection();
        try {
            em.getTransaction().begin();
            Administrador administrador = new Administrador("Admin", "admin@example.com", "admin");
            em.persist(administrador);
            em.getTransaction().commit();
            System.out.println("Administrador criado com sucesso no banco 'ecommerce'!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
