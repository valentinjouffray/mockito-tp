package diginamic;

import diginamic.entites.Compte;
import diginamic.entites.Statut;
import diginamic.entites.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * Stocke les transactions effectuées en base de données
 */
public class TransactionDao extends AbstractDao {
	
	/** Entity manager */
	private EntityManager em = emf.createEntityManager();

	/** Stocke une transaction en base de données pour le compte.
	 * @param compte compte
	 * @param montant montant
	 * @param message message
	 */
	public void enregistrer(Compte compte, double montant, String message, Statut statut) {
		EntityTransaction entityTransaction = em.getTransaction();

		Transaction transaction = new Transaction(compte, montant, message, statut);
		em.persist(transaction);

		entityTransaction.commit();
	}

}
