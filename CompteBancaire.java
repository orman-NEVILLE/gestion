class CompteBancaire {
    protected String titulaire;
    protected String numeroCompte;
    protected double solde;

    public CompteBancaire(String titulaire, String numeroCompte, double solde) {
        this.titulaire = titulaire;
        this.numeroCompte = numeroCompte;
        this.solde = solde;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void deposer(double montant) {
        solde += montant;
    }

    public void retirer(double montant) {
        if (montant <= solde) {
            solde -= montant;
        } else {
            System.out.println("Fonds insuffisants.");
        }
    }

    @Override
    public String toString() {
        return "Titulaire: " + titulaire + ", Numéro de compte: " + numeroCompte + ", Solde: " + solde;
    }
}