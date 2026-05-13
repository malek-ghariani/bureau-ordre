export interface Planification {

  id?: number;

  // 👤 destinataire
  destinataireId?: number;
  destinataireNom?: string;

  // 📝 contenu
  message?: string;
  dateEcheance?: string;

  // 📌 statut / résultat (Enums côté backend → string côté Angular)
  statut?: 'EN_ATTENTE' | 'ENVOYE' | 'TERMINE' | string;
  resultat?: 'ACCEPTE' | 'REFUSE' | 'EN_COURS' | string;

  // 📦 type source
  typeSource?: 'COURRIER' | 'MANUEL' | string;

  // 📩 courrier lié
  courrierEntrantId?: number;
  courrierSortantId?: number;

  // 🔗 transmission origine
  transmissionId?: number;

  // 📎 pièces jointes
  piecesJointesIds?: number[];
  piecesJointesNoms?: string[];
}