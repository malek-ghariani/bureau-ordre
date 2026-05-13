package tn.iit.entity;

public enum TypeTiers {
    PERSONNE("Personne physique"),
    ENTREPRISE("Entreprise"),
    ADMINISTRATION("Administration"),
    ASSOCIATION("Association"),
    AUTRE("Autre");
    
    private final String libelle;
    
    TypeTiers(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}