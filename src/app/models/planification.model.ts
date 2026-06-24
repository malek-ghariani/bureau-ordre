export interface Planification {
  id?: number;
  destinataireId?: number;
  destinataireNom?: string;
  transmissionMessage?: string;  // ← renomme message en transmissionMessage
  commentaireResultat?: string;  // ← ajoute ça
  dateEcheance?: string;
  statut?: 'EN_ATTENTE' | 'TRAITE' | string;
  resultat?: 'ACCEPTE' | 'REFUSE' | 'EN_COURS' | string;
  courrierEntrantId?: number;
  courrierSortantId?: number;
  transmissionId?: number;
  piecesJointesIds?: number[];
  piecesJointesNoms?: string[];
}