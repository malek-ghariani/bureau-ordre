package tn.iit.entity;

public enum StatutPlanification {
	EN_ATTENTE,   // courrier envoyé au destinataire
    LU,       // courrier ouvert
    TRAITE,   // destinataire a traité le courrier
    REPONDU   // une réponse a été envoyée à l’expéditeur
}
