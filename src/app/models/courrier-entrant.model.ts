export interface CourrierEntrant {
  id?: number;
  numeroOrdre?: string;
  dateReception: string;
  dateSaisie?: string;
  typeDocument?: string;
  reference?: string;
  nature?: string;
  modeReception: string;
  statut?: string;
  etat?: string;
  priorite?: string;
  dateArchivage?: string;
  expediteurId?: number;
  nomExpediteur?: string;
}