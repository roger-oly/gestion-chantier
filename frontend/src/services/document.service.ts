import api from "./api";

import type { Document } from "../types/document";

export interface DocumentRequest {
  nom: string;
  type: string;
  idChantier: number;
  idUtilisateur: number;
}

export async function getDocuments(): Promise<Document[]> {
  const response = await api.get<Document[]>("/documents");

  return response.data;
}

export async function getDocumentsByChantier(
  idChantier: number
): Promise<Document[]> {

  const response = await api.get<Document[]>(
    `/documents/chantier/${idChantier}`
  );

  return response.data;
}

export async function createDocument(
  data: DocumentRequest,
  fichier: File
) {
  const formData = new FormData();

  formData.append("nom", data.nom);
  formData.append("type", data.type);
  formData.append(
    "idChantier",
    String(data.idChantier)
  );
  formData.append(
    "idUtilisateur",
    String(data.idUtilisateur)
  );
  formData.append("fichier", fichier);

const response = await api.post(
  "/documents",
  formData,
  {
    headers: {
      "Content-Type": undefined,
    },
  }
);

  return response.data;
}

export async function updateDocument(
  id: number,
  data: DocumentRequest
): Promise<Document> {
  const response = await api.put<Document>(
    `/documents/${id}`,
    data
  );

  return response.data;
}

export async function deleteDocument(
  id: number
): Promise<void> {
  await api.delete(`/documents/${id}`);
}
export async function downloadDocument(
  id: number
): Promise<Blob> {

  const response = await api.get(
    `/documents/${id}/fichier`,
    {
      responseType: "blob",
    }
  );

  return response.data;
}