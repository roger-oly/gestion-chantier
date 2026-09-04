import api from "./api";

export interface ChangePasswordRequest {
  ancienMotDePasse: string;
  nouveauMotDePasse: string;
}

export async function changePassword(
  idUtilisateur: number,
  data: ChangePasswordRequest
): Promise<void> {

  await api.put(
    `/utilisateurs/${idUtilisateur}/mot-de-passe`,
    data
  );
}