package diginamic.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Représente une transaction effectuée sur un compte donné.
 */
@Entity
public class Transaction {
	
	/** identifiant unique */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/** compte */
	@ManyToOne
	@JoinColumn(name="id_cpt")
	private Compte compte;
	
	/** montant */
	private double montant;
	
	/** message */
	private String message;
	
	/** statut de la transaction */
	@Enumerated(EnumType.STRING)
	private Statut statut;
	
	/**
     * Constructeur pour JPA
     */
	public Transaction() {
		
	}

	/** Constructeur
	 * @param compte compte
	 * @param montant montant 
	 * @param message message
	 * @param statut statut
	 */
	public Transaction(Compte compte, double montant, String message, Statut statut) {
		super();
		this.compte = compte;
		this.montant = montant;
		this.message = message;
		this.statut = statut;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the compte
	 */
	public Compte getCompte() {
		return compte;
	}

	/**
	 * @param compte the compte to set
	 */
	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	/**
	 * @return the montant
	 */
	public double getMontant() {
		return montant;
	}

	/**
	 * @param montant the montant to set
	 */
	public void setMontant(double montant) {
		this.montant = montant;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
