package tn.iit.entity;

public enum Priorite {
    FAIBLE("Faible"),
    NORMALE("Normale"), 
    URGENTE("Urgente"),
    TRES_URGENTE("Très urgente");
    
    private final String libelle;
    
    Priorite(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}