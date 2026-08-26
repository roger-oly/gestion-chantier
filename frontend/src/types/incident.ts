export interface Incident {
  idIncident: number;
  type: string;
  description: string;
  gravite: string;
  dateIncident: string;
  dateModification: string;
  statut: string;
  idChantier: number;
  idUtilisateur: number;
}