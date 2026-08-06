import api from "./api";

import type { Chantier } from "../types/chantier";


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
    chantier:any
){

    const response =
        await api.post(
            "/chantiers",
            chantier
        );

    return response.data;

}



export async function updateChantier(
    id:number,
    chantier:any
){

    const response =
        await api.put(
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