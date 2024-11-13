package diginamic;

import diginamic.entites.Compte;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Stocke les comptes en base de données
 */
public class CompteDao extends AbstractDao {

    /** Entity manager */
    private EntityManager em = emf.createEntityManager();

    /**
     * Recherche un compte par son numéro
     * @param numero numero de compte
     * @return Compte
     */
    public Compte findByNumero(String numero){
        return em.find(Compte.class, numero);
    }

    /**
     * Stocke un nouveau compte en base de données
     * @param compte nouveau compte
     */
    public void enregistrer(Compte compte) {
        EntityTransaction transaction = em.getTransaction();
        em.persist(compte);
        transaction.commit();
    }

    /**
     * Met à jour les données d'un compte en base de données à l'exception de son numéro
     * @param compte compte
     */
    public void mettreAJour(Compte compte) {
        EntityTransaction transaction = em.getTransaction();

        Compte compteDb = findByNumero(compte.getNumero());
        compteDb.setSolde(compte.getSolde());
        compteDb.setEmail(compte.getEmail());

        transaction.commit();
    }

}
