class CompteEpargne extends CompteBancaire {
    private double tauxInteret;

    public CompteEpargne(String titulaire, String numeroCompte, double solde, double tauxInteret) {
        super(titulaire, numeroCompte, solde);
        this.tauxInteret = tauxInteret;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void appliquerInteret() {
        solde += solde * tauxInteret;
    }

    @Override
    public String toString() {
        return super.toString() + ", Taux d'intérêt: " + tauxInteret;
    }
}