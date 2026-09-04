import api from "./api";

export interface Role {
  idRole: number;
  libelle: string;
}

/**
 * Récupère tous les rôles.
 */
export const getRoles = async (): Promise<Role[]> => {
  const response = await api.get<Role[]>("/roles");

  return response.data;
};