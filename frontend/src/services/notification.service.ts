import api from "./api";

import type { Notification } from "../types/notification";


export async function getNotificationsByUtilisateur(
  idUtilisateur: number
): Promise<Notification[]> {

  const response = await api.get<Notification[]>(
    `/notifications/utilisateur/${idUtilisateur}`
  );

  return response.data;
}


export async function getNotificationsNonLues(
  idUtilisateur: number
): Promise<Notification[]> {

  const response = await api.get<Notification[]>(
    `/notifications/utilisateur/${idUtilisateur}/non-lues`
  );

  return response.data;
}


export async function marquerNotificationCommeLue(
  idNotification: number
): Promise<Notification> {

  const response = await api.put<Notification>(
    `/notifications/${idNotification}/lue`
  );

  return response.data;
}


export async function deleteNotification(
  idNotification: number
): Promise<void> {

  await api.delete(
    `/notifications/${idNotification}`
  );

}