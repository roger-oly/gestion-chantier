import api from "./api";

import type { Utilisateur } from "../types/utilisateur";

export interface UtilisateurRequest {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
  telephone: string;
  statut: string;
  role: {
    idRole: number;
  };
}

/**
 * Récupère tous les utilisateurs.
 */
export const getUtilisateurs = async (): Promise<Utilisateur[]> => {
  const response = await api.get<Utilisateur[]>("/utilisateurs");

  return response.data;
};

/**
 * Crée un utilisateur.
 */
export const createUtilisateur = async (
  utilisateur: UtilisateurRequest
): Promise<Utilisateur> => {
  const response = await api.post<Utilisateur>(
    "/utilisateurs",
    utilisateur
  );

  return response.data;
};

/**
 * Récupère un utilisateur par son identifiant.
 */
export const getUtilisateurById = async (
  id: number
): Promise<Utilisateur> => {
  const response = await api.get<Utilisateur>(
    `/utilisateurs/${id}`
  );

  return response.data;
};

/**
 * Modifie un utilisateur.
 */
export const updateUtilisateur = async (
  id: number,
  utilisateur: UtilisateurRequest
): Promise<Utilisateur> => {
  const response = await api.put<Utilisateur>(
    `/utilisateurs/${id}`,
    utilisateur
  );

  return response.data;
};

/**
 * Supprime un utilisateur.
 */
export const deleteUtilisateur = async (
  id: number
): Promise<void> => {
  await api.delete(`/utilisateurs/${id}`);
};