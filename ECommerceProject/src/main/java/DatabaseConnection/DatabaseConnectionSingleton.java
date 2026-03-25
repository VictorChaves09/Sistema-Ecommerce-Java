package DatabaseConnection;

import jakarta.persistence.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectionSingleton {
    private static EntityManagerFactory databaseConnection;

    static {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("jakarta.persistence").setLevel(Level.SEVERE);
        try {
            databaseConnection = Persistence.createEntityManagerFactory("My_PU");
        } catch (Throwable ex) {
            System.err.println("Falha ao criar o EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getDatabaseConnection() {
        return databaseConnection.createEntityManager();
    }

    public static void fecharFactory() {
        if (databaseConnection != null && databaseConnection.isOpen()) {
            databaseConnection.close();
        }
    }
}
