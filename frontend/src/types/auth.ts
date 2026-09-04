export interface AuthRole {
  idRole: number;
  libelle: string;
}

export interface AuthUser {
  idUtilisateur: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string | null;
  statut: string;
  role: AuthRole;
}