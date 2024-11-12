package diginamic;

import java.util.List;

import diginamic.entites.Compte;
import diginamic.entites.Statut;

/**
 * Classe de service bancaire
 */
public class BanqueService {
	
    /**
     * Effectue les opérations bancaires
     */
    private TransactionProcessor transactionProcessor;
    
    /**
     * Stocke les transactions en base de données
     */
    private TransactionDao transactionDao; 

    /** Dépose un montant sur un compte
     * @param compte compte à créditer
     * @param montant montant
     */
    public void deposer(Compte compte, double montant) {
    	if (transactionProcessor.effectuerDepot(compte, montant)) {
    		transactionDao.enregistrer(compte, montant, "Dépôt de "+montant+"€", Statut.SUCCES);
    	}
    	else {
    		traiterErreur(compte, transactionProcessor.getErrors());
    	}
    }

    /** Effectue un retrait sur un compte
     * @param compte compte à débiter
     * @param montant montant
     */
    public void retirer(Compte compte, double montant) {
        if (transactionProcessor.effectuerRetrait(compte, montant)) {
            transactionDao.enregistrer(compte, montant, "Retrait de "+montant+"€", Statut.SUCCES);
        } 
        else {
    		traiterErreur(compte, transactionProcessor.getErrors());
    	}
    }

    /** Effectue un virement d'un compte vers un bénéficiaire
     * @param compteOri compte depuis lequel le virement s'effectue
     * @param beneficiaire compte bénéficiaire du virement
     * @param montant montant
     */
    public void virer(Compte compteOri, Compte beneficiaire, double montant) {

        if (transactionProcessor.effectuerVirement(compteOri, beneficiaire, montant)) {
            transactionDao.enregistrer(compteOri, montant, "Virement vers le compte bénéficiaire "+beneficiaire.getNumero(), Statut.SUCCES);
            transactionDao.enregistrer(beneficiaire, montant, "Virement reçu du compte "+compteOri.getNumero(), Statut.SUCCES);
        } 
        else {
    		traiterErreur(compteOri, transactionProcessor.getErrors());
    	}
    }

	/** Traite les transactions en erreur
	 * @param compte compte
	 * @param errors liste des erreurs constatées
	 */
	private void traiterErreur(Compte compte, List<String> errors) {
		
		for (String error : errors) {
			transactionDao.enregistrer(compte, 0.0, error, Statut.ECHEC);
		}
	}

	/**
	 * @return the transactionProcessor
	 */
	public TransactionProcessor getTransactionProcessor() {
		return transactionProcessor;
	}

	/**
	 * @param transactionProcessor the transactionProcessor to set
	 */
	public void setTransactionProcessor(TransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}

	/**
	 * @return the notificationService
	 */
	public TransactionDao getNotificationService() {
		return transactionDao;
	}

	/**
	 * @param notificationService the notificationService to set
	 */
	public void setNotificationService(TransactionDao notificationService) {
		this.transactionDao = notificationService;
	}
    
}