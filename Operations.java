import java.io.*;
import java.util.Scanner;
import java.util.HashMap;

class Operations {
    private String fichier;

    public Operations(String fichier) {
        this.fichier = fichier;
    }

    public void ajouterCompte(CompteBancaire compte) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, true))) {
            writer.write(compte.toString());
            writer.newLine();
        }
    }

    public void supprimerCompte(String numeroCompte) throws IOException {
        File inputFile = new File(fichier);
        File tempFile = new File("tempFile.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (!ligne.contains("Numéro de compte: " + numeroCompte)) {
                    writer.write(ligne);
                    writer.newLine();
                }
            }
        }

        if (!inputFile.delete()) {
            System.out.println("Erreur lors de la suppression du fichier original.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Erreur lors du renommage du fichier temporaire.");
        }
    }

    public void modifierCompte(String numeroCompte, CompteBancaire compteModifie) throws IOException {
        File inputFile = new File(fichier);
        File tempFile = new File("tempFile.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.contains("Numéro de compte: " + numeroCompte)) {
                    writer.write(compteModifie.toString());
                } else {
                    writer.write(ligne);
                }
                writer.newLine();
            }
        }

        if (!inputFile.delete()) {
            System.out.println("Erreur lors de la suppression du fichier original.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Erreur lors du renommage du fichier temporaire.");
        }
    }

    public CompteBancaire rechercherCompte(String titulaire) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.contains("Titulaire: " + titulaire)) {
                    return parseCompte(ligne);
                }
            }
        }
        return null;
    }

    public void listerComptesParLettre(char lettre) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.startsWith("Titulaire: " + lettre)) {
                    System.out.println(ligne);
                }
            }
        }
    }

    public void voirNombreComptes() throws IOException {
        int nbCompteCourant = 0, nbCompteEpargne = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.contains("CompteCourant")) {
                    nbCompteCourant++;
                } else if (ligne.contains("CompteEpargne")) {
                    nbCompteEpargne++;
                }
            }
        }
        System.out.println("Nombre de comptes courants: " + nbCompteCourant);
        System.out.println("Nombre de comptes épargne: " + nbCompteEpargne);
    }

    public void voirComptes(Class<?> type) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if ((type == CompteCourant.class && ligne.contains("CompteCourant")) ||
                        (type == CompteEpargne.class && ligne.contains("CompteEpargne"))) {
                    System.out.println(ligne);
                }
            }
        }
    }

    public CompteBancaire voirDetailsCompte(String numeroCompte) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.contains("Numéro de compte: " + numeroCompte)) {
                    return parseCompte(ligne);
                }
            }
        }
        return null;
    }

    public void transfererArgent(String numeroCompteSource, String numeroCompteDestination, double montant) throws IOException, TransferException {
        CompteBancaire source = voirDetailsCompte(numeroCompteSource);
        CompteBancaire destination = voirDetailsCompte(numeroCompteDestination);
        if (source != null && destination != null && source.getSolde() >= montant) {
            source.retirer(montant);
            destination.deposer(montant);
            modifierCompte(numeroCompteSource, source);
            modifierCompte(numeroCompteDestination, destination);
            System.out.println("Transfert réussi.");
        } else {
            throw new TransferException("Transfert échoué: vérifiez les comptes et le solde.");
        }
    }

    public void voirSoldeTotal() throws IOException {
        double soldeTotal = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                CompteBancaire compte = parseCompte(ligne);
                soldeTotal += compte.getSolde();
            }
        }
        System.out.println("Solde total de tous les comptes: " + soldeTotal);
    }

    private CompteBancaire parseCompte(String ligne) {
        String[] donnees = ligne.split(", ");
        String titulaire = donnees[0].split(": ")[1];
        String numeroCompte = donnees[1].split(": ")[1];
        double solde = Double.parseDouble(donnees[2].split(": ")[1]);
        if (donnees.length == 4) { // Compte d'épargne avec taux d'intérêt
            double tauxInteret = Double.parseDouble(donnees[3].split(": ")[1]);
            return new CompteEpargne(titulaire, numeroCompte, solde, tauxInteret);
        } else { // Compte courant
            return new CompteCourant(titulaire, numeroCompte, solde);
        }
    }
}

class TransferException extends Exception {
    public TransferException(String message) {
        super(message);
    }
}
