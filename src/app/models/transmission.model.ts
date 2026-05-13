export interface Transmission {
  id: number;
  message: string;
  dateEnvoi: string;
  dateLecture?: string;

  expediteurId?: number;
  destinataireId?: number;

  type?: string;
}