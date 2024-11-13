package diginamic;

import diginamic.entites.Compte;
import diginamic.exception.BanqueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BanqueServiceTest {

    public static CompteDao compteDao;
    public static TransactionDao transactionDao;
    public static TransactionProcessor transactionProcessor;
    public static BanqueService banqueService;

    @BeforeEach
    void setUp() {
        compteDao = Mockito.mock(CompteDao.class);
        transactionDao = Mockito.mock(TransactionDao.class);
        transactionProcessor = Mockito.spy(TransactionProcessor.class);

        banqueService = new BanqueService();
        banqueService.setCompteDao(compteDao);
        banqueService.setTransactionDao(transactionDao);
        banqueService.setTransactionProcessor(transactionProcessor);
    }

    @Test
    void creerCompte() {
        String numero1 = "123";
        double solde1 = 100.0;
        String mail1 = "mail1@test.com";
        Mockito.when(compteDao.findByNumero(numero1)).thenReturn(null);
        Compte compte1 = null;
        try {
            compte1 = banqueService.creerCompte(numero1, solde1, mail1);
        } catch (BanqueException e) {
            fail();
        }
        Mockito.verify(compteDao).enregistrer(compte1);
        assertNotNull(compte1);

        String numero2 = "456";
        double solde2 = 200.0;
        String mail2 = "mail2@test.com";
        Mockito.when(compteDao.findByNumero(numero2)).thenReturn(new Compte(numero2, mail2, solde2));
        assertThrows(BanqueException.class, () -> banqueService.creerCompte(numero2, solde2, mail2));
    }

    @Test
    void deposer() {
        double montant1 = 100.0;
        Compte compte = new Compte("123", "mail1@test.com", 1000.0D);
        Mockito.when(compteDao.findByNumero("123")).thenReturn(compte);
        Mockito.doNothing().when(transactionProcessor).envoyerMailConfirmation(Mockito.any(Compte.class), Mockito.anyString());
        banqueService.deposer(compte, montant1);
        Mockito.verify(compteDao).mettreAJour(compte);
        assertEquals(1100.0, compte.getSolde());
    }

    @Test
    void retirerCompteSuffisant() {
        Compte compte = new Compte("123", "mail1@test.com", 1000.0D);
        double montant1 = 100.0;
        Mockito.doNothing().when(transactionProcessor).envoyerMailConfirmation(Mockito.any(Compte.class), Mockito.anyString());
        Mockito.when(compteDao.findByNumero("123")).thenReturn(compte);
        banqueService.retirer(compte, montant1);
        Mockito.verify(compteDao).mettreAJour(compte);
        assertEquals(900.0, compte.getSolde());
    }

    @Test
    void retirerCompteInsuffisant() {
        Compte compte = new Compte("123", "mail1@test.com", 1000.0D);
        double montant1 = 2000.0;
        Mockito.doNothing().when(transactionProcessor).envoyerMailConfirmation(Mockito.any(Compte.class), Mockito.anyString());
        Mockito.when(compteDao.findByNumero("123")).thenReturn(compte);
        banqueService.retirer(compte, montant1);
        Mockito.verify(transactionProcessor).getErrors();
        assertEquals(1000.0, compte.getSolde());
    }

    @Test
    void virerCompteSuffisant() {
        String compteNb1 = "123";
        String compteNb2 = "456";
        String mail1 = "mail1@test.com";
        String mail2 = "mail2@test.com";
        double virement = 100.0D;
        double coutVirement = virement * 0.01;
        Compte compte1 = new Compte(compteNb1, mail1, 1000.0);
        Compte compte2 = new Compte(compteNb2, mail2, 1000.0);
        Mockito.when(compteDao.findByNumero(compteNb1)).thenReturn(compte1);
        Mockito.when(compteDao.findByNumero(compteNb2)).thenReturn(compte2);
        Mockito.doNothing().when(transactionProcessor).envoyerMailConfirmation(Mockito.any(Compte.class), Mockito.anyString());
        banqueService.virer(compte1, compte2, virement);
        Mockito.verify(compteDao).mettreAJour(compte1);
        Mockito.verify(compteDao).mettreAJour(compte2);
        assertEquals(1000 - (virement + coutVirement), compte1.getSolde());
        assertEquals(1100.0, compte2.getSolde());
    }

    @Test
    void virerCompteInsuffisant() {
        String compteNb1 = "123";
        String compteNb2 = "456";
        String mail1 = "mail1@test.com";
        String mail2 = "mail2@test.com";
        double virement = 2000.0;
        Compte compte1 = new Compte(compteNb1, mail1, 1000.0);
        Compte compte2 = new Compte(compteNb2, mail2, 1000.0);
        Mockito.when(compteDao.findByNumero(compteNb1)).thenReturn(compte1);
        Mockito.when(compteDao.findByNumero(compteNb2)).thenReturn(compte2);
        Mockito.doNothing().when(transactionProcessor).envoyerMailConfirmation(Mockito.any(Compte.class), Mockito.anyString());
        banqueService.virer(compte1, compte2, virement);
        Mockito.verify(transactionProcessor).getErrors();
        assertEquals(1000.0, compte1.getSolde());
        assertEquals(1000.0, compte2.getSolde());
    }
}