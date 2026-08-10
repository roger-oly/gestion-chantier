export interface Document {
  idDocument: number;
  nom: string;
  type: string;
  cheminFichier: string;
  dateUpload: string;
  idChantier: number;
  nomChantier: string;
  idUtilisateur: number;
  nomUtilisateur: string;
}