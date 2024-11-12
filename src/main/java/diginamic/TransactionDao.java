package diginamic;

import diginamic.entites.Compte;
import diginamic.entites.Statut;
import diginamic.entites.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Stocke les transactions effectuées en base de données
 */
public class TransactionDao {
	
	/**
	 * Entity manager factory
	 */
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("banque");
	
	/**
	 * Entity managet
	 */
	private EntityManager em = emf.createEntityManager();

	/** Stocke une transaction en base de données pour le compte.
	 * @param compte compte
	 * @param montant montant
	 * @param message message
	 */
	public int enregistrer(Compte compte, double montant, String message, Statut statut) {
		Transaction transaction = new Transaction(compte, montant, message, statut);
		em.persist(transaction);
		return transaction.getId();
	}

}
