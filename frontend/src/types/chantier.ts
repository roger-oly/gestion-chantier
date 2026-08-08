export interface Chantier {
  idChantier: number;
  nom: string;
  description: string;
  localisation: string;
  budget: number;
  dateDebut: string;
  dateFinPrevue: string;
  statut: string;
  idUtilisateur: number;
  nomUtilisateur: string;

}

export interface ChantierRequest {
  nom: string;
  description: string;
  localisation: string;
  budget: number;
  dateDebut: string;
  dateFinPrevue: string;
  statut: string;
  idUtilisateur: number;
}