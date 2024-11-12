package diginamic.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Représente un compte bancaire
 */
@Entity
public class Compte {
	
	/** Numéro du compte */
	@Id
	private String numero;
	
	/** solde */
    private double solde;
    
    /** email */
    private String email;
    
    /**
     * Constructeur pour JPA
     */
    public Compte() {
    	
    }

    /** Constructeur
     * @param numero numéro de compte
     * @param email email
     * @param soldeInitial solde initial
     */
    public Compte(String numero, String email, double soldeInitial) {
    	this.numero = numero;
    	this.email = email;
        this.solde = soldeInitial;
    }

    /** Débite le compte d'un montant donné si le solde le permet
     * @param montant somme à débiter
     * @return boolean
     */
    public boolean debiter(double montant) {
        if (montant > 0 && montant <= solde) {
            solde -= montant;
            return true;
        }
        return false; 
    }

	/** Dépôt d'une certaine somme sur le compte
	 * @param montant somme à déposer
	 */
	public void crediter(double montant) {
		this.solde+=montant;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the solde
	 */
	public double getSolde() {
		return solde;
	}
	
}