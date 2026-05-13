package tn.iit.entity;

public enum ModeExpedition {
	 EMAIL("Email"),
	    FAX("Fax"),
	    MESSAGERIE("Messagerie"),
	    LIVRAISON_MAIN_PROPRE("Livraison en main propre"),
	    PLATEFORME_NUMERIQUE("Plateforme numérique"),
	    AUTRE("Autre");
    
    private final String libelle;
    
    ModeExpedition(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}