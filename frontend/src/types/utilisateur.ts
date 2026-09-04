export interface Role {
  idRole: number;
  libelle: string;
}

export interface Utilisateur {
  idUtilisateur: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  statut: string;
  role: Role;
}