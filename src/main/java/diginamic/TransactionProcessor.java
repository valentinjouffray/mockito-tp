package diginamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import diginamic.entites.Compte;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Effectue les transactions bancaires
 */
public class TransactionProcessor {
    
	/**
	 * Liste des erreurs
	 */
	private List<String> errors = new ArrayList<>();

    /** Effectue un retrait sur un compte et envoi un mail de confirmation au détenteur du compte.<br>
     * La méthode retourne true si la transaction a pu s'effectuer, sinon retourne false.
     * @param compte compte
     * @param montant montant du retrait
     * @return boolean
     */
    public boolean effectuerRetrait(Compte compte, double montant) {

        if (compte.getSolde() < montant) {
        	errors.add("Retrait refusé: solde insuffisant.");
            return false;
        }

        compte.debiter(montant);
        envoyerMailConfirmation(compte, "Retrait de votre compte d'un montant de "+montant+"€");
        return true;
    }
    
    /** Effectue un dépot sur un compte et envoi un mail de confirmation au détenteur du compte.<br>
     * La méthode retourne true si la transaction a pu s'effectuer, sinon retourne false.
     * @param compte compte
     * @param montant montant du retrait
     * @return boolean
     */
    public boolean effectuerDepot(Compte compte, double montant) {

        compte.crediter(montant);
        envoyerMailConfirmation(compte, "Dépôt sur votre compte d'un montant de "+montant+"€");
        return true;
    }

    /** Effectue un virement depuis un compte vers un autre compte puis envoie un mail de confirmation aux détenteurs des comptes.<br>
     * La méthode retourne true si la transaction a pu s'effectuer, sinon retourne false.
     * @param compteOri compte à débiter
     * @param compteDest compte à créditer
     * @param montant montant du virement
     * @return boolean
     */
	public boolean effectuerVirement(Compte compteOri, Compte compteDest, double montant) {
        double coutVirement = montant * 0.01;
        double montantTotal = montant + coutVirement;

        if (compteOri.getSolde() < montantTotal) {
        	errors.add("Transfert refusé: solde insuffisant.");
            return false;
        }

        compteOri.debiter(montantTotal);
        compteDest.crediter(montant);
        envoyerMailConfirmation(compteOri, "Virement depuis votre compte d'un montant de "+montantTotal+"€");
        envoyerMailConfirmation(compteDest, "Virement vers le compte "+compteDest.getNumero()+" d'un montant de "+montant+"€");
        return true;
    }
    
    /** Envoie un email au détenteur du compte passé en paramètre avec le texte passé en paramètre
     * @param compte compte
     * @param texte message
     */
    public void envoyerMailConfirmation(Compte compte, String texte) {
    	// Paramètres de configuration SMTP
        String host = "smtp.gmail.com"; // Serveur SMTP
        final String user = "operations@banque.com"; // Votre e-mail
        final String password = "votre-mot-de-passe"; // Mot de passe de votre e-mail

        // Informations du destinataire
        String to = compte.getEmail();

        // Propriétés de la session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Création de la session avec authentification
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        try {
            // Création de l’objet e-mail
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Opération bancaire sur votre compte "+compte.getNumero());
            msg.setText(texte);

            // Envoi de l’e-mail
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}
}