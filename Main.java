import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fichier ="Compte.txt";
        Operations banque = new Operations(fichier);

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Ajouter un compte bancaire.\n2. Supprimer un compte bancaire.\n3. Modifier un compte bancaire" +
                    "\n4. Rechercher un compte bancaire par nom de titulaire" +
                    "\n5. Lister les comptes bancaires par lettre.\n6. Afficher le nombre de comptes bancaires par type." +
                    "\n7. Afficher les comptes par type\n8. Afficher les détails d'un compte.\n9. Transférer de l'argent entre comptes" +
                    "\n10. Afficher le solde total de tous les comptes\n11. Quitter");

            System.out.print("Choisissez une option: ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choix) {
                    case 1:
                        System.out.print("Type de compte (courant/epargne): ");
                        String type = scanner.nextLine();
                        System.out.print("Titulaire: ");
                        String titulaire = scanner.nextLine();
                        System.out.print("Numéro de compte: ");
                        String numero = scanner.nextLine();
                        System.out.print("Solde initial: ");
                        double solde = scanner.nextDouble();
                        scanner.nextLine();

                        if (type.equalsIgnoreCase("courant")) {
                            CompteCourant compteCourant1 = new CompteCourant(titulaire, numero, solde);
                            banque.ajouterCompte(compteCourant1);
                        } else if (type.equalsIgnoreCase("epargne")) {
                            System.out.print("Taux d'intérêt: ");
                            double tauxInteret = scanner.nextDouble();
                            scanner.nextLine();  // Consommer le retour à la ligne
                            CompteEpargne compteEpargne1 = new CompteEpargne(titulaire, numero, solde, tauxInteret);
                            banque.ajouterCompte(compteEpargne1);
                        } else {
                            System.out.println("Type de compte invalide.");
                        }
                        break;
                    case 2:
                        System.out.print("Numéro de compte à supprimer: ");
                        String numeroSupprimer = scanner.nextLine();
                        banque.supprimerCompte(numeroSupprimer);
                        System.out.println("Compte supprimé.");
                        break;
                    case 3:
                        System.out.print("Numéro de compte à modifier: ");
                        String numeroModifier = scanner.nextLine();
                        System.out.print("Nouveau titulaire: ");
                        String nouveauTitulaire = scanner.nextLine();
                        System.out.print("Nouveau solde: ");
                        double nouveauSolde = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Type de compte (courant/epargne): ");
                        String nouveauType = scanner.nextLine();
                        if (nouveauType.equalsIgnoreCase("courant")) {
                            CompteCourant compteCourant2 = new CompteCourant(nouveauTitulaire, numeroModifier, nouveauSolde);
                            banque.modifierCompte(numeroModifier, compteCourant2);
                        } else if (nouveauType.equalsIgnoreCase("epargne")) {
                            System.out.print("Nouveau taux d'intérêt: ");
                            double nouveauTauxInteret = scanner.nextDouble();
                            scanner.nextLine();
                            CompteEpargne compteEpargne2 = new CompteEpargne(nouveauTitulaire, numeroModifier, nouveauSolde, nouveauTauxInteret);
                            banque.modifierCompte(numeroModifier, compteEpargne2);
                        } else {
                            System.out.println("Type de compte invalide.");
                        }
                        break;
                    case 4:
                        System.out.print("Nom du titulaire: ");
                        String titulaireRecherche = scanner.nextLine();
                        CompteBancaire compteRecherche = banque.rechercherCompte(titulaireRecherche);
                        if (compteRecherche != null) {
                            System.out.println(compteRecherche);
                        } else {
                            System.out.println("Compte non trouvé.");
                        }
                        break;
                    case 5:
                        System.out.print("Lettre: ");
                        char lettre = scanner.nextLine().charAt(0);
                        banque.listerComptesParLettre(lettre);
                        break;
                    case 6:
                        banque.voirNombreComptes();
                        break;
                    case 7:
                        System.out.print("Type de compte à afficher (courant/epargne): ");
                        String typeAffichage = scanner.nextLine();
                        if (typeAffichage.equalsIgnoreCase("courant")) {
                            banque.voirComptes(CompteCourant.class);
                        } else if (typeAffichage.equalsIgnoreCase("epargne")) {
                            banque.voirComptes(CompteEpargne.class);
                        } else {
                            System.out.println("Type de compte invalide.");
                        }
                        break;
                    case 8:
                        System.out.print("Numéro de compte: ");
                        String numeroDetails = scanner.nextLine();
                        CompteBancaire compteDetails = banque.voirDetailsCompte(numeroDetails);
                        if (compteDetails != null) {
                            System.out.println(compteDetails);
                        } else {
                            System.out.println("Compte non trouvé.");
                        }
                        break;
                    case 9:
                        System.out.print("Numéro de compte source: ");
                        String compteSource = scanner.nextLine();
                        System.out.print("Numéro de compte destination: ");
                        String compteDestination = scanner.nextLine();
                        System.out.print("Montant à transférer: ");
                        double montant = scanner.nextDouble();
                        scanner.nextLine();
                        banque.transfererArgent(compteSource, compteDestination, montant);
                        break;
                    case 10:
                        banque.voirSoldeTotal();
                        break;
                    case 11:
                        System.out.println("Au revoir!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Option invalide.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Erreur: " + e.getMessage());
            } catch (TransferException e) {
                System.out.println("Erreur de transfert: " + e.getMessage());
            }
        }
    }
}
