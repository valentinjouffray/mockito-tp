package diginamic;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AbstractDao {
    /**
     * Entity manager factory
     */
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("banque");
}
