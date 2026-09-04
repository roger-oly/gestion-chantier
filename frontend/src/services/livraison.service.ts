import api from "./api";

export interface Livraison {
  idLivraison: number;
  description: string;
  dateLivraison: string;
  statut: string;
  idChantier: number;
  idUtilisateur: number;
}

/**
 * Récupère toutes les livraisons.
 */
export const getLivraisons = async (): Promise<Livraison[]> => {
  const response = await api.get<Livraison[]>("/livraisons");

  return response.data;
};

/**
 * Récupère une livraison par son identifiant.
 */
export const getLivraisonById = async (
  id: number
): Promise<Livraison> => {
  const response = await api.get<Livraison>(`/livraisons/${id}`);

  return response.data;
};

/**
 * Récupère les livraisons d'un chantier.
 */
export const getLivraisonsByChantier = async (
  idChantier: number
): Promise<Livraison[]> => {
  const response = await api.get<Livraison[]>(
    `/livraisons/chantier/${idChantier}`
  );

  return response.data;
};

/**
 * Crée une livraison.
 */
export const createLivraison = async (
  livraison: {
    description: string;
    dateLivraison: string;
    statut: string;
    idChantier: number;
    idUtilisateur: number;
  }
): Promise<Livraison> => {
  const response = await api.post<Livraison>(
    "/livraisons",
    livraison
  );

  return response.data;
};

/**
 * Modifie une livraison.
 */
export const updateLivraison = async (
  id: number,
  livraison: {
    description: string;
    dateLivraison: string;
    statut: string;
    idChantier: number;
    idUtilisateur: number;
  }
): Promise<Livraison> => {
  const response = await api.put<Livraison>(
    `/livraisons/${id}`,
    livraison
  );

  return response.data;
};

/**
 * Supprime une livraison.
 */
export const deleteLivraison = async (
  id: number
): Promise<void> => {
  await api.delete(`/livraisons/${id}`);
};