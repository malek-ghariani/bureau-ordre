export interface CourrierSortant {
  id?: number;
  numeroOrdre?: string;
  dateEmission: string;
  dateSaisie?: string;
  typeDocument?: string;
  reference?: string;
  nature?: string;
  modeExpedition?: string;
  statut?: string;
  etat?: string;
  priorite?: string;
  dateArchivage?: string;
  dateExpedition?: string;
  destinataireId?: number;
  nomDestinataire?: string;
}