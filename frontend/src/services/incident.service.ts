import api from "./api";
import { Incident } from "../types/incident";

/**
 * Récupère les incidents d'un chantier.
 */
export const getIncidentsByChantier = async (
  idChantier: number
): Promise<Incident[]> => {
  const response = await api.get<Incident[]>(
    `/incidents/chantier/${idChantier}`
  );

  return response.data;
};

/**
 * Récupère tous les incidents.
 */
export const getIncidents = async (): Promise<Incident[]> => {
  const response = await api.get<Incident[]>("/incidents");

  return response.data;
};

/**
 * Récupère un incident individuellement.
 */
export const getIncidentById = async (
  id: number
): Promise<Incident> => {
  const response = await api.get<Incident>(`/incidents/${id}`);

  return response.data;
};

/**
 * Crée un nouvel incident.
 */
export const createIncident = async (
  incident: {
    type: string;
    description: string;
    gravite: string;
    dateIncident: string;
    statut: string;
    idChantier: number;
    idUtilisateur: number;
  }
): Promise<Incident> => {

  const response = await api.post<Incident>(
    "/incidents",
    {
      type: incident.type,
      description: incident.description,
      gravite: incident.gravite,
      dateIncident: incident.dateIncident,
      statut: incident.statut,
      idChantier: incident.idChantier,
      idUtilisateur: incident.idUtilisateur,
    }
  );

  return response.data;
};


/**
 * Modifie un incident.
 */
export const updateIncident = async (
  id: number,
  incident: {
    type: string;
    description: string;
    gravite: string;
    dateIncident: string;
    statut: string;
    idChantier: number;
    idUtilisateur: number;
  }
): Promise<Incident> => {

  const response = await api.put<Incident>(
    `/incidents/${id}`,
    {
      type: incident.type,
      description: incident.description,
      gravite: incident.gravite,
      dateIncident: incident.dateIncident,
      statut: incident.statut,
      idChantier: incident.idChantier,
      idUtilisateur: incident.idUtilisateur,
    }
  );

  return response.data;
};

/**
 * Supprime un incident.
 */
export const deleteIncident = async (id: number): Promise<void> => {
  await api.delete(`/incidents/${id}`);
};

