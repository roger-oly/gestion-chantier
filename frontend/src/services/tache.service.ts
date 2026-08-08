import api from "./api";

import type { Tache } from "../types/tache";


export interface TacheRequest {

  titre: string;

  description: string;

  statut: string;

  niveauPriorite: string;

  idChantier: number;

}


export async function getTaches(): Promise<Tache[]> {

  const response = await api.get<Tache[]>("/taches");

  return response.data;

}

export async function getTacheById(
  id: number
): Promise<Tache> {

  const response = await api.get<Tache>(
    `/taches/${id}`
  );

  return response.data;
}


export async function createTache(
  tache: TacheRequest
): Promise<Tache> {

  const response = await api.post<Tache>(
    "/taches",
    tache
  );

  return response.data;

}


export async function updateTache(
  id: number,
  tache: TacheRequest
): Promise<Tache> {

  const response = await api.put<Tache>(
    `/taches/${id}`,
    tache
  );

  return response.data;

}


export async function deleteTache(
  id: number
): Promise<void> {

  await api.delete(`/taches/${id}`);

}

export async function getTachesByChantier(
  idChantier: number
): Promise<Tache[]> {

  const response = await api.get(
    `/taches/chantier/${idChantier}`
  );

  return response.data;
}