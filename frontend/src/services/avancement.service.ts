import api from "./api";
import type { Avancement } from "../types/avancement";

export async function getAvancementsByChantier(
  idChantier: number
): Promise<Avancement[]> {
  const response = await api.get<Avancement[]>(
    `/avancements/chantier/${idChantier}`
  );

  return response.data;
}

export async function getPourcentageByChantier(
  idChantier: number
): Promise<number> {
  const response = await api.get<number>(
    `/avancements/chantier/${idChantier}/pourcentage`
  );

  return response.data;
}

export async function createAvancement(
  idChantier: number,
  idUtilisateur: number,
  commentaire: string
): Promise<Avancement> {
  const response = await api.post<Avancement>(
    "/avancements",
    null,
    {
      params: {
        idChantier,
        idUtilisateur,
        commentaire,
      },
    }
  );

  return response.data;
}