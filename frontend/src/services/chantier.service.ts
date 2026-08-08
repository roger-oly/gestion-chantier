import api from "./api";

import type { Chantier, ChantierRequest } from "../types/chantier";


export async function getChantiers(): Promise<Chantier[]> {

  const response = await api.get("/chantiers");

  return response.data;

}

export async function getChantierById(
    id:number
){

    const response =
        await api.get<Chantier>(
            `/chantiers/${id}`
        );

    return response.data;

}



export async function createChantier(
  chantier: ChantierRequest
) {
  const response = await api.post<Chantier>(
    "/chantiers",
    chantier
  );

  return response.data;
}



export async function updateChantier(
  id: number,
  chantier: ChantierRequest
) {
  const response = await api.put<Chantier>(
    `/chantiers/${id}`,
    chantier
  );

  return response.data;
}



export async function deleteChantier(
    id:number
){

    await api.delete(
        `/chantiers/${id}`
    );

}